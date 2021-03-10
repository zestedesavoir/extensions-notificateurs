import { BASE_URL } from '../../config.js'

/** 
 * Class representing the extension's core.
 * @property {number} userState - The user's state on the Website
 * @property {ZdsNotification[]} notifications - The user's pending notifications
*/
class Notifier {
	constructor() {
		this.userState = 'LOGGED_OUT'
		this.notifications = []

		browser.runtime.onMessage.addListener((msg) => {
			if (msg.state !== undefined) {
				return this.updateState(msg.state)
			}
		})
	}

	/**
	 * Update the user's state
	 * @param {number} state 
	 */
	updateState(state) {
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

		if (!['LOGGED_OUT', 'ERROR'].includes(state)) {
			return this.getNotificationsFromAPI()
		}
	}

	getNotificationsFromAPI() {
		// const options = `page_size=100&ordering=-pubdate&Authorization=${token}`
		const options = `page_size=100&ordering=-pubdate`

		return fetch(`${BASE_URL}/api/notifications/?${options}`)
		.then(res => res.json())
		.then((res) => {
			this.notifications = res.results.filter(notif => !notif.is_read)
			browser.storage.local.set({ userState: this.userState, notifications: this.notifications })
		})
		.catch((err) => {
			this.updateState('ERROR')
		})
	}
}

export const NOTIFIER = new Notifier()
