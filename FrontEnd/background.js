chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    if (request.action === "sendText") {
        console.log("Received text from content script, sending to backend...");

        fetch('http://localhost:8080/api/summary/getSummary', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ text: request.text })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Backend error ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                if (data && data.summary) {
                    chrome.runtime.sendMessage({
                        action: "summaryReady",
                        summary: data.summary
                    }, (response) => {
                        if (chrome.runtime.lastError) {
                            console.warn("No receiver for summaryReady:", chrome.runtime.lastError.message);
                        }
                    });
                } else {
                    throw new Error("Invalid summary format from backend");
                }
            })
            .catch(error => {
                console.error("Backend error:", error.message);
                // Send error as summaryReady with error message to match popup.js expectations
                chrome.runtime.sendMessage({
                    action: "summaryReady",
                    summary: "AI service is unavailable. Please try again later."
                }, (response) => {
                    if (chrome.runtime.lastError) {
                        console.warn("No receiver for summaryReady:", chrome.runtime.lastError.message);
                    }
                });
            });

        return true; // Keep the message channel open
    }
});
