import { BASE_URL } from '../../config.js'
import { relativeDate } from '../../date-format.js'

import { ZdsElement } from './_element.js'

/**
 * WebComponent permettant d'afficher la popup de notifications
 * @property {string} userState - L'état de l'utilisateur
 * @property {ZdsNotification[]} notifications - Les notifications non lues
*/
export class Popup extends ZdsElement {
	get styles () {
		return `
:host {
	background-color: var(--color-bg);
}
		`
	}

	get template() {
		return this.userState === 'LOGGED_OUT'
			? `<zds-logo></zds-logo>
<div class="alert">
	<h1 class="title">Oh-oh !</h1>
	<div class="icon"><img src="../../images/clemoji-asleep.svg" alt=""></div>
	<div class="message">On dirait que vous êtes déconnecté·e</div>
</div>
<a id="signin-button" href="${BASE_URL}/membres/connexion/" target="_blank" rel="noopener">Connexion</a>`
			: `<zds-logo></zds-logo>
${this.notifications.length
	? `<ul id="notifications-list">${this.notifications.map(notif => `
		<li>
			<a href="${BASE_URL}${notif.url}">
				<header>
					${notif.sender.avatar_url ? `<img src="${notif.sender.avatar_url}" alt="" class="avatar">`: ``}
					<span class="username">${notif.sender.username}</span>
					<span class="date">${relativeDate(notif.pubdate)}</span>
				</header>
				<span class="topic">${notif.title}</span>
			</a>
		</li>`).join('')}
	</ul>`
	: `<div class="alert">
		<h1 class="title">Bravo !</h1>
		<div class="icon"><img src="../../images/clemoji-party.svg" alt=""></div>
		<div class="message">Aucune notification non lue</div>
	</div>`
}
<div id="bottom-buttons">
	<a id="all-notifications-button" href="${BASE_URL}/notifications/" title="Voir la liste des notifications" target="_blank" rel="noopener">
		<span>Toutes les notifications</span>
	</a>
	<a id="new-message-button" href="${BASE_URL}/mp/creer/" title="Composer un nouveau message privé" target="_blank" rel="noopener">
		<span class="sr-only">Nouveau message privé</span>
	</a>
</div>`
	}

	constructor() {
		super()
		this.userState = 'LOGGED_OUT'
		this.notifications = []

		browser.runtime.sendMessage({ state: 'LOGGED_IN' })

		browser.storage.local.get({
			userState: 'LOGGED_OUT',
			notifications: []
		}).then((data) => {
			this.userState = data.userState
			this.notifications = data.notifications

			this.render()
		})

		browser.storage.onChanged.addListener((changes, areaName) => {
			if (areaName !== 'local') {
				return
			}
			if (changes.userState) {
				this.userState = changes.userState.newValue
			}
			if (changes.notifications) {
				this.notifications = changes.notifications.newValue
			}

			this.render()
		})
	}
	
}
window.customElements.define('zds-popup', Popup)
