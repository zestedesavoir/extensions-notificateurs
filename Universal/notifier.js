import { BASE_URL, BADGE_COLOR_DEFAULT, BADGE_COLOR_ERROR, BADGE_COLOR_OK, POLLING_RATE } from './config.js'

/**
 * Classe tournant en fond permettant de gérer l'icône et le badge ainsi que de mettre à jour les notifications
 * @property {string} userState - L'état de l'utilisateur
 * @property {ZdsNotification[]} notifications - Les notifications non lues
*/
class Notifier {
	constructor() {
		this.userState = 'LOGGED_OUT'
		this.notifications = []
		this.pollingTimer = null

		browser.runtime.onMessage.addListener((msg) => {
			if (msg.state !== undefined) {
				return this.updateState(msg.state)
			}
		})
	}

	/**
	 * Met à jour l'état de connexion
	 * @param {number} state - État à afficher pour le badge
	 * @param {boolean} stopApiUpdate - Empêche de relancer un appel vers l'API
	 */
	updateState(state, stopApiUpdate) {
		if (state !== this.userState) { // If any difference update the state
			switch(state) {
				case 'ERROR':
					chrome.browserAction.setIcon({path: 'images/clemoji-dizzy.svg'})
					break
				case 'LOGGED_OUT':
					chrome.browserAction.setIcon({path: 'images/clemoji-asleep.svg'})
					break
				case 'LOGGED_IN':
					chrome.browserAction.setIcon({path: 'images/clemoji-smile.svg'})
					break
				case 'PENDING_NOTIFICATIONS':
					chrome.browserAction.setIcon({path: 'images/clemoji-moneymouth.svg'})
					break
			}

			this.userState = state;
			browser.storage.local.set({ userState: this.userState, notifications: this.notifications })
		}

		if (!stopApiUpdate && !['LOGGED_OUT', 'ERROR'].includes(state)) {
			if (this.pollingTimer) {
				clearTimeout(this.pollingTimer)
			}

			this.pollingTimer = setTimeout(() => {
				this.updateState(this.userState)
				this.pollingTimer = null
			}, POLLING_RATE * 1000)

			return this.getNotificationsFromAPI()
		}
	}

	/**
	 * Rafraîchit la liste des notifications depuis l'API et met à jour le badge sur l'icône
	 */
	getNotificationsFromAPI() {
		const options = `page_size=100&ordering=-pubdate`

		try {
			browser.browserAction.setBadgeText({ text: '…' })
			browser.browserAction.setBadgeBackgroundColor({ color: BADGE_COLOR_OK })
		}
		catch (err) {
			console.error(err)
		}

		return fetch(`${BASE_URL}/api/notifications/?${options}`)
		.then(res => res.json())
		.then((res) => {
			this.notifications = ((res || {}).results || []).filter(notif => !notif.is_read)
			console.info(res.results.length)
			console.dir(this.notifications)
			browser.storage.local.set({ userState: this.userState, notifications: this.notifications })

			/* On met à jour le badge sans recharger l'API */

			if (this.notifications.length) {
				browser.browserAction.setBadgeText({ text: (this.notifications.length >= 100 ? '99+' : `${this.notifications.length}`) })
				browser.browserAction.setBadgeBackgroundColor({ color: BADGE_COLOR_DEFAULT })

				this.updateState('PENDING_NOTIFICATIONS', true)
			}
			else {
				browser.browserAction.setBadgeText({ text: '' })
				this.updateState('LOGGED_IN', true)
			}
		})
		.catch((err) => {
			console.error(err)
			this.updateState('ERROR', true)
			browser.browserAction.setBadgeText({ text: 'ERR' })
			browser.browserAction.setBadgeBackgroundColor({ color: BADGE_COLOR_ERROR })
		})
	}
}

const NOTIFIER = new Notifier()
