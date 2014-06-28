var notificator;

var urlZdS = "http://zestedesavoir.com";

var linkListener = function(notificator, event) {
    var url = $(event.currentTarget).attr("href");
    if(url == "#")
        return;

    event.preventDefault();

    var parent = $(event.currentTarget).closest('div'); //trouve l'id du parent
    var isShortcut = $(parent).hasClass("allNotifs");
    if(!isShortcut) {
        var id = $(parent).attr('id').slice(6);
        var isAlerte = $(parent).hasClass("alerte");
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

        if(notificator.getOptions("autoclosePopup")) {
            window.close();
        }
    });
}

var createNotif = function(notif) {
    var elem = $("<div>", { class: "element", id: "notif-" + notif.id }),
        notifLink = $("<a>").appendTo(elem);
    
    elem.addClass(notif.type);
    notifLink.attr("href", urlZdS + notif["link"]);
        
    $("<img>", { class: "posteur" }).attr("src", notif["answerer"]["avatar"]).appendTo(notifLink);
    $("<span>", { class: "posteur" }).text(notif["answerer"]["username"]).appendTo(notifLink);
    $("<span>", { class: "date" }).text(notif["date"]).appendTo(notifLink);
    $("<span>", { class: "titre" }).text(notif["title"]).appendTo(notifLink);
    
    return elem;
};

var backgroundLoaded = function(bgWindow) {
    if(!bgWindow || !bgWindow.theNotificator) {
        console.error("ZdSNotificator : Failed to load background");
        return;
    }
    notificator = bgWindow.theNotificator;
    var notifs = notificator.getNotification();
    
    if(notificator.logged || notificator.useFakeData) {
        var len = notifs.length;
        var content = $("#content");
        
        var notifList = $("<div>", { class: "notifList" });
        
        if(len == 0) {
            $("<div>", { class: "element other noNotifs" }).append($("<span>").text("Aucune nouvelle notifications")).appendTo(content);
        } else {
            for(var i = 0; i < len; i++) {
                var n = createNotif(notifs[i]).appendTo(notifList);
            }
        }
        
        notifList.appendTo(content);
        
        //ligne "Afficher toute les notifications"
        if(notificator.getOptions("showAllNotifButton")) {
            $("<div>", { class: "element other allNotifs" }).append(
                $("<a>", { href: urlZdS + "/forum/notifications" }).text("Toutes les notifications")
            ).appendTo(content);
        }
        
        //ligne "Ouvrir le ZdS"
        if(notificator.getOptions("ZdSLink")) {
            $("<div>", { class: "element other allNotifs" }).append(
                $("<a>", { href: urlZdS }).text("Aller sur ZdS")
            ).appendTo(content);
        }
        
        var liens = document.getElementsByTagName("a");
        for (var i = 0; i < liens.length; i++) {
            $(liens[i]).on("click", linkListener.bind(this, notificator));
        }
        
        notificator.setNewNotifCallback(function(notif) {
            $(".noNotifs").hide();
            createNotif(notif).appendTo(notifList).find("a").on("click", linkListener.bind(this, notificator));
        });
    }
    else {
        var content = $("#content");
        $("<div>", { class: "not-connected"}).html("Vous n'&ecirc;tes pas connect&eacute;!").appendTo(content);
    }
};

document.addEventListener('DOMContentLoaded', function () {
    chrome.runtime.getBackgroundPage(function() {
        backgroundLoaded.apply(this, arguments);
    });
});
