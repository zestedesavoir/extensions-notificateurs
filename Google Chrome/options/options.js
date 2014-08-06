/**
 * Notificator Options
 * @namespace
 */

var NotificatorOptions = {
	/**
	 * Init options
	 */
	init: function(_notificator) {
		this.notificator = _notificator;
		
		this.elems = {
			updateInterval: document.getElementById('interval'),
			openListe: document.getElementById('openListe'),
			openInNewTab: document.getElementById("newTab"),
			showAllNotifButton: document.getElementById("allNotifs"),
			showDesktopNotif: document.getElementById("notifNative"),
			notifPriority: document.getElementById("priorityNotif"),
			mpPriority: document.getElementById("priorityMP"),
			playSon: document.getElementById("playSon"),
			tweet: document.getElementById("tweet"),
			// ZdSLink: document.getElementById("ZdSLink"),
			autoclosePopup: document.getElementById("autoclosePopup"),
			//useDetailedNotifs: document.getElementById("detailedNotifs"),
			//archiveAllLink: document.getElementById("archiveAllLink")
		};
		
		document.getElementById("enregistrer").addEventListener("click", this.save.bind(this));
		document.getElementById("reset").addEventListener("click", this.reset.bind(this));
		this.elems["updateInterval"].addEventListener("keyup", this.checkInput.bind(this));
		this.elems["openListe"].addEventListener("click", this.toggle.bind(this));
		this.elems["showDesktopNotif"].addEventListener("click", this.toggle.bind(this));
		
		this.load();
	},
	
	oldValue: 5,
	
	/**
	 * Check inputs
	 * @param {Object} [evt] JS Event
	 */
	checkInput: function(evt) {
		var val = this.elems["updateInterval"].value;
		if(val == '')
			this.elems["updateInterval"].value = this.oldValue;
		else {
			if(parseInt(val) > 60) {
				this.elems["updateInterval"].value = 60;
				this.oldValue = 60;
			} else {
				this.oldValue = val;
			}
		}
	},

	/**
	 * Reset options
	 */
	reset: function() {
		var c = confirm("Voulez vous réellement remettre à zéro les options ?");
		if(c) {
			this.notificator.resetOptions();
			window.location.reload();
		}
	},
	
	/**
	 * Save options
	 */
	save: function() {
		this.notificator.setOptions(this.getValues(), function() {
			window.close();
		});
	},
	
	/**
	 * Load options
	 */
	load: function() {
		this.options = this.notificator.getOptions();
		for(var key in this.elems) {
			if(this.options[key] !== undefined) {
				if(this.elems[key].type == "checkbox") {
					this.elems[key].checked = this.options[key];
				}
				else {
					this.elems[key].value = this.options[key];
				}
			}
		}
		this.toggle();
		this.oldValue = this.elems['updateInterval'].value;
		this.checkAvatar();
	},
	
	/**
	 * Get inputs value
	 * @returns {Object} The input values
	 */
	getValues: function() {
		var obj = {};
		for(var key in this.elems) {
			var val = this.elems[key].type == "checkbox" ? this.elems[key].checked : parseInt(this.elems[key].value);
			obj[key] = val;
		}
		return obj;
	},
	
	/**
	 * Toggle inputs
	 */
	toggle: function() {
		if(this.elems['showDesktopNotif'].checked) {
			Array.prototype.forEach.call(document.querySelectorAll(".subNotifFields"), function(elem) {
				elem.classList.remove("disabled");

				Array.prototype.forEach.call(elem.querySelectorAll("input"), function(input) {
					input.removeAttribute("disabled");
				});
			});
		}
		else {
			Array.prototype.forEach.call(document.querySelectorAll(".subNotifFields"), function(elem) {
				elem.classList.add("disabled");

				Array.prototype.forEach.call(elem.querySelectorAll("input"), function(input) {
					input.setAttribute("disabled", true);
				});
			});
		}
		
		if(this.elems['openListe'].checked) {
			Array.prototype.forEach.call(document.querySelectorAll(".subPopupFields"), function(elem) {
				elem.classList.remove("disabled");

				Array.prototype.forEach.call(elem.querySelectorAll("input"), function(input) {
					input.removeAttribute("disabled");
				});
			});
		}
		else {
			Array.prototype.forEach.call(document.querySelectorAll(".subPopupFields"), function(elem) {
				elem.classList.add("disabled");

				Array.prototype.forEach.call(elem.querySelectorAll("input"), function(input) {
					input.setAttribute("disabled", true);
				});
			});
		}
		
		Array.prototype.forEach.call(document.querySelectorAll(".priority input"), function(input) {
			input.addEventListener("input", onPriorityInputChange, false);
			onPriorityInputChange.call(input, null);
		});
		// $(".priority input").on("change", ).trigger("change");
		function onPriorityInputChange(event) {
			var value = "Erreur";

			switch(parseInt(this.value)) {
				case -2:
					value = "Pas important";
					break;
				case -1:
					value = "Peu important";
					break;
				case 0:
					value = "Normale";
					break;
				case 1:
					value = "Important";
					break;
				case 2:
					value = "Très important";
					break;
			}
			this.parentNode.querySelector(".value").textContent = value;
		}
	},
	
	/**
	 * Check avatar
	 */
	checkAvatar: function() {
		new AjaxRequest("http://zestedesavoir.com", this.loadCallback.bind(this), "text");
	},
	
	/**
	 * Callback when page loaded
	 */
	loadCallback: function(event) {
		var doc = document.implementation.createHTMLDocument("xhr_result");
		doc.documentElement.innerHTML = event.target.responseText.trim();

		var leDiv = document.getElementById("connecteComme");
		//on est pas connecté !
		if(!this.notificator.logged) {
			leDiv.querySelector("a").setAttribute("href", "http://zestedesavoir.com/membres/connexion/");
			leDiv.querySelector("strong").textContent = "Non connecté !";		  
		} else {
			var link = doc.getElementById("my-account");
			var profileLink = link.getAttribute("href");
			var profileName = link.querySelector("span.username").textContent;
			var avatarImgSrc = link.querySelector("img").getAttribute("src").replace(/^\/\/(.*)/, "http://$1");
			leDiv.querySelector("a").setAttribute("href", "http://zestedesavoir.com" + profileLink/* + profileName*/);

			leDiv.querySelector("strong").textContent = profileName;
			leDiv.querySelector("img").setAttribute("src", avatarImgSrc);
		}
	}
};

document.addEventListener('DOMContentLoaded', function () {
	chrome.runtime.getBackgroundPage(function(bgWindow) {
		var notificator = bgWindow.theNotificator;
		if(notificator) {
			NotificatorOptions.init.call(NotificatorOptions, notificator);
		} else {
			console.log("Can't fetch the background :(");
		}
	});
});
