//Get preferences
var preferences = Components.classes["@mozilla.org/preferences-service;1"]
.getService(Components.interfaces.nsIPrefService);
preferences = preferences.getBranch("extensions.zds-notif.");

//Main timer.
var prefTimer = preferences.getIntPref("timer-refresh");
var timer = Components.classes["@mozilla.org/timer;1"]
.createInstance(Components.interfaces.nsITimer);
timer.initWithCallback(getNotifAndMP, prefTimer*1000, Components.interfaces.nsITimer.TYPE_REPEATING_PRECISE_CAN_SKIP);

var g_preNotifF = 0;
var g_preNotifMP = 0;
var g_showNotifF = true;

function escapeHTML(str) str.replace(/[&"<>]/g, function (m) escapeHTML.replacements[m]);
escapeHTML.replacements = { "&": "&amp;", '"': "&quot;", "<": "&lt;", ">": "&gt;" };

//When we click on a link. Remove the event and open a new tab.
function linkClick(e) {
  var win = Components.classes['@mozilla.org/appshell/window-mediator;1']
  .getService(Components.interfaces.nsIWindowMediator)
  .getMostRecentWindow('navigator:browser');

  if (e.target.tagName == 'html:a')
  win.gBrowser.selectedTab = win.gBrowser.addTab(e.target.href);
  else
  win.gBrowser.selectedTab = win.gBrowser.addTab(e.target.parentNode.href);

  e.preventDefault();
}

function notifyMe(text) {
  var showNotif = preferences.getBoolPref("show-notification");
  //Note: Notification.sound is not supported yet
  var soundFile = preferences.getComplexValue("sound-notif", Components.interfaces.nsILocalFile);
  var options = {
    sound:soundFile.path,
    icon:'chrome://zds-notif/skin/images/icone_20.png'
  }
  if (!("Notification" in window) || !showNotif) { }
  else if (Notification.permission === "granted") {
    var notification = new Notification(text, options);
  }
  else if (Notification.permission !== 'denied') {
    Notification.requestPermission(function (permission) {
      if (permission === "granted") {
        var notification = new Notification(text, options);
      }
    });
  }
}

function HTMLParser(aHTMLString) {
  var html = document.implementation.createDocument("http://www.w3.org/1999/xhtml", "html", null),
  body = document.createElementNS("http://www.w3.org/1999/xhtml", "body");
  html.documentElement.appendChild(body);

  body.appendChild(Components.classes["@mozilla.org/feed-unescapehtml;1"]
  .getService(Components.interfaces.nsIScriptableUnescapeHTML)
  .parseFragment(aHTMLString, false, null, body));

  return body;
}

var StateEnum = {
  NO_NOTIFICATION : 0,
  NEW_FORUM_NOTIF : 1,
  NEW_MP_NOTIF : 2,
  NOT_CONNECTED : 3
}

function changeIconBtn(state)
{
  var iconBtn = document.getElementById('zds-notif-button');
  var newImage;
  switch (state) {
    case StateEnum.NO_NOTIFICATION:
    newImage = 'chrome://zds-notif/skin/images/icone_20.png';
    break;
    case StateEnum.NEW_FORUM_NOTIF:
    newImage = 'chrome://zds-notif/skin/images/icone_n_20.png';
    break;
    case StateEnum.NEW_MP_NOTIF:
    newImage = 'chrome://zds-notif/skin/images/icone_m_20.png';
    break;
    case StateEnum.NOT_CONNECTED:
    newImage = 'chrome://zds-notif/skin/images/icone_20_logout.png';
    break;
    default:
    newImage = 'chrome://zds-notif/skin/images/icone_20_logout.png'
    break;

  }
  iconBtn.setAttribute('image', newImage);
}

function HTMLToXUL(htmlNotif)
{
  htmlNotif = htmlNotif.substring(htmlNotif.indexOf('<ul'), htmlNotif.indexOf('</ul>') + 5);

  //Convert HTML in XUL
  htmlNotif = htmlNotif.replace(/<span/g, '<html:span');
  htmlNotif = htmlNotif.replace(/<\/span/g, '</html:span');
  htmlNotif = htmlNotif.replace(/<ul/g, '<html:ul');
  htmlNotif = htmlNotif.replace(/<\/ul/g, '</html:ul');
  htmlNotif = htmlNotif.replace(/<li/g, '<html:li');
  htmlNotif = htmlNotif.replace(/<\/li/g, '</html:li');
  htmlNotif = htmlNotif.replace(/<a/g, '<html:a');
  htmlNotif = htmlNotif.replace(/<\/a/g, '</html:a');
  htmlNotif = htmlNotif.replace(/href=\"\//g, 'href=\"https://zestedesavoir.com/');
  htmlNotif = htmlNotif.replace(/src=\"\//g, 'src=\"https://zestedesavoir.com/');
  htmlNotif = htmlNotif.replace('<img(.*)>', '');
  escapeHTML(htmlNotif);
  return htmlNotif;
}

function getNotifAndMP() {

  var oReq = new XMLHttpRequest();
  oReq.open("GET", 'https://zestedesavoir.com', true);//DEBUG
  oReq.onload = function() {
    //To have real links
    var DOMPars = HTMLParser(this.responseText.replace(/href=\"\//g, 'href="https://zestedesavoir.com/'));
    var isConnected = false;

    var dropdownContent = DOMPars.getElementsByClassName('dropdown');
    var dropdown = document.querySelector('.dropdown');
    for (var i = 0; i < dropdownContent.length; ++i) {
      //Get forums posts
      if (dropdownContent[i].innerHTML.indexOf('Notifications') != -1) {
        //If we can check notifications, the user is connected.
        isConnected = true;

        //Check notifications
        var htmlNotif = dropdownContent[i].innerHTML;
        var notiflab = document.querySelector('.notiflab');
        var nbNotif = htmlNotif.split("<a").length - 2;
        if (nbNotif > 0) {
          changeIconBtn(StateEnum.NEW_FORUM_NOTIF);
          if(nbNotif > 1 && g_preNotifF < nbNotif) {
            notifyMe('Vous avez ' + nbNotif.toString() + ' nouvelles notifications sur le forum');
          }
          else if(g_preNotifF < nbNotif) {
            notifyMe('Vous avez une nouvelle notification sur le forum');
          }
        }
        g_preNotifF = nbNotif;
        notiflab.innerHTML = escapeHTML((htmlNotif.split("<a").length - 2).toString());

        if(g_showNotifF) {
          dropdown.innerHTML = HTMLToXUL(htmlNotif);
          dropdown.innerHTML += '<html:a href="https://zestedesavoir.com/forums/notifications/" class="dropdown-link-all">Toutes les notifications</html:a>';
        }
      }

      if (dropdownContent[i].innerHTML.indexOf('Messagerie') != -1) {
        var htmlNotif = dropdownContent[i].innerHTML;
        var mplab = document.querySelector('.mplab');
        var nbNotif = htmlNotif.split("<a").length - 2;
        if (nbNotif > 0) {
          changeIconBtn(StateEnum.NEW_MP_NOTIF);
          if(nbNotif > 1 && g_preNotifMP < nbNotif) {
            notifyMe('Vous avez ' + nbNotif.toString() + ' nouveaux messages privé');
          }
          else if(g_preNotifMP < nbNotif) {
            notifyMe('Vous avez 1 nouveau message privé');
          }
        }
        g_preNotifMP = nbNotif;
        mplab.innerHTML = escapeHTML((htmlNotif.split("<a").length - 2).toString());


        if(!g_showNotifF) {
          dropdown.innerHTML = HTMLToXUL(htmlNotif);
          dropdown.innerHTML += '<html:a href="https://zestedesavoir.com/mp/" class="dropdown-link-all">Tous les messages</html:a>';
        }
      }
    }

    if(g_preNotifF == 0 && g_preNotifMP == 0)
    {
      changeIconBtn(StateEnum.NO_NOTIFICATION);
    }

    if(!isConnected)
    {
      changeIconBtn(StateEnum.NOT_CONNECTED);
      dropdown.innerHTML = '<html:a href="https://zestedesavoir.com/membres/connexion/?next=/" class="dropdown-link-all">Connexion</html:a>';
      var mplab = document.querySelector('.mplab');
      var notiflab = document.querySelector('.notiflab');
      mplab.innerHTML = 'x';
      notiflab.innerHTML = 'x';
    }

    //To open a link in a new tab
    var Anchors = document.getElementsByTagName("html:a");
    for (var i = 0; i < Anchors.length; i++)
    Anchors[i].addEventListener("click", linkClick, false);

    var tempAnchors = document.getElementsByTagName("a");
    for (var i = 0; i < tempAnchors.length; i++)
    tempAnchors[i].addEventListener("click", linkClick, false);
  };
  oReq.send(null);
}
