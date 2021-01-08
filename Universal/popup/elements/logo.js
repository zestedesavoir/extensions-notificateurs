import { BASE_URL } from '../../config.js'
import { ZdsElement } from './_element.js'

export class Logo extends ZdsElement {
	get styles () {
		return `
zds-logo {
	display: block;
}
zds-logo a {
	display: block;
	text-align: center;
	background-color: var(--color-navy);
}
zds-logo a:hover,
zds-logo a:focus {
	background-color: var(--color-navy-light);
}
zds-logo img {
	height: 3rem;
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
