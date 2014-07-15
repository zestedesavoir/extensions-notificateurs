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
        var c = confirm("Voulez vous r\u00E9ellement remettre \u00E0 z\u00E9ro les options ?");
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
            $(".subNotifFields").removeClass("disabled");
            $(".subNotifFields input").attr("disabled", false);
        }
        else {
            $(".subNotifFields").addClass("disabled");
            $(".subNotifFields input").attr("disabled", true);
        }
        
        if(this.elems['openListe'].checked) {
            $(".subPopupFields").removeClass("disabled");
            $(".subPopupFields input").attr("disabled", false);
        }
        else {
            $(".subPopupFields").addClass("disabled");
            $(".subPopupFields input").attr("disabled", true);
        }
        
        $(".priority input").on("change", function(e) {
            var value = "Erreur";
            switch(parseInt(e.target.value)) {
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
            $(e.currentTarget).parent().find(".value").text(value);
        }).trigger("change");
    },
    
    /**
     * Check avatar
     */
    checkAvatar: function() {
        $.get("http://zestedesavoir.com", this.loadCallback.bind(this), "text");
    },
    
    /**
     * Callback when page loaded
     */
    loadCallback: function(data) {            
        var leDiv = $("div#connecteComme");
        //on est pas connecté !
        if(!this.notificator.logged) {
            leDiv.find("a").attr("href","http://zestedesavoir.com/membres/connexion/");
            leDiv.find("strong").text("Non connecté !");          
        } else {
            var link = $("a#my-account", $(data));
            var profileLink = link.attr("href");
            var profileName = link.find("span.username").text();
            var avatarImgSrc = link.find("img").attr('src');
            leDiv.find("a").attr("href","http://zestedesavoir.com" + profileLink + profileName);
            leDiv.find("strong").text(profileName);
            leDiv.find("img").attr("src",avatarImgSrc);
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
