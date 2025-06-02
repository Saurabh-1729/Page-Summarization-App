// get text of current chrome page
if (typeof text === 'undefined') {
    var text = document.body.innerText;
} else {
    text = document.body.innerText;
}

// send text to background
chrome.runtime.sendMessage({
    action: "sendText",
    text: text
});
