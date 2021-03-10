/* Variables de configuration (répétées depuis `config.js`) en l'absence de support des modules ES6 dans les extensions */

const DEBUG = false
const BASE_URL = DEBUG ? 'https://beta.zestedesavoir.com' : 'https://zestedesavoir.com'

/* On active le script si on est sur une page du site */

if (document.location.href.startsWith(BASE_URL)) {
	/* Sélecteurs pour détecter l'état de connexion et le nombre de notifications */

	const SELECTOR_LOGBOX = '.logbox'
	const SELECTOR_COUNT_ALERTS = '.notifs-links>div:not(:nth-child(3)) ul.dropdown-list>li:not(.dropdown-empty-message)'

	/* Le script qui scanne la page */

	let state = 'LOGGED_OUT'

	const logbox = document.querySelector(SELECTOR_LOGBOX)

	if (logbox) { // L'utilisateur est connecté
		state = 'LOGGED_IN'

		const countTotal = logbox.querySelectorAll(SELECTOR_COUNT_ALERTS).length

		if (countTotal > 0) { // Il a des notifications non lues
			state = 'PENDING_NOTIFICATIONS'
		}
	}

	/* On envoie l'info vers le script de fond (notifier) */

	browser.runtime.sendMessage({ state: state })
	.catch((err) => {
		console.error(err);
	})
}
