/* Variables de configuration (répétées depuis `config.js`) en l'absence de support des modules ES6 dans les extensions */

const DEBUG = true
const BASE_URL = DEBUG ? 'https://beta.zestedesavoir.com' : 'https://zestedesavoir.com'

if (!document.location.href.startsWith(BASE_URL)) {
	console.warn('ZdS: Domaine ignoré')
}
else {
	/* Sélecteurs pour détecter l'état de connexion et le nombre de notifications */

	const SELECTOR_LOGBOX = '.logbox'
	const SELECTOR_COUNT_ALERTS = '.notifs-links>div:not(:nth-child(3)) ul.dropdown-list>li:not(.dropdown-empty-message)'

	/* Script de fond, injecté dans la page */

	let state = 'LOGGED_OUT'

	const logbox = document.querySelector(SELECTOR_LOGBOX)

	if (logbox) { // Logged
		state = 'LOGGED_IN'

		const countTotal = logbox.querySelectorAll(SELECTOR_COUNT_ALERTS).length

		if (countTotal > 0) { // 1 notif or more
			state = 'PENDING_NOTIFICATIONS'
		}
	}

	console.info('state', state)

	// Send the state to background script
	browser.runtime.sendMessage({ state: state })
	.catch((err) => {
		console.error(err);
	})
}
