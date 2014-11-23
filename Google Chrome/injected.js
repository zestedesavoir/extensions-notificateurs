/* PLUS NECESSAIRE MAINTENANT QUE L'ON SAIT ARCHIVER !

var url = document.URL;
if((url.indexOf("/membres/") == -1) && (url.indexOf("#badges") == -1)) //si on est pas sur la page pour les badges
    url = url.slice(0,url.indexOf("?")) + "/" + url.slice(url.lastIndexOf("-")+1);
var container = document.getElementById("lastNotifications"); //on restreint juste aux notifications
var els = container.getElementsByTagName("a");
var len = els.length;
var target = false;
for (var i = 0; i < len; i++) {
    var el = els[i];
    if (el.href === url) {
        target = els[i+1];
        break;
    }
}
target && target.click();
*/
/*
// Je suspecte ce bout de code de faire remonter la page tout en haut plutot que rester sur le lien qui va bien
// Update du nombre de notifs
var elem = document.getElementById("notifications");
elem.click();
elem.click();
*/

// KONAMI!
var keys = [38, 38, 40, 40, 37, 39, 37, 39, 66, 65],
    current = 0;

document.addEventListener("keydown", function(e) {
    if(e.which == keys[current]) {
        current++;
    }
    else {
        current = 0;
    }
    
    if(current >= keys.length) {
        setInterval(function() {
            var i = document.createElement("img"),
                h = window.innerHeight,
                w = window.innerWidth;
            i.src = "http://www.siteduzero.com/uploads/fr/ftp/iphone/zozor.png";
            i.style.position = "fixed";
            i.style.top = (Math.floor(Math.random() * (h - 480))) + "px";
            i.style.right = (Math.floor(Math.random() * (w - 320))) + "px";
            i.style.zIndex = 2000;
            document.body.appendChild(i);
            console.log("Konami");
        }, 100);
        current = 0;
    }
}, false);
