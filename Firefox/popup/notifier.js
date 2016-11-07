var _notifications = document.getElementById('notificationList');

function updateUI() {
  if(chrome.extension.getBackgroundPage()._connected) {
    var notConnectedDiv = document.getElementById('notConnected');
    notConnectedDiv.style.display = 'none';
    var bgNodes = chrome.extension.getBackgroundPage()._currentDom.lastChild.cloneNode(true);
    _notifications.appendChild(bgNodes);
  } else {
    var connectedDiv = document.getElementById('connected');
    connectedDiv.style.display = 'none';
  }
}

// sleep time expects milliseconds
function sleep (time) {
  return new Promise((resolve) => setTimeout(resolve, time));
}

_notifications.addEventListener("click", function(){
  sleep(2000).then(() => {
    if(chrome.extension.getBackgroundPage()._notifCounter !== 0) {
      close();
    }
    chrome.extension.getBackgroundPage().getNotificationsFromAPI();
  });
});

updateUI();
