function convertMarkdownToHtml(text) {
    return text
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>') // Bold
        .replace(/\*(.*?)\*/g, '<em>$1</em>') // Italic
        .replace(/\n/g, '<br>'); // Line breaks
}

document.addEventListener('DOMContentLoaded', () => {
    console.log("Popup opened - Extension clicked by user");

    // Listen for summary from background script
    chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
        if (request.action === "summaryReady") {
            const htmlContent = convertMarkdownToHtml(request.summary);
            console.log("Received summary in popup:", request.summary);
            // Use innerHTML instead of innerText to render HTML
            document.getElementById('summary').innerHTML = htmlContent;
        }
    });

    // Show loading message
    document.getElementById('summary').innerText = "Loading summary...";

    // NOW inject content script only when user clicks extension
    chrome.tabs.query({active: true, currentWindow: true}, tabs => {
        console.log("Injecting content script into tab:", tabs[0].url);
        chrome.scripting.executeScript({
            target: {tabId: tabs[0].id},
            files: ['content.js']
        });
    });
});
