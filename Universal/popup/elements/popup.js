import { BASE_URL } from '../../config.js'

import { ZdsElement } from './_element.js'
import { Logo } from './logo.js'

export class Popup extends ZdsElement {
	get styles () {
		return `
		`
	}

	get template() {
		return this.isLoggedIn ? `
<zds-logo></zds-logo>
<ul id="notifications-list"></ul>
<div id="bottom-buttons">
	<a id="all-notifications-button" href="${BASE_URL}/notifications/" title="Voir la liste des notifications" target="_blank" rel="noopener">
		<span>Toutes les notifications</span>
	</a>
	<a id="new-message-button" href="${BASE_URL}/mp/creer/" title="Composer un nouveau message privé" target="_blank" rel="noopener">
		<span class="sr-only">Nouveau message privé</span>
	</a>
</div>
		` : `
<zds-logo></zds-logo>
<div id="loggedout">
	<h1 id="title">Oh-oh !</h1>
	<div id="clemoji-asleep"><img src="../../images/clemoji-asleep.svg"></div>
	<div id="message">On dirait que vous êtes déconnecté(e).</div>
</div>
<a id="signin-button" href="${BASE_URL}/membres/connexion/" target="_blank" rel="noopener">Connexion</a>`
	}

	constructor() {
		super()
		// this.isLoggedIn = true
	}
	
}
window.customElements.define('zds-popup', Popup)
