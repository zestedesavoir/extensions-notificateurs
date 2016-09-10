if(chrome.extension.getBackgroundPage()._connected) {
  var notConnectedDiv = document.getElementById('notConnected');
  notConnectedDiv.style.display = 'none';
  var notifications = document.getElementById('notificationList');
  var bgNodes = chrome.extension.getBackgroundPage()._currentDom.childNodes[1].cloneNode(true);
  notifications.appendChild(bgNodes);
} else {
  var connectedDiv = document.getElementById('connected');
  connectedDiv.style.display = 'none';
}
