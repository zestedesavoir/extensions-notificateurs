import { BASE_URL } from '../../config.js'
import { ZdsElement } from './_element.js'

/**
 * WebComponent permettant d'afficher le logo ZdS avec un lien vers la page d'accueil
 */
export class Logo extends ZdsElement {
	get styles () {
		return `
zds-logo {
	display: block;
	border-bottom: 2px solid var(--color-orange);
}
zds-logo a {
	display: block;
	text-align: center;
	line-height: 1;
	background-color: var(--color-logo);
}
zds-logo a:hover,
zds-logo a:focus {
	background-color: var(--color-primary);
}
zds-logo img {
	height: 4rem;
}
		`
	}

	get template() {
		return `
<a href="${BASE_URL}" target="_blank" rel="noopener">
	<img src="../images/logo.svg" alt="Zeste de Savoir" />
</a>
		`
	}

	constructor() {
        super()
	}
	
}
window.customElements.define('zds-logo', Logo)
