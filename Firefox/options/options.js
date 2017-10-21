function saveOptions(e) {
  chrome.storage.local.set({
    notify: document.querySelector("#notify").checked,
    white_theme: document.querySelector("#white_theme").checked
  });
}

function restoreOptions() {
  chrome.storage.local.get('notify', (res) => {
    document.querySelector("#notify").checked = res.notify || false;
  });
  chrome.storage.local.get('white_theme', (res) => {
    document.querySelector("#white_theme").checked = res.white_theme || false;
  });
}

document.addEventListener('DOMContentLoaded', restoreOptions);
document.querySelector("form").addEventListener("submit", saveOptions);
