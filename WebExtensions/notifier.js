/*
* Global variables
*/
var _notifForum = 0;
var _notifMP = 0;
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
var _debug = true;
chrome.storage.local.get('debug', (res) => {
  _debug = res.debug || false;
});
var _base_url = "https://zestedesavoir.com/";
var _token = "zds-notifier-firefox";
if(_debug) _base_url = "https://beta.zestedesavoir.com/";

/**
* Get private messages
*/
function getNewPrivateMessages() {
  var target_mp = _base_url + "api/mps/unread/?Authorization=" + _token;
  var xhr_mp = new XMLHttpRequest();
  xhr_mp.open("GET", target_mp, false);
  xhr_mp.send();
  var result_mp = xhr_mp.status;

  //If we are not connected
  if(result_mp === 401) {
    if(_debug) console.log("Not connected");
    //Change the icon
    chrome.browserAction.setIcon({path:"icons/notconnected.png"});
  } else if (result_mp === 200) {
    var parser = new DOMParser();
    var rootDOM = parser.parseFromString(xhr_mp.response, "application/xml");
    if(rootDOM.documentElement.nodeName === "parsererror") {
      if(_debug) console.log("Error while parsing");
    } else {
      var countNotifications = 0;
      //count != 0
      if(rootDOM.documentElement.firstChild.innerHTML != "0") {
        var resultsNotification = rootDOM.documentElement.getElementsByTagName("results")[0].childNodes;
        countNotifications = resultsNotification.length;
        for(var notif = 0; notif < resultsNotification.length; ++notif) {
          var title = resultsNotification[notif].childNodes[2].innerHTML;
          var url = _base_url + "mp/" + resultsNotification[notif].childNodes[0].innerHTML + "/" + encodeURI(title).toLowerCase() + "/messages/";
          var date = resultsNotification[notif].childNodes[4].innerHTML;

          var target_membre = "https://beta.zestedesavoir.com:443/api/membres/" + resultsNotification[notif].childNodes[5].innerHTML + "/?Authorization=" + token;
          var xhr_membre = new XMLHttpRequest();
          xhr_membre.open("GET", target_membre, false);
          xhr_membre.send();
          var result_membre = xhr_membre.status;
          var parserMembre = new DOMParser();
          var membreDOM = parserMembre.parseFromString(xhr_membre.response, "application/xml");
          var pseudo = membreDOM.documentElement.getElementsByTagName("username")[0].innerHTML;
          var avatarUrl = membreDOM.documentElement.getElementsByTagName("avatar_url")[0].innerHTML;
          addNotification(title, pseudo, avatarUrl, date, url);
        }
      }

      //Notify the user
      if(countNotifications > 0) {
        if(_debug) console.log("Nouveaux messages : " + countNotifications);
        chrome.browserAction.setIcon({path:"icons/icone_n_20.png"});
        var title = "Zds-notificateur : MPs !";
        var content = "Vous avez " + countNotifications + " message";
        if (countNotifications > 1) content += "s";
        notifyMe(title, content);
      } else if (countNotifications === 0) {
        chrome.browserAction.setIcon({path:"icons/clem_48.png"});
      }
      _notifMP = countNotifications;
    }
  } else {
    if(_debug) console.log(result_mp);
  }
}

/**
* Get forum notifications
*/
function getNewForumsNotifications() {
  var target = _base_url + "api/notifications/?Authorization=" + _token;
  var xhr = new XMLHttpRequest();
  xhr.open("GET", target, false);
  xhr.send();
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
        //is_read === False
        if(resultsNotification[notif].childNodes[2].innerHTML === "False") {
          countNotifications += 1;
          var titleNotif = resultsNotification[notif].childNodes[1].innerHTML
          var senderNotif = resultsNotification[notif].childNodes[4].childNodes[1].innerHTML;
          var senderAvatarNotif = resultsNotification[notif].childNodes[4].childNodes[5].innerHTML;
          var dateNotif = resultsNotification[notif].childNodes[5].innerHTML;
          var urlNotif = "https://beta.zestedesavoir.com" + resultsNotification[notif].childNodes[3].innerHTML;
          if(_debug) console.log(urlNotif + " by " + senderNotif);
          addNotification(titleNotif, senderNotif, senderAvatarNotif, dateNotif, urlNotif);
        }
      }
      //Notify the user
      if(countNotifications > _notifForum) {
        if(_debug) console.log("Nouvelles notifications : " + countNotifications);
        chrome.browserAction.setIcon({path:"icons/icone_n_20.png"});
        var title = "Zds-notificateur : Nouvelle notification !";
        var content = "Vous avez " + countNotifications + " notification";
        if (countNotifications > 1) content += "s";
        notifyMe(title, content);
      } else if (countNotifications === 0) {
        chrome.browserAction.setIcon({path:"icons/clem_48.png"});
      }
      _notifForum = countNotifications;
    }
  } else {
    if(_debug) console.log(result);
  }
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

/*
* Main function. Get all notification and build the final DOM object
*/
function getNewNotifications() {
  _contentDiv = document.createElement('div');
  getNewForumsNotifications();
  getNewPrivateMessages();
  if(_notifMP === 0 && _notifForum === 0) {
    var divNoNotif = document.createElement('div');
    divNoNotif.id = "noNotif";
    divNoNotif.innerHTML = "Aucune notification";
    document.body.appendChild(divNoNotif);
    if(_debug) console.log("Aucune notification");
  }
  document.body.appendChild(_contentDiv);
  _currentDom = document.body;
}

//Update the popup
setInterval(getNewNotifications, _delayUpdate);
getNewNotifications();
