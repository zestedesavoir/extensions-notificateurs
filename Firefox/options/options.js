function saveOptions(e) {
  chrome.storage.local.set({
    notify: document.querySelector("#notify").checked
  });
}

function restoreOptions() {
  chrome.storage.local.get('notify', (res) => {
    document.querySelector("#notify").checked = res.notify || false;
  });
}

document.addEventListener('DOMContentLoaded', restoreOptions);
document.querySelector("form").addEventListener("submit", saveOptions);
