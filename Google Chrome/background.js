/**
 * OpenClassrooms Notificateur
 * @author Eskimon & Sandhose
 * @licence under MIT Licence
 * @version 2.2.0
 * ======
 * background.js
 * Main background script
 */


/**
 * Notificateur - Main class
 * @constructor
 */
var Notificateur = function() {
	this.init.apply(this, arguments);
};

Notificateur.prototype = {
	/**
	 * ZdS URL
	 */
	url: "http://zestedesavoir.com",

	/**
	 * If logged in last check
	 */
	logged: true,

	/**
	 * Options
	 */
	default_options: {
		updateInterval: 5,
		openListe: true,
		openInNewTab: true,
		showAllNotifButton: true,
		showDesktopNotif: true,
		notifPriority: 0,
		mpPriority: 0,
		playSon: false,
		ZdSLink: false,
		autoclosePopup: true,
		useDetailedNotifs: false,
		archiveAllLink: false
	},

	options: {},

	_optionsTypes: { // Je l'ai quand meme fait ^^
		updateInterval: Number,
		openListe: Boolean,
		openInNewTab: Boolean,
		showAllNotifButton: Boolean,
		showDesktopNotif: Boolean,
		notifPriority: Number,
		mpPriority: Number,
		playSon: Boolean,
		ZdSLink: Boolean,
		autoclosePopup: Boolean,
		useDetailedNotifs: Boolean,
		archiveAllLink: Boolean
	},

	/**
	 * Use fake data for debug
	 */
	useFakeData: false,

	/**
	 * Check en cours
	 */
	checkPending: false,

	/**
	 * chrome.storage
	 */
	storage: chrome.storage.sync,

	/**
	 * Init
	 */
	init: function() {
		this.initialized = false;
		this.notifications = []; //tableau stockant les notifs
		this.alertTabId = [];
		this.MPs = []; //tableau stockant les MPs
		chrome.browserAction.enable(); //sinon un concours de circonstance pourrait nous faire démarrer avec une icone disable
		this.loadOptions(function() {
			//action lorsqu'on click sur le bouton (affichage liste ou chargement ZdS
			if(!this.options.openListe) { //soit on ouvre le ZdS
				chrome.browserAction.setPopup({popup:""});
				chrome.browserAction.onClicked.addListener(this.listeners.toolbarClick.bind(this));
			} else { //sinon on ouvre une popup avec le contenu des notifs
				chrome.browserAction.setPopup({popup:"popup/popup.html"});
			}

			chrome.alarms.create('refresh', {periodInMinutes: parseInt(this.options.updateInterval)});

			this.loadSounds(function() {
				this.loadSoundpack(this.soundpack);
			}.bind(this));

			this.initialized = true;

			this.check();

		}.bind(this));

		this.initListeners();
	},

	/* Add events listeners */
	initListeners: function() {
		chrome.runtime.onInstalled.addListener(this.listeners.install.bind(this));

		if(chrome.tabs) {
			chrome.tabs.onUpdated.addListener(this.listeners.tabUpdate.bind(this));
			chrome.tabs.onCreated.addListener(this.listeners.tabCreate.bind(this));
		}

		if(chrome.alarms) {
			chrome.alarms.onAlarm.addListener(this.listeners.alarm.bind(this));
		}

		if(chrome.notifications) {
			chrome.notifications.onButtonClicked.addListener(this.listeners.notifButtonClick.bind(this));
			chrome.notifications.onClicked.addListener(this.listeners.notifClick.bind(this));
			chrome.notifications.onClosed.addListener(this.listeners.notifClose.bind(this));
		}
	},

	/**
	 * Listeners
	 */
	listeners: {
		/**
		 * Extension install
		 */

		install: function(details) {
			if(details.reason == "install") {
				chrome.tabs.create({
					'url': chrome.runtime.getURL("welcome/welcome.html"),
					'active': true
				});
			}
		},

		/**
		 * Tab Update
		 */
		tabUpdate: function(tabId, changeInfo, tab) {
			if(tab.url !== undefined && tab.url.indexOf("zestedesavoir.com") != -1 && tab.url.indexOf("zestedesavoir.com") < 14 && changeInfo.status == "complete") {
				if(tab.url.indexOf("/forum/sujet/") != -1) {//cas d'une notif de type forum -> il faut faire l'injection
					if(this.alertTabId.indexOf(tabId) == -1) { //ne se déclenche pas si on arrive via une alerte de modo
						//on garde le inject juste pour le plaisir du konami code :D
						chrome.tabs.executeScript(tabId, {
							file: "injected.js"
						});
					} else {
						this.alertTabId.splice(this.alertTabId.indexOf(tabId),1);
					}
					//on attend une seconde pour que le script soit injecté puis on check de nouveau les notifs pour mettre à jour le numero
					this.check();
				} else if(tab.url.indexOf("/mp/") != -1) { //cas des MP
					this.check();
				}
			}
		},

		/**
		 * Tab create
		 */
		tabCreate: function(tab) {
			if(tab.url !== undefined && tab.url.indexOf("zestedesavoir.com") != -1 && tab.url.indexOf("zestedesavoir.com") < 14) {
				this.check();
			}
		},

		/**
		 * Toolbar Click
		 */
		toolbarClick: function() {
			chrome.tabs.create({
					'url': this.url,
					'active': true
				});
		},

		/**
		 * Alarm
		 */
		alarm: function(alarm) {
			if (alarm.name == 'refresh') {
				this.check();
			}
		},

		/**
		 * Notification button click
		 */
		notifButtonClick: function(notifId, button) {
			var notif = this.getNotification(notifId);
			if(button == 0) { // Open last message
				if(notif) {
					switch(notif.type) {
						case("forum"): //normal
							this.openZdS(notif.link);
							this.archiveNotification(notif.id);
							break;
						case("mp"): //MP
							this.openZdS(notif.link);
							break;
						case("alerte"): //alerte
							this.openZdS(notif.link);
							break;
					}
				}

				chrome.notifications.clear(notifId, function() {

				});
			}
			else if(button == 1) { // Open thread
				if(notif) {
					this.openZdS("/forum/sujet/" + notif.id);
					this.archiveNotification(notif.id);
				}

				chrome.notifications.clear(notifId, function() {

				});
			}
		},

		/**
		 * Notif Click
		 */
		notifClick: function(notifId) {
			var notif = this.getNotification(notifId);
			if(notif) {
				switch(notif.type) {
					case("forum"): //normal
						this.openZdS(notif.link);
						this.archiveNotification(notif.id);
						break;
					case("mp"): //MP
						this.openZdS(notif.link);
						break;
					case("alerte"): //alerte
						this.openZdS(notif.link, true);
						break;
				}
			}

			chrome.notifications.clear(notifId, function() {

			});
		},

		/**
		 * Notif close
		 */
		notifClose: function(notifId) {
			// A la fermeture de la notif
		}
	},

	/**
	 * Check for new Notifications
	 */
	check: function() {
		var self = this;
		if(this.checkPending || !this.initialized) { // Si l'extension n'est pas (encore) initialisee ou un check est deja en cours
			return;
		}

		this.checkPending = true;
		chrome.browserAction.setIcon({"path":"icons/icone_38_parsing.png"});

		var url = this.useFakeData ? chrome.runtime.getURL("fake-data.html") : this.url;
		$.get(url, this.loadCallback.bind(this), "text").error(function() {
			//si jamais la requete plante (pas d'internet, 404 ou autre 500...)
			chrome.browserAction.setBadgeText({text: "err"});
			chrome.browserAction.setIcon({"path":"icons/icone_38_logout.png"});
			chrome.browserAction.disable();
			self.logged = false;
			self.checkPending = false;
		});
	},

	/**
	 * Get notifications by id
	 * @param {String} [id] Notification ID
	 * @returns {Array|Object} A notifications if ID is set, or an array of all Notifications
	 */
	getNotification: function(id) {
		if(id) {
			for(var i = 0; i < this.notifications.length; i++) {
				if(this.notifications[i].id == id) {
					return this.notifications[i];
				}
			}
			return false;
		}
		else {
			return this.notifications;
		}
	},

	/**
	 * Callback on page load
	 * @param {String} data Page data
	 */
	loadCallback: function(data) {
		var contenu = $("div.logbox", $(data));

		var hasNewNotif = {
			notification: false,
			mp: false
		};
		console.log("loadCallback >>")
		//on est pas connecté !
		if(contenu.hasClass('unlogged') && !this.useFakeData) {
			if(this.logged) {
				chrome.browserAction.setBadgeText({text: "log"});
				chrome.browserAction.setIcon({"path":"icons/icone_38_logout.png"});
				this.logged = false;
			}
			return;
		} else {
			if(!this.logged) {
				chrome.browserAction.setIcon({"path":"icons/icone_38.png"});
				chrome.browserAction.setBadgeText({text: ""});
				this.logged = true;
			}
		}
		
		//récupere les deux listes, celle des MP (0) et celle des notifs (1)
		contenu = contenu.find("div.notifs-links ul.dropdown-list");
		
		// Check les notifications ---------------------------------------
		var notifications = $("li", $(contenu[1])),
			newNotifs = [], // Liste des nouvelles notifications
			oldNotifs = this.notifications, // Ancienne liste
			removedNotifs = [], // Notifs enlevées
			notifsList = []; // Nouvelle liste

		if(!notifications.hasClass('dropdown-empty-message')) {
			for(var i = 0; i < notifications.length; i++) {

				var notif = $(notifications[i]),
					notifLink = notif.find('a').attr('href');

				var notifObj = {
					id: notifLink.split('/')[3],
					link: notifLink,
					title: notif.find("span.topic").text(),
					date: notif.find("span.date").text(),
					answerer: {
						avatar: notif.find("img.avatar").attr('src'),
						username: notif.find("span.username").text()
					},
					type: "forum"
				};

				var existingNotif = this.getNotification(notifObj.subjectId);
				if(existingNotif) {
					$.extend(notifObj, existingNotif);
				}
				else {
					newNotifs.push(notifObj);
					this.newNotifCallback && this.newNotifCallback(notifObj);
					hasNewNotif.notification = true;
				}

				if(this.options.useDetailedNotifs && !notifObj.detailed) {
					this.fetchNotificationDetails(notifObj, function(newNotif) {
						var notifTmp = $.extend({}, notifObj, newNotif);
						self.showDesktopNotif(notifTmp);
					});
				}
				else if(!existingNotif) {
					this.showDesktopNotif(notifObj);
				}

				notifsList.push(notifObj);
			}
		}


		// Check les mp -----------------------------------------------
		notifications = $("li", $(contenu[0]));
		
		if(!notifications.hasClass('dropdown-empty-message')) {
			for(var i = 0; i < notifications.length; i++) { //-1 pour éviter le "tout les mps
								
				var notif = $(notifications[i]),
					notifLink = notif.find('a').attr('href');

				var notifObj = {
					id: notifLink.split('/')[2],
					link: notifLink,
					title: notif.find("span.topic").text(),
					date: notif.find("span.date").text(),
					answerer: {
						avatar: notif.find("img.avatar").attr('src'),
						username: notif.find("span.username").text()
					},
					type: "mp"
				};

				var existingNotif = this.getNotification(notifObj.id);
				if(existingNotif) {
					$.extend(notifObj, existingNotif);
				}
				else {
					newNotifs.push(notifObj);
					this.newNotifCallback && this.newNotifCallback(notifObj);
					hasNewNotif.mp = true;
				}

				if(!existingNotif)
					this.showDesktopNotif(notifObj);

				notifsList.push(notifObj);
			}
		}
/*
		// Check les notifications "alertes" des modos
		notifications = $data.find(".last-active-item .dropdown-menu .dropdown-menu-item");

		for(var i = 0; i < notifications.length-1; i++) { //-1 our pas avoir le lien "toutes les alertes"
			var notif = $(notifications[i]),
				notifLink = notif.find("a").attr('href'),
				archiveLink = notifLink; //pas de lien d'archives pour les alertes

			if(notifLink == "/mp/") continue;

			var notifObj = {
				id: "alerte-"+notifLink.substr(notifLink.lastIndexOf("/") + 1),
				title: notif.find("li.title").text(),
				date: notif.find("li.date").text(),
				messageId: notifLink.substr(notifLink.lastIndexOf("/") + 1),
				thread: notifLink.substr(13, notifLink.lastIndexOf("/") - 13),
				type: "alerte"
			};

			var existingNotif = this.getNotification(notifObj.id);
			if(existingNotif) {
				$.extend(notifObj, existingNotif);
			}
			else {
				newNotifs.push(notifObj);
				this.newNotifCallback && this.newNotifCallback(notifObj);
				hasNewNotif.notification = true;
			}

			if(!existingNotif)
				this.showDesktopNotif(notifObj);

			notifsList.push(notifObj);
		}
*/

		this.notifications = notifsList;

		for(var i = 0; i < oldNotifs.length; i++) { // Faire la liste des notifs enlevées
			var exists = false;
			for(var j = 0; j < this.notifications.length; j++) {
				if(oldNotifs[i].id == this.notifications[j].id) {
					exists = true;
					break;
				}
			}
			if(!exists) {
				removedNotifs.push(oldNotifs[i]);
				this.removeNotifCallback && this.removeNotifCallback(oldNotifs[i]);
			}
		}

		// Play sounds
		if(this.options.playSon) {
			this.playSound(hasNewNotif);
		}

		this.clearDesktopNotifs(removedNotifs);

		//set le texte du badge
		this.updateBadge();

		chrome.browserAction.setIcon({"path":"icons/icone_38.png"});

		this.checkPending = false;
	},

	/**
	 * Show a desktop notification
	 * @param {Objecy|Array} notif A Notification or an Array of Notifications
	 */
	showDesktopNotif: function(notif) {
		if(typeof notif == "Array") {
			for(var i = 0; i < notif.length; i++) {
				this.showDesktopNotif(notif[i]);
			}
			return;
		}

		if(chrome.notifications && this.options.showDesktopNotif) {
			if(this.options.useDetailedNotifs && notif.detailed && (notif.type == "forum")) {
				var notifOptions = {};

				chrome.notifications.create(notif.id, {
					type: "basic",
					iconUrl: notif.avatarUrl,
					title: notif.answerer['username'] + " - " + notif.title,
					message: notif.postContent.replace(/<br \/>/ig, "\n").replace(/(<([^>]+)>)/ig, "").substr(0, 140) + "...\n" + notif.date,
					priority: parseInt(this.options.notifPriority),
					buttons: [{ title: "Voir le message" }, { title: "Voir le début du thread"}]
				}, function() {});
			}
			else {
				var boutons = new Array();
				var priority = 0;
				var icone = "";
				switch(notif.type) {
					case("forum"):
						boutons[0] = { title: "Voir le message" };
						boutons[1] = { title: "Voir le début du thread" };
						icone = "icons/big_message.png";
						priority = this.options.notifPriority;
						break;
					case("mp"):
						boutons[0] = { title: "Voir le MP" };
						icone = "icons/big_mp.png";
						priority = this.options.mpPriority;
						break;
					case("alerte"):
						boutons[0] = { title: "Voir l'alerte" };
						icone = "icons/big_alerte.png";
						priority = this.options.notifPriority;
						break;
				}
				var notifOptions = { // Options des notifications
					type: "basic",
					iconUrl: icone,
					title: notif.title,
					message: notif.date,
					buttons: boutons,
					priority: parseInt(priority)
				};

				chrome.notifications.create(notif.id, notifOptions, function() {});
			}
		}
	},

	/**
	 * Clear desktops notifications
	 * @param {Array} notifs Array of notifications to clear
	 */
	clearDesktopNotifs: function(notifs) {
		if(chrome.notifications) {
			for(var i = 0; i < notifs.length; i++) {
				chrome.notifications.clear(notifs[i].id, function() {});
			}
		}
	},

	/**
	 * Fetch notifications details (forum posts only)
	 * @param {Object|Array} notif A notifications or an Array of notifications
	 * @param {Function} callback The callback when the details are fetched
	 */
	// TODO
	fetchNotificationDetails: function(notif, callback) {
		if(Object.prototype.toString.call(notif) == "[object Array]") {
			for(var i = 0; i < notif.length; i++) {
				this.fetchNotificationDetails(notif[i], function(newNotif) {
					callback && callback(newNotif, i);
				});
			}

			return;
		}

		$.get(this.url + notif.link, function(_data) {
			var data = _data.replace(/src=/ig, "data-src=").replace(/href=/ig, "data-href="); // <-- pas forcement la meilleur methode... marche pas si qqn a un nom/avatar avec src=/href= dans le nom
			//data = data.replace(/<img\b[^>]*>(.*?)<\/img>/ig,'');
			//data = data.replace(/<head\b[^>]*>(.*?)<\/head>/ig,'');
			//var xmlDoc = new DOMParser().parseFromString(data, "text/xml"); <-- Le parser bug...
			var $data = $(data);
			var post = $data.find("#message-" + notif.messageId).parent();
			if(post.length == 1) {
				var authorElem = post.find(".avatar .author a");
				var author = $.trim(authorElem.text());
				var authorUrl = authorElem.attr("data-href"); // URL de l'auteur sans le /membres/
				var avatarUrl = post.find(".avatar img[alt=\"avatar\"]").attr("data-src");
				var postContent = post.find(".content .markdown").text();
				var threadTitle = $(data.match(/<title>[\n\r\s]*(.*)[\n\r\s]*<\/title>/gmi)[0]).text() // <-- Un peu hard, je sais ^^
				notif.author = author;
				notif.avatarUrl = avatarUrl;
				notif.authorUrl = authorUrl;
				notif.postContent = postContent;
				notif.threadTitle = threadTitle;
				notif.detailed = true;

				if(!notif.avatarUrl.indexOf("http") == 0) { // <-- URL relative
					notif.avatarUrl = "http://zestedesavoir.com" + notif.avatarUrl;
				}
			}
			else {
				notif.detailed = false;
			}

			callback && callback(notif);
		}, "text");
	},

	/**
	 * Get options
	 * @param {String} [key] Option key
	 * @returns {Object} The options value or all options
	 */
	getOptions: function(key) {
		if(key) {
			return this.options[key];
		}
		else {
			return this.options;
		}
	},

	/**
	 * Set options
	 * @param {Object} changes Object of options changes
	 * @param {Function} [callback]
	 */
	setOptions: function(changes, callback) {
		callback = callback || function() { console.log("Options saved"); };

		var oldOptions = this.options;
		for(var key in changes) {
			if(this.options.hasOwnProperty(key)) {
				// Conversion des donnees dans le bon type
				if(this._optionsTypes[key] == Number) {
					changes[key] = parseInt(changes[key]);
				}
				else if(this._optionsTypes[key] == Boolean) {
					changes[key] = !!changes[key];
				}
				else if(this._optionsTypes[key] == String) {
					changes[key] = changes[key].toString();
				}

				this.options[key] = changes[key];
			}
		}

		changes.timestamp = Date.now();

		console.log("Options changes from", oldOptions, "to", this.options);
		this.storage.set(changes, callback);
		this.updateOptions();
	},

	/**
	 * Reset options
	 */
	resetOptions: function() {
		var defaults = this.default_options;
		delete defaults["lastEdit"];
		this.setOptions(defaults);
	},

	/**
	 * Load options from chrome.storage
	 * @param {Function} [callback] Callback when options are loaded
	 */
	loadOptions: function(callback) { // Charge les options depuis le chrome.storage
		this.options = $.extend(this.options, this.default_options);
		var keys = Object.keys(this.options);
		this.storage.get(keys, function(items) {
			for(var key in items) {
				if(this.options.hasOwnProperty(key)) {
					this.options[key] = items[key];
				}
			}

			(typeof callback == "function") && callback();
		}.bind(this));
	},

	/**
	 * Update options
	 */
	updateOptions: function() {
		// Update interval
		chrome.alarms.create('refresh', { periodInMinutes: parseInt(this.options.updateInterval) });

		//action lorsqu'on click sur le bouton (affichage liste ou chargement ZdS
		if(!this.options.openListe) { //soit on ouvre le ZdS
			chrome.browserAction.setPopup({popup:""});
			chrome.browserAction.onClicked.addListener(this.listeners.toolbarClick.bind(this));
		} else { //sinon on ouvre une popup avec le contenu des notifs
			chrome.browserAction.onClicked.removeListener(this.listeners.toolbarClick);
			chrome.browserAction.setPopup({popup:"popup/popup.html"});
		}
	},

	/**
	 * Load soundpacks
	 * @param {Function} [callback] Callback fired when soundpacks are loaded
	 */
	loadSounds: function(callback) {
		$.getJSON(chrome.extension.getURL("/sounds/packs.json"), function(data) {
			this.soundpacks = data;
			if(this.soundpacks.sounds[this.options.soundpack]) {
				this.soundpack = this.soundpacks.sounds[this.options.soundpack];
			}
			else {
				this.soundpack = this.soundpacks.sounds[this.soundpacks.default_soundpack];
			}

			callback && callback();
		}.bind(this));
	},

	/**
	 * Load a specific soundpack
	 * @param {Object} soundpack The soundpack to load
	 */
	loadSoundpack: function(soundpack) {
		var soundsList = document.getElementById("sound_list") || document.createElement("div");
		soundsList.id = "sound_list";
		document.body.appendChild(soundsList);
		soundsList.innerHTML = "";

		["notif_mp_new", "notif_new", "mp_new"].forEach(function(element) {
			//debugger;
			var exists = document.getElementById("audio_" + element) !== null,
				sound = exists ? document.getElementById("audio_" + element) : new Audio();
			sound.src = chrome.extension.getURL("/sounds/" + soundpack.folder + "/" + soundpack.sounds[element]);
			sound.id = "audio_" + element;
			sound.autoplay = false;
			sound.load();
			!exists && soundsList.appendChild(sound);
		}, this);
	},

	/**
	 * Play sound
	 * @param {Object} options Which sound should be played?
	 */
	playSound: function(options) {
		var sound;
		if(options.notification && options.mp) {
			sound = document.getElementById("audio_notif_mp_new");
		}
		else if(options.notification) {
			sound = document.getElementById("audio_notif_new");
		}
		else if(options.mp) {
			sound = document.getElementById("audio_mp_new");
		}
		else {
			return;
		}

		if(sound) {
			sound.pause();
			sound.currentTime = 0;
			sound.play();
		}
	},

	/**
	 * Set new notif callback
	 * @param {Function} callback
	 */
	setNewNotifCallback: function(callback) {
		if(typeof callback == "function") {
			this.newNotifCallback = callback;
		}
		else {
			this.newNotifCallback = undefined;
		}
	},

	/**
	 * Set remove notif callback
	 * @param {Function} callback
	 */
	setRemoveNotifCallback: function(callback) {
		if(typeof callback == "function") {
			this.removeNotifCallback = callback;
		}
		else {
			this.removeNotifCallback = undefined;
		}
	},
	
	/**
	 * Update badge
	 */
	updateBadge: function() {
		var notifs = this.notifications,
			len = notifs.length,
			totMP = 0;
		for(var i=0; i<len; i++)
			if(notifs[i].type == "mp")
				totMP++;
				
		var badgeTexte = (totMP > 0) ? totMP.toString() + " - " : "";
		badgeTexte += (len-totMP > 0) ? (len-totMP).toString() : ((totMP > 0) ? "0" : "");
		chrome.browserAction.setBadgeText({text: badgeTexte});
	},

	/**
	 * Open a ZdS page
	 * @param {String} _url The url to open
	 * @param {Boolean} remember
	 */
	openZdS: function(_url, remember) {
		var url = this.url + _url,
			self = this;

		chrome.windows.getCurrent({ populate:true }, function(currentWindow) {
			var tab = false;
			for(var i in currentWindow.tabs) {
				if(currentWindow.tabs[i].active) {
					tab = currentWindow.tabs[i];
					break;
				}
			}

			if(!self.getOptions("openInNewTab") && tab && tab.url !== undefined && tab.url.indexOf("zestedesavoir.com") != -1 && tab.url.indexOf("zestedesavoir.com") < 14) {
				if(remember) {
					self.alertTabId.push(tab.id); //on ajout l'id du tab
				}
				chrome.tabs.update(tab.id, { url: url });
			}
			else {
				chrome.tabs.create({
					'url': url,
					'active': false
				}, function(tab){
					if(remember) {
						self.alertTabId.push(tab.id); //on ajout l'id du tab
					}
				});
			}
		});
	}
};


/** @global */
window.theNotificator = new Notificateur();
