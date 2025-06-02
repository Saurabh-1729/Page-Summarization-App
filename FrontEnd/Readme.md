Key Features:
Popup/UI: When a user clicks on the extension icon, a popup appears displaying the summary of the current page.

Content Script: A content script will read the DOM of the current page, extract the textual content, and send it to the backend for summarization.

Background Script: The background script manages communication between the content script and the popup.

Files:
manifest.json: This defines the metadata of your Chrome extension.

popup.html: A simple UI to show the summary of the page.

popup.js: Handles logic for UI interactions, like sending data to the backend.

content.js: A content script that extracts the text from the current page and sends it to the backend.

background.js: Manages API calls to your Spring Boot backend.

2. Backend with Spring Boot
On the backend, we will have an API endpoint that accepts the raw text of the page, processes it (like using a summarization algorithm), and sends back a summary.

User clicks extension → Popup opens → Triggers content script → 
Content script extracts text → Sends to background → 
Background calls API → Background sends summary to popup → 
Popup displays summary
