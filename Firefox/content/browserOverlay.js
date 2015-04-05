if ("undefined" == typeof(ZDSNotif)) {
    var ZDSNotif = {};
};

ZDSNotif.BrowserOverlay = {
    showNotif: true,
    isMP: false,
    preNotifF: 0,
    preNotifMP: 0,
    init: function() {
        window.removeEventListener("load", ZDSNotif.BrowserOverlay.init, false);
        var timer = Components.classes["@mozilla.org/timer;1"]
            .createInstance(Components.interfaces.nsITimer);
        timer.initWithCallback(ZDSNotif.BrowserOverlay.updateUI, 60000,
            Components.interfaces.nsITimer.TYPE_REPEATING_SLACK);
        ZDSNotif.BrowserOverlay.updateUI();
    },
    updateUI: function() {
        getNotifAndMP();

        function HTMLParser(aHTMLString) {
            var html = document.implementation.createDocument("http://www.w3.org/1999/xhtml", "html", null),
                body = document.createElementNS("http://www.w3.org/1999/xhtml", "body");
            html.documentElement.appendChild(body);

            body.appendChild(Components.classes["@mozilla.org/feed-unescapehtml;1"]
                .getService(Components.interfaces.nsIScriptableUnescapeHTML)
                .parseFragment(aHTMLString, false, null, body));

            return body;
        }

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
          if (!("Notification" in window)) { }
          else if (Notification.permission === "granted") {
            var notification = new Notification(text);
          }
          else if (Notification.permission !== 'denied') {
            Notification.requestPermission(function (permission) {
              if (permission === "granted") {
                var notification = new Notification(text);
              }
            });
          }
         }

        function getNotifAndMP() {
            var toolbarbutton = document.getElementById('zds-notif-button');
            var dropdown = document.querySelector('.dropdown');
            var oReq = new XMLHttpRequest();
            oReq.open("GET", 'https://zestedesavoir.com', true);
            oReq.onload = function() {
                //Parser
                var DOMPars = HTMLParser(this.responseText.replace(/href=\"\//g, 'href="https://zestedesavoir.com/'));
                var isConnected = false;
                for (var i = 0; i < DOMPars.getElementsByClassName('dropdown').length; ++i) {
                    //Notification
                    if (DOMPars.getElementsByClassName('dropdown')[i].innerHTML.indexOf('Notifications') != -1) {
                        var htmlNotif = DOMPars.getElementsByClassName('dropdown')[i].innerHTML;
                        var notiflab = document.querySelector('.notiflab');
                        if (htmlNotif.split("<a").length - 2 > 0) {
                            toolbarbutton.setAttribute('image', 'chrome://zds-notif/skin/images/icone_n_20.png');
                            var nbNotif = htmlNotif.split("<a").length - 2;
                            if(nbNotif > 1 && ZDSNotif.BrowserOverlay.preNotifF < nbNotif) {
                              notifyMe('Clem Notificateur : Vous avez ' + nbNotif.toString() + ' nouvelles notifications sur le forum');
                              ZDSNotif.BrowserOverlay.preNotifF = nbNotif;
                            }
                            else if(ZDSNotif.BrowserOverlay.preNotifF < nbNotif) {
                              notifyMe('Clem Notificateur : Vous avez une nouvelle notification sur le forum');
                              ZDSNotif.BrowserOverlay.preNotifF = nbNotif;
                            }
                        }
                        else if (!ZDSNotif.BrowserOverlay.isMP) {
                            toolbarbutton.setAttribute('image', 'chrome://zds-notif/skin/images/icone_20.png');
                            ZDSNotif.BrowserOverlay.preNotifF = 0;
                        }
                        notiflab.innerHTML = (htmlNotif.split("<a").length - 2).toString();

                        if (ZDSNotif.BrowserOverlay.showNotif) {
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
                            dropdown.innerHTML = htmlNotif;
                        }

                        isConnected = true;
                    }
                    //PM
                    if (DOMPars.getElementsByClassName('dropdown')[i].innerHTML.indexOf('Messagerie') != -1) {
                        var htmlNotif = DOMPars.getElementsByClassName('dropdown')[i].innerHTML;
                        var mplab = document.querySelector('.mplab');
                        if (htmlNotif.split("<a").length - 2 > 0) {
                            toolbarbutton.setAttribute('image', 'chrome://zds-notif/skin/images/icone_m_20.png');
                            ZDSNotif.BrowserOverlay.isMP = true;
                            var nbNotif = htmlNotif.split("<a").length - 2;
                            if(nbNotif > 1 && ZDSNotif.BrowserOverlay.preNotifMP < nbNotif) {
                              notifyMe('Clem Notificateur : Vous avez ' + nbNotif.toString() + ' nouveaux messages privé');
                              ZDSNotif.BrowserOverlay.preNotifMP = nbNotif;
                            }
                            else if(ZDSNotif.BrowserOverlay.preNotifMP < nbNotif) {
                              notifyMe('Clem Notificateur : Vous avez 1 nouveau message privé');
                              ZDSNotif.BrowserOverlay.preNotifMP = nbNotif;
                            }
                        } else {
                            toolbarbutton.setAttribute('image', 'chrome://zds-notif/skin/images/icone_20.png');
                            ZDSNotif.BrowserOverlay.isMP = false;
                            ZDSNotif.BrowserOverlay.preNotifMP = 0;
                        }
                        mplab.innerHTML = (htmlNotif.split("<a").length - 2).toString();

                        if (!ZDSNotif.BrowserOverlay.showNotif) {
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
                            dropdown.innerHTML = htmlNotif;
                        }
                        isConnected = true;
                    }
                }
                var toParse = dropdown.innerHTML;
                //If we are disconnected
                if (!isConnected) {
                    dropdown.innerHTML = '<html:a href="https://zestedesavoir.com/membres/connexion/?next=/" class="dropdown-link-all">Connexion</html:a>';
                    toolbarbutton.setAttribute('image', 'chrome://zds-notif/skin/images/icone_20_logout.png');
                    var mplab = document.querySelector('.mplab');
                    var notiflab = document.querySelector('.notiflab');
                    mplab.innerHTML = 'x';
                    notiflab.innerHTML = 'x';
                } else {
                    if (ZDSNotif.BrowserOverlay.showNotif)
                        dropdown.innerHTML += '<html:a href="https://zestedesavoir.com/forums/notifications/" class="dropdown-link-all">Toutes les notifications</html:a>';
                    else
                        dropdown.innerHTML += '<html:a href="https://zestedesavoir.com/mp/" class="dropdown-link-all">Tous les messages</html:a>';
                }

                var Anchors = document.getElementsByTagName("html:a");
                for (var i = 0; i < Anchors.length; i++)
                    Anchors[i].addEventListener("click", linkClick, false);

                var tempAnchors = document.getElementsByTagName("a");
                for (var i = 0; i < tempAnchors.length; i++)
                    tempAnchors[i].addEventListener("click", linkClick, false);
            };
            oReq.send(null);
        }
    }
};


//Listener
window.addEventListener("load", ZDSNotif.BrowserOverlay.init, false);
window.addEventListener('click', ZDSNotif.BrowserOverlay.updateUI, false);
