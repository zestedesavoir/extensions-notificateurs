//TODO : Afficher les MP, quitter au clic d'une notification ?, Refaire le code pour qu'il ressemble plus à la version chrome ? Notifications ?

if ("undefined" == typeof(ZDSNotif)) {
  var ZDSNotif = {
  };
};

ZDSNotif.BrowserOverlay = {
  init: function() 
  {
    window.removeEventListener("load", ZDSNotif.BrowserOverlay.init, false);

    /*//Ajouter un système de préférence (pour régler les listeners)
    var prefs = Components.classes["@mozilla.org/preferences-service;1"]
                          .getService(Components.interfaces.nsIPrefService);
    prefs = prefs.getBranch("extensions.zds-notif.");
    prefs.addObserver("", this, false);*/

    var timer = Components.classes["@mozilla.org/timer;1"]
                          .createInstance(Components.interfaces.nsITimer);
    timer.initWithCallback(ZDSNotif.BrowserOverlay.updateUI, 60000, Components.interfaces.nsITimer.TYPE_REPEATING_SLACK);
    
    ZDSNotif.BrowserOverlay.updateUI();
  },
  updateUI: function()
  {
    getNotifAndMP();
    function HTMLParser(aHTMLString){
      var html = document.implementation.createDocument("http://www.w3.org/1999/xhtml", "html", null),
      body = document.createElementNS("http://www.w3.org/1999/xhtml", "body");
      html.documentElement.appendChild(body);

      body.appendChild(Components.classes["@mozilla.org/feed-unescapehtml;1"]
      .getService(Components.interfaces.nsIScriptableUnescapeHTML)
      .parseFragment(aHTMLString, false, null, body));

      return body;
    }

    //Lorsque l'on clic sur un lien. Désactive l'évènement (pour éviter un bug) et ouvre un nouvel onglet.
    function linkClick(e) {
      var win = Components.classes['@mozilla.org/appshell/window-mediator;1']
                  .getService(Components.interfaces.nsIWindowMediator)
                  .getMostRecentWindow('navigator:browser');

      if(e.target.tagName == 'html:a')
        win.gBrowser.selectedTab = win.gBrowser.addTab(e.target.href);
      else
        win.gBrowser.selectedTab = win.gBrowser.addTab(e.target.parentNode.href);

      e.preventDefault();
    }

    function getNotifAndMP() {
      var toolbarbutton = document.getElementById('zds-notif-button');
      var dropdown = document.querySelector('.dropdown');
      var oReq = new XMLHttpRequest();
      oReq.open("GET", 'http://zestedesavoir.com/', true);
      oReq.onload = function () {
        //Parse la page
        var DOMPars = HTMLParser(this.responseText.replace(/href=\"\//g, 'href="http://zestedesavoir.com/'));
        var isConnected = false;
        for(var i = 0; i < DOMPars.getElementsByClassName('dropdown').length; ++i)
        {
          //Notifications
          if(DOMPars.getElementsByClassName('dropdown')[i].innerHTML.indexOf('Notifications') != -1)
          {
            var htmlNotif = DOMPars.getElementsByClassName('dropdown')[i].innerHTML;
            if(htmlNotif.split("<a").length-2 > 0)
              toolbarbutton.setAttribute('image', 'chrome://zds-notif/skin/images/icone_n_16.png');
            else
              toolbarbutton.setAttribute('image', 'chrome://zds-notif/skin/images/icone_16.png');

            //Convert HTML in XUL
            htmlNotif = htmlNotif.replace(/<span/g, '<html:span');
            htmlNotif = htmlNotif.replace(/<\/span/g, '</html:span');
            htmlNotif = htmlNotif.replace(/<ul/g, '<html:ul');
            htmlNotif = htmlNotif.replace(/<\/ul/g, '</html:ul');
            htmlNotif = htmlNotif.replace(/<li/g, '<html:li');
            htmlNotif = htmlNotif.replace(/<\/li/g, '</html:li');
            htmlNotif = htmlNotif.replace(/<a/g, '<html:a');
            htmlNotif = htmlNotif.replace(/<\/a/g, '</html:a');
            htmlNotif = htmlNotif.replace(/href=\"\//g, 'href="http://zestedesavoir.com/');
            htmlNotif = htmlNotif.replace('<img(.*)>', '');
            
            dropdown.innerHTML = htmlNotif;
            isConnected = true;
          }
          //Messagerie
          if(DOMPars.getElementsByClassName('dropdown')[i].innerHTML.indexOf('Messagerie') != -1)
          {
            var htmlMP = DOMPars.getElementsByClassName('dropdown')[i].innerHTML;
            /*htmlNotif = htmlNotif.replace(/<span/g, '<html:span');
            htmlNotif = htmlNotif.replace(/<\/span/g, '</html:span');
            htmlNotif = htmlNotif.replace(/<ul/g, '<html:ul');
            htmlNotif = htmlNotif.replace(/<\/ul/g, '</html:ul');
            htmlNotif = htmlNotif.replace(/<li/g, '<html:li');
            htmlNotif = htmlNotif.replace(/<\/li/g, '</html:li');
            htmlNotif = htmlNotif.replace(/<a/g, '<html:a');
            htmlNotif = htmlNotif.replace(/<\/a/g, '</html:a');
            htmlNotif = htmlNotif.replace(/href=\"\//g, 'href="http://zestedesavoir.com/');
            htmlNotif = htmlNotif.replace('<img(.*)>', '');
            
            dropdown.innerHTML += htmlMP;
            isConnected = true;*/
          }
        }
        //Si on est déconnecté.
        if(!isConnected)
        {
          dropdown.innerHTML = '<html:a href="http://zestedesavoir.com/membres/connexion/?next=/" class="dropdown-link-all">Connexion</html:a>';
          toolbarbutton.setAttribute('image', 'chrome://zds-notif/skin/images/icone_16_logout.png');
        }

        var Anchors = document.getElementsByTagName("html:a");
	for (var i = 0; i < Anchors.length ; i++)
	  Anchors[i].addEventListener("click", linkClick, false);
      };
      oReq.send(null);
    }
  }
};


//Listener
window.addEventListener("load", ZDSNotif.BrowserOverlay.init, false);
window.addEventListener('click', ZDSNotif.BrowserOverlay.updateUI, false);
