/*
* Global variables
*/
var _notifCounter = 0;
var _delayUpdate = 60 * 1000;
//Used by the popup
var _currentDom = null;
var _contentDiv = null;
//If the user is connected
var _connected = false;
//If we want to see popup variables
var _showNotification = false;
chrome.storage.local.get('notify', (res) => {
  _showNotification = res.notify || false;
});
//If we are in debug mode
var _debug = false;
var _base_url = "https://zestedesavoir.com/";
var _token = "zds-notifier-firefox";
if(_debug) _base_url = "https://beta.zestedesavoir.com/";

/**
* getNotificationsFromAPI
*/
function getNotificationsFromAPI() {
  _contentDiv = document.createElement('div');
  var target = _base_url + "api/notifications/?page_size=30&Authorization=" + _token;
  var xhr = new XMLHttpRequest();
  xhr.open("GET", target, true);
  xhr.onload = function (e) {
    if (xhr.readyState === 4) {
      var result = xhr.status;

      if(result === 401) {
        _connected = false;
        if(_debug) console.log("Not connected");
        //Change popup image
        chrome.browserAction.setIcon({path:"icons/notconnected.png"});
      } else if (result === 200) {
        _connected = true;
        var parser = new DOMParser();
        var rootDOM = parser.parseFromString(xhr.response, "application/xml");
        if(rootDOM.documentElement.nodeName === "parsererror") {
          if(_debug) console.log("Error while parsing");
        } else {
          //Get new notifications
          var resultsNotification = rootDOM.documentElement.getElementsByTagName("results")[0].childNodes;
          var countNotifications = 0;
          for(var notif = 0; notif < resultsNotification.length; ++notif) {
            //If a notification is new we have is_read === False
            if(resultsNotification[notif].childNodes[2].innerHTML === "False") {
              countNotifications += 1;
              var titleNotif = resultsNotification[notif].childNodes[1].innerHTML
              var senderNotif = resultsNotification[notif].childNodes[4].childNodes[1].innerHTML;
              var senderAvatarNotif = resultsNotification[notif].childNodes[4].childNodes[5].innerHTML;
              var dateNotif = resultsNotification[notif].childNodes[5].innerHTML;
              var date = new Date((dateNotif || "").replace(/-/g,"/").replace(/[TZ]/g," "));
              var formatedDate = 'le ' + [date.getDate(),
               date.getMonth()+1].join('/') + ' à ' +
              [date.getHours(),
               date.getMinutes()].join('h');
              var urlNotif = "https://zestedesavoir.com" + resultsNotification[notif].childNodes[3].innerHTML;
              if(_debug) console.log(urlNotif + " by " + senderNotif);
              addNotification(titleNotif, senderNotif, senderAvatarNotif, formatedDate, urlNotif);
            }
          }
          //Notify the user
          if(countNotifications > _notifCounter) {
            if(_debug) console.log("Nouvelles notifications : " + countNotifications);
            chrome.browserAction.setIcon({path:"icons/icone_n_20.png"});
            var title = "Zds-notificateur : Nouvelle notification !";
            var content = "Vous avez " + countNotifications + " notification";
            if (countNotifications > 1) content += "s";
            notifyMe(title, content);
          } else if (countNotifications === 0) {
            chrome.browserAction.setIcon({path:"icons/clem_48.png"});
          }
          _notifCounter = countNotifications;
        }
      } else {
        if(_debug) console.log(result);
      }
    }


    if(!_notifCounter) {
      var divNoNotif = document.createElement('div');
      divNoNotif.id = "noNotif";
      divNoNotif.innerHTML = "Aucune notification";
      _contentDiv.appendChild(divNoNotif);
      if(_debug) console.log("Aucune notification");
    }
    var body = document.body;
    body.appendChild(_contentDiv);
    //Remove useless nodes
    while(body.childNodes.length > 2) {
      body.removeChild(body.childNodes[1]);
    }
    _currentDom = body;
  };

  xhr.onerror = function (e) {
    console.error(xhr.statusText);
    _connected = false;
  };
  xhr.send(null);
}

/*
* Add a notification to the DOM
*/
function addNotification(title, sender, senderAvatar, date, url) {
  //Design popup
  var a = document.createElement('a');
  a.href = url;
  a.target = "_blank";
  var divNotif = document.createElement('div');
  divNotif.id = "notification";
  imgAvatar = document.createElement('img');
  imgAvatar.src = senderAvatar;
  var divBlocNotif = document.createElement('div');
  divBlocNotif.id="blocNotif";
  var divDate = document.createElement('div');
  divDate.id = "date";
  divDate.innerHTML = date;
  var divPseudo = document.createElement('div');
  divPseudo.id = "pseudo";
  divPseudo.innerHTML = sender;
  var divTitle = document.createElement('div');
  divTitle.id = "title";
  divTitle.innerHTML = title;

  divBlocNotif.appendChild(divDate);
  divBlocNotif.appendChild(divPseudo);
  divBlocNotif.appendChild(divTitle);
  divNotif.appendChild(imgAvatar);
  divNotif.appendChild(divBlocNotif);
  a.appendChild(divNotif);
  _contentDiv.appendChild(a);
}

/*
* Create a notification
*/
function notifyMe(title, content) {
  if(_showNotification) {
    chrome.notifications.create({
      "type": "basic",
      "iconUrl": chrome.extension.getURL("icons/icone_n_20.png"),
      "title": title,
      "message": content
    });
  }
}

//Update the popup
setInterval(getNotificationsFromAPI, _delayUpdate);
getNotificationsFromAPI();
