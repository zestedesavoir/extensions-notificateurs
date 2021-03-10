/** 
 * Class representing a user.
 * @property {string} username
 * @property {string} avatar_url
*/
export class ZdsUser {
    /**
     * Create a user.
     * @param {string} username - The user's public name.
     * @param {string} avatar_url - The URL of the avatar (should point to a valid image).
     */
	constructor(username, avatar_url) {
		this.username = username
		this.avatar_url = avatar_url
	}
}
