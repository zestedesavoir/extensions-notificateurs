/*
* Global letiables
*/
let _notifCounter = 0
const _delayUpdate = 60 * 1000
// Used by the popup
let _currentDom = null
let _contentDiv = null
// If the user is connected
let _connected = false
// If we want to see popup letiables
let _showNotification = false
chrome.storage.local.get('notify', (res) => {
  _showNotification = res.notify || false
})
// If we are in debug mode
const _debug = true
let baseUrl = 'https://zestedesavoir.com/'
const _token = 'zds-notifier'
if (_debug) baseUrl = 'https://beta.zestedesavoir.com/'

function escapeHTML (str) { return str.replace(/[&''<>]/g, (m) => escapeHTML.replacements[m]) }
escapeHTML.replacements = { '&': '&amp', '\'': '&quot', '"': '&#39', '<': '&lt', '>': '&gt' }

/**
* getNotificationsFromAPI
*/
function getNotificationsFromAPI () {
  _contentDiv = document.createElement('div')
  const target = baseUrl + 'api/notifications/?page_size=30&ordering=-pubdate&Authorization=' + _token
  const xhr = new XMLHttpRequest()
  xhr.open('GET', target, true)
  xhr.onload = function (e) {
    if (xhr.readyState === 4) {
      const result = xhr.status
      if (result === 401) {
        _connected = false
        if (_debug) console.log('Not connected')
        // Change popup image
        chrome.browserAction.setIcon({path:'icons/notconnected.png'})
      } else if (result === 200) {
        _connected = true
        const rootDOM = JSON.parse(xhr.response)
        if (rootDOM.details) {
          if (_debug) console.log('Error while parsing')
        } else {
          // Get new notifications
          const resultsNotification = rootDOM.results
          let countNotifications = 0
          for (let notif = 0; notif < resultsNotification.length; ++notif) {
            // If a notification is new we have is_read === False
            if (!resultsNotification[notif].is_read) {
              countNotifications += 1
              const titleNotif = resultsNotification[notif].title
              const senderNotif = resultsNotification[notif].sender.username
              const senderAvatarNotif = resultsNotification[notif].sender.avatar_url
              const dateNotif = resultsNotification[notif].pubdate
              const date = new Date((dateNotif || '').replace(/-/g, '/').replace(/[TZ]/g, ' '))
              let minutes = `${date.getMinutes()}`
              if (minutes.length < 2) {
                minutes = `0${minutes}`
              }
              let formatedDate = `le ${[date.getDate(),
                date.getMonth() + 1].join('/')} à ${[date.getHours(),
                  minutes].join('h')}`
              const actualDate = new Date()
              if (date.getDate() === actualDate.getDate() &&
                 date.getMonth() === actualDate.getMonth() &&
                 date.getYear() === actualDate.getYear()) {
                formatedDate = 'Aujourd\'hui'
              } else {
                const yesterday = actualDate
                yesterday.setDate(actualDate.getDate() - 1)
                if (date.getDate() === yesterday.getDate() &&
                   date.getMonth() === yesterday.getMonth() &&
                   date.getYear() === yesterday.getYear()) {
                  formatedDate = 'Hier'
                }
              }
              const urlNotif = `https://zestedesavoir.com${resultsNotification[notif].url}`
              if (_debug) console.log(`${urlNotif} by ${senderNotif}`)
              addNotification(titleNotif, senderNotif, senderAvatarNotif, formatedDate, urlNotif)
            }
          }
          // Notify the user
          if (countNotifications > _notifCounter) {
            if (_debug) console.log(`Nouvelles notifications : ${countNotifications}`)
            chrome.browserAction.setIcon({path: 'icons/icone_n_20.png'})
            const title = 'Zds-notificateur : Nouvelle notification !'
            let content = `Vous avez ${countNotifications} notification`
            if (countNotifications > 1) content += 's'
            notifyMe(title, content)
          } else if (countNotifications === 0) {
            chrome.browserAction.setIcon({path: 'icons/clem_48.png'})
          }
          _notifCounter = countNotifications
        }
      } else if (_debug) {
        console.log(result)
      }
    }


    if (!_notifCounter) {
      const divNoNotif = document.createElement('div')
      divNoNotif.id = 'noNotif'
      divNoNotif.innerHTML = 'Aucune notification'
      _contentDiv.appendChild(divNoNotif)
      if (_debug) console.log('Aucune notification')
    }
    const body = document.body
    body.appendChild(_contentDiv)
    // Remove useless nodes
    while (body.childNodes.length > 2) {
      body.removeChild(body.childNodes[1])
    }
    _currentDom = body
  }

  xhr.onerror = function (e) {
    console.error(xhr.statusText)
    _connected = false
  }
  xhr.send(null)
}

/*
* Add a notification to the DOM
*/
function addNotification (title, sender, senderAvatar, date, url) {
  // Design popup
  const a = document.createElement('a')
  a.href = url
  a.target = '_blank'
  const divNotif = document.createElement('div')
  divNotif.id = 'notification'
  const imgAvatar = document.createElement('img')
  imgAvatar.src = senderAvatar
  const divBlocNotif = document.createElement('div')
  divBlocNotif.id = 'blocNotif'
  const divDate = document.createElement('div')
  divDate.id = 'date'
  divDate.innerHTML = escapeHTML(date)
  const divPseudo = document.createElement('div')
  divPseudo.id = 'pseudo'
  divPseudo.innerHTML = escapeHTML(sender)
  const divTitle = document.createElement('div')
  divTitle.id = 'title'
  divTitle.innerHTML = escapeHTML(title)

  divBlocNotif.appendChild(divDate)
  divBlocNotif.appendChild(divPseudo)
  divBlocNotif.appendChild(divTitle)
  divNotif.appendChild(imgAvatar)
  divNotif.appendChild(divBlocNotif)
  a.appendChild(divNotif)
  _contentDiv.appendChild(a)
}

/*
* Create a notification
*/
function notifyMe (title, content) {
  if (_showNotification) {
    chrome.notifications.create({
      'type': 'basic',
      'iconUrl': chrome.extension.getURL('icons/icone_n_20.png'),
      'title': title,
      'message': content
    })
  }
}

// Update the popup
setInterval(getNotificationsFromAPI, _delayUpdate)
getNotificationsFromAPI()
