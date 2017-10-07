/* global chrome close:true */
/* eslint no-undef: "error" */
const notificationsList = document.getElementById('notificationList')

function updateUI () {
  if (chrome.extension.getBackgroundPage().connected) {
    const notConnectedDiv = document.getElementById('notConnected')
    notConnectedDiv.style.display = 'none'
    const bgNodes = chrome.extension.getBackgroundPage().currentDom.lastChild.cloneNode(true)
    notificationsList.appendChild(bgNodes)
  } else {
    const connectedDiv = document.getElementById('connected')
    connectedDiv.style.display = 'none'
  }
}

// sleep time expects milliseconds
function sleep (time) {
  return new Promise((resolve) => setTimeout(resolve, time))
}

notificationsList.addEventListener('click', function () {
  sleep(2000).then(() => {
    if (chrome.extension.getBackgroundPage().notifCounter !== 0) {
      close()
    }
    chrome.extension.getBackgroundPage().getNotificationsFromAPI()
  })
})

updateUI()
