import { BASE_URL } from '../config.js'
import { ZdsUser } from './user.js'

/** 
 * Class representing a notification.
 * @property {string} title - The title of the notification
 * @property {ZdsUser} sender - The author of the notification
 * @property {string} url - The URL the notification points to
 * @property {string} fullURL - The full URL the notification points to
*/
export class ZdsNotification {
	get fullURL() {
		return `${BASE_URL}${this.url}`
	}

    /**
     * Create a notification.
     * @param {object} notif - The raw notification retrieved from the API
     */
	constructor(notif) {
		this.title = notif.title
		this.url = notif.url
		this.sender = new ZdsUser(notif.sender.username, notif.sender.avatar_url)
	}
}
