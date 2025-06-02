// Run when popup opens
// Fixed popup.js
// document.addEventListener('DOMContentLoaded', () => {
//     chrome.tabs.query({active: true, currentWindow: true}, tabs => {
//         // Inject content script to get page text
//         chrome.scripting.executeScript({
//             target: {tabId: tabs[0].id},
//             files: ['content.js']
//         });
//     });
// });

// Listen for summary from background
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    if (request.action === "summaryReady") {
        document.getElementById('summary').innerText = request.summary;
    }
});

document.addEventListener('DOMContentLoaded', () => {
    console.log("Popup opened - Extension clicked by user");

    // Listen for summary from background script
    chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
        if (request.action === "summaryReady") {
            console.log("Received summary in popup:", request.summary);
            document.getElementById('summary').innerText = request.summary;
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

// document.addEventListener('DOMContentLoaded', async () => {
//     console.log("Popup opened - Extension clicked by user");

//     try {
//         const tabs = await chrome.tabs.query({ active: true, currentWindow: true });
//         const currentTab = tabs[0];

//         // Show loading message
//         document.getElementById('summary').innerText = "Loading summary...";

//         // Set up one-time message listener
//         const handleMessage = (request, sender, sendResponse) => {
//             if (request.action === "summaryReady") {
//                 console.log("Summary received, displaying");
//                 document.getElementById('summary').innerText = request.summary;
//             } else if (request.action === "summaryError") {
//                 console.error("Error received:", request.error);
//                 document.getElementById('summary').innerText = request.error;
//             }

//             chrome.runtime.onMessage.removeListener(handleMessage);
//         };

//         chrome.runtime.onMessage.addListener(handleMessage);

//         // Inject content script to extract page content
//         await chrome.scripting.executeScript({
//             target: { tabId: currentTab.id },
//             func: extractPageContent
//         });

//     } catch (error) {
//         console.error("Popup error:", error);
//         document.getElementById('summary').innerText = "An error occurred. Please try again.";
//     }
// });

// // This must be defined for injection
// function extractPageContent() {
//     const text = document.body.innerText || "";
//     chrome.runtime.sendMessage({
//         action: "sendText",
//         text: text.trim().slice(0, 10000) // optional: limit text length
//     });
// }
