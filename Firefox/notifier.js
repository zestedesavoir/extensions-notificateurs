/* global chrome XMLHttpRequest:true */
/* eslint-disable no-console */
const debugMode = false
const NOT_CONNECTED = 401
const CONNECTED = 200

// TODO move this
var connected = false
var notifCounter = 0
var currentDom = null
let contentDiv = null

// Load preferences
const updateDelay = 60 * 1000
let showPopupNotification = false
chrome.storage.local.get('notify', (res) => {
  showPopupNotification = res.notify || false
})

let baseUrl = 'https://zestedesavoir.com/'
if (debugMode) baseUrl = 'https://beta.zestedesavoir.com/'
const token = 'zds-notifier'

/**
 * Get notifications from https://zestedesavoir.com via the API
 */
function getNotificationsFromAPI () {
  contentDiv = document.createElement('div')
  const options = `page_size=30&ordering=-pubdate&Authorization=${token}`
  const target = `${baseUrl}api/notifications/?${options}`
  // TODO use fetch instead
  const xhr = new XMLHttpRequest()
  xhr.open('GET', target, true)
  xhr.onload = function (e) {
    if (xhr.readyState === 4) {
      const result = xhr.status
      if (result === NOT_CONNECTED) {
        connected = false
        if (debugMode) console.log('Not connected')
        chrome.browserAction.setIcon({path: 'icons/notconnected.png'})
      } else if (result === CONNECTED) {
        connected = true
        const rootDOM = JSON.parse(xhr.response)
        if (rootDOM.details) {
          if (debugMode) console.log('can\'t parse incorrect JSON')
        } else {
          parseNotifications(rootDOM.results)
        }
      } else if (debugMode) {
        console.log(result)
      }
    }

    buildPopup()
  }

  xhr.onerror = function (e) {
    console.error(xhr.statusText)
    connected = false
  }
  xhr.send(null)
}

/**
 * Build the DOM of the popup
 */
function buildPopup () {
  if (!notifCounter) {
    const divNoNotif = document.createElement('div')
    divNoNotif.id = 'noNotif'
    divNoNotif.innerText = 'Aucune notification'
    contentDiv.appendChild(divNoNotif)
    if (debugMode) console.log(divNoNotif.innerText)
  }
  const body = document.body
  body.appendChild(contentDiv)
  // Remove useless nodes
  while (body.childNodes.length > 2) {
    body.removeChild(body.childNodes[1])
  }
  currentDom = body
}

/**
 * Parse the JSON returned by the API
 * @param  results the JSON object
 */
function parseNotifications (results) {
  // Get new notifications
  let notificationsCounter = 0
  for (const notif of results) {
    // If a notification is new we have is_read === False
    if (!notif.is_read) {
      notificationsCounter += 1
      const title = notif.title
      const author = notif.sender.username
      const authorAvatar = notif.sender.avatar_url

      const urlNotif = `${baseUrl}${notif.url}`
      if (debugMode) console.log(`${urlNotif} by ${author}`)
      addNotification(title, author, authorAvatar, formatDate(notif.pubdate), urlNotif)
    }
  }
  // Notify the user
  if (notificationsCounter > notifCounter) {
    if (debugMode) console.log(`New notification: ${notificationsCounter}`)
    chrome.browserAction.setIcon({path: 'icons/icone_n_20.png'})
    const title = 'Zds-notificateur : Nouvelle notification !'
    let content = `Vous avez ${notificationsCounter} notification`
    if (notificationsCounter > 1) content += 's'
    popupNotification(title, content)
  } else if (notificationsCounter === 0) {
    chrome.browserAction.setIcon({path: 'icons/clem_48.png'})
  }
  notifCounter = notificationsCounter
}

/**
 * Pretty print a publication date
 * @param  pubdate
 */
function formatDate (pubdate) {
  const date = new Date((pubdate || '').replace(/-/g, '/').replace(/[TZ]/g, ' '))

  const actualDate = new Date()
  if (date.toDateString() === actualDate.toDateString()) {
    return 'Aujourd\'hui'
  }

  const yesterday = actualDate
  yesterday.setDate(actualDate.getDate() - 1)
  if (date.toDateString() === yesterday.toDateString()) {
    return 'Hier'
  }

  let minutes = `${date.getMinutes()}`
  if (minutes.length < 2) {
    minutes = `0${minutes}`
  }
  const shortDate = [date.getDate(), date.getMonth() + 1].join('/')
  const time = [date.getHours(), minutes].join('h')
  return `le ${shortDate} à ${time}`
}

/**
 * Add a notification entry
 * @param title of the subject
 * @param author
 * @param authorAvatar
 * @param date
 * @param url
 */
function addNotification (title, author, authorAvatar, date, url) {
  const divBlocNotif = document.createElement('div')
  divBlocNotif.id = 'blocNotif'
  const divDate = document.createElement('div')
  divDate.id = 'date'
  divDate.innerText = date
  const divPseudo = document.createElement('div')
  divPseudo.id = 'pseudo'
  divPseudo.innerText = author
  const divTitle = document.createElement('div')
  divTitle.id = 'title'
  divTitle.innerText = title
  const imgAvatar = document.createElement('img')
  imgAvatar.src = authorAvatar
  const divNotif = document.createElement('div')
  divNotif.id = 'notification'
  const a = document.createElement('a')
  a.href = url
  a.target = '_blank' // Open the notification in a new window

  divBlocNotif.appendChild(divDate)
  divBlocNotif.appendChild(divPseudo)
  divBlocNotif.appendChild(divTitle)
  divNotif.appendChild(imgAvatar)
  divNotif.appendChild(divBlocNotif)
  a.appendChild(divNotif)
  contentDiv.appendChild(a)
}

/**
 * Show a popup notification
 * @param  title of the popup
 * @param  content
 */
function popupNotification (title, content) {
  if (showPopupNotification) {
    chrome.notifications.create({
      'type': 'basic',
      'iconUrl': chrome.extension.getURL('icons/icone_n_20.png'),
      'title': title,
      'message': content
    })
  }
}

// Update the popup
setInterval(getNotificationsFromAPI, updateDelay)
getNotificationsFromAPI()

/**
 * Compare current state with the one of the loading page
 * @param  the content of the message request
 */

function updateState(request) {
  const lastState = connected ? (notifCounter > 0 ? 2 : 1 ) : 0;

  if( request.state !== lastState ) { // If any difference update the state
    switch( request.state ) {
      case 1:
        chrome.browserAction.setIcon({path: 'icons/clem_48.png'})
        break;
      case 2:
        chrome.browserAction.setIcon({path: 'icons/icone_n_20.png'})
        break;
      case 0:
        chrome.browserAction.setIcon({path: 'icons/notconnected.png'})
        break;
    }
    lastIcon = request.state;
    getNotificationsFromAPI(); // Update the state
  }
}

browser.runtime.onMessage.addListener(updateState);
