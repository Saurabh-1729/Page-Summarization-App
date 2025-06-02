// Get selected text
selection = window.getSelection();
selectedText = selection.toString().trim();

if (selectedText && selectedText.length > 0) {
    // Use selected text if available
    text = selectedText;
} else {
    // Fallback to entire page content if no selection
    text = document.body.innerText;
}

// send text to background
chrome.runtime.sendMessage({
    action: "sendText",
    text: text
});
