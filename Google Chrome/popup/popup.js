var notificator;

var urlZdS = "http://zestedesavoir.com";

var linkListener = function(notificator, event) {
	var url = this.href;
	if(url === "#")
		return;

	event.preventDefault();

	var isShortcut = this.classList.contains("allNotifs");
	if(!isShortcut) {
		var id = this.dataset.notificationId;
		var isAlerte = this.classList.contains("alerte");
	}
	
	//console.log(url);
	chrome.windows.getCurrent({ populate: true }, function(currentWindow) {
		var tab = false;
		for(var i in currentWindow.tabs) {
			if(currentWindow.tabs[i].active) {
				tab = currentWindow.tabs[i];
				break;
			}
		}
		if(!notificator.getOptions("openInNewTab") && tab && tab.url !== undefined && tab.url.indexOf("zestedesavoir.com") != -1 && tab.url.indexOf("zestedesavoir.com") < 14) {
			if(isAlerte) {
				notificator.alertTabId.push(tab.id);
			}
			chrome.tabs.update(tab.id, { url: url });
		}
		else {
			chrome.tabs.create({
				'url': url,
				'active': false
			}, function(tab){
				if(isAlerte) {
					notificator.alertTabId.push(tab.id); //on ajout l'id du tab
				}
			});
		}

		notificator.check();
		
		if(notificator.getOptions("autoclosePopup")) {
			window.close();
		}
	});
}

var createNotif = function(notif) {
	var notif_link = document.createElement("a");
	
	notif_link.href = urlZdS + notif.link;

	notif_link.className = "notification " + notif.type;

	notif_link.id = "notif-" + notif.id;
	notif_link.dataset.notificationId = notif.id;


	// if(!notif.answerer.avatar.match(/^(http:\/\/|https:\/\/|\/\/)(.*)/i)) {
	// 	notif.answerer.avatar = urlZdS + notif.answerer.avatar;
	// }

	var notif_answerer = document.createElement("div");
	notif_answerer.className = "user";

	var notif_avatar = document.createElement("img");
	notif_avatar.src = notif.answerer.avatar;
	notif_avatar.className = "user-avatar";
	notif_avatar.alt = "Avatar de " + notif.answerer.username;

	notif_answerer.appendChild(notif_avatar);

	notif_link.appendChild(notif_answerer);



	var notif_details = document.createElement("div");
	notif_details.className = "details";

	var notif_pseudo = document.createElement("span");
	notif_pseudo.className = "user-name";
	notif_pseudo.textContent = notif.answerer.username;

	notif_details.appendChild(notif_pseudo);

	var notif_date = document.createElement("time");
	notif_date.className = "date";
	notif_date.textContent = notif.date;

	notif_details.appendChild(notif_date);


	var notif_title = document.createElement("span");
	notif_title.className = "title";
	notif_title.textContent = notif.title;

	notif_details.appendChild(notif_title);

	notif_link.appendChild(notif_details);
	
	return notif_link;
};

var backgroundLoaded = function(bgWindow) {
	console.dir(bgWindow);
	if(!bgWindow || !bgWindow.theNotificator) {
		console.error("ZdSNotificator : Failed to load background");
		return;
	}
	notificator = bgWindow.theNotificator;
	var notifs = notificator.getNotification();

	// var notifs = fakeTestData.notifs;

	console.dir(notifs);

	var logo = document.getElementById("logo");
	logo.href = urlZdS;

	var content = document.getElementById("content");

	if(notificator.logged || notificator.useFakeData) {
		var len = notifs.length;
		
		var notifList = document.createElement("div");
		notifList.classList.add("notifList");
		
		if(len === 0) {
			var no_notifs_elem = document.createElement("div");
			no_notifs_elem.classList.add("element", "other", "noNotifs");

			var no_notifs_text = document.createElement("span");
			no_notifs_text.textContent = "Aucune nouvelle notification";

			no_notifs_elem.appendChild(no_notifs_text);
			content.appendChild(no_notifs_elem);
		} else {
			for(var i = 0; i < len; i++) {
				var n = createNotif(notifs[i]);
				notifList.appendChild(n);
			}

			content.appendChild(notifList);
		}
		
		//ligne "Afficher toute les notifications"
		if(notificator.getOptions("showAllNotifButton")) {
			var all_notifs_link = document.createElement("a");
			all_notifs_link.classList.add("element", "other", "allNotifs");
			all_notifs_link.href = urlZdS + "/forums/notifications";
			all_notifs_link.textContent = "Toutes les notifications";

			content.appendChild(all_notifs_link);
		}
		
		var liens = document.getElementsByTagName("a");
		for (var i = 0; i < liens.length; i++) {
			liens[i].addEventListener("click", linkListener.bind(liens[i], notificator), false);
		}
		
		notificator.setNewNotifCallback(function(notif) {
			var no_notifs_elem = document.getElementsByClassName("noNotifs");
			if (no_notifs_elem) {
				no_notifs_elem[0].parentNode.removeChild(no_notifs_elem[0]);
			}

			var n = createNotif(notif);

			var liens = n.getElementsByTagName("a");

			for (var i = 0; i < liens.length; i++) {
				liens[i].addEventListener("click", linkListener.bind(liens[i], notificator), false);
			}

			notifList.appendChild(n);
		});
	}
	else {
		logo.addEventListener("click", linkListener.bind(logo, notificator), false);

		var not_logged_in_elem = document.createElement("div");
		not_logged_in_elem.classList.add("element", "other", "notConnected");
		not_logged_in_elem.textContent = "Vous n'êtes pas connecté !";

		content.appendChild(not_logged_in_elem);
	}
};

// document.addEventListener('DOMContentLoaded', function () {
	chrome.runtime.getBackgroundPage(function() {
		backgroundLoaded.apply(this, arguments);
	});
// });
