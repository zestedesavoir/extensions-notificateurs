function saveOptions(e) {
  chrome.storage.local.set({
    debug: document.querySelector("#debug").checked,
    notify: document.querySelector("#notify").checked
  });
}

function restoreOptions() {
  chrome.storage.local.get('debug', (res) => {
    document.querySelector("#debug").checked = res.debug || false;
  });
  chrome.storage.local.get('notify', (res) => {
    document.querySelector("#notify").checked = res.notify || false;
  });
}

document.addEventListener('DOMContentLoaded', restoreOptions);
document.querySelector("form").addEventListener("submit", saveOptions);
