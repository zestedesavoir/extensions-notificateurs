/**
* Pretty print a publication date
* @param {Date|string} pubdate
*/
export function relativeDate(pubdate) {
	const date = new Date((pubdate || '').replace(/-/g, '/').replace(/[TZ]/g, ' '))
	const now = new Date()
	
	const diff = (now.valueOf() - date.valueOf()) / 1000
	
	if (diff < 0) {
		return 'Dans le futur'
	}
	if (diff < 60) { // Moins d'une minute
		return 'À l\'instant'
	}
	if (diff < 60 * 60) { // Moins d'une heure
	const minutes = Math.round(diff / 60)
	return `Il y a ${minutes} ${minutes > 1 ? 'minutes' : 'minute'}`
	}
	if (diff < 24 * 3600) { // Moins de 24 heures
		const hours = Math.round(diff / (3600))
		return `Il y a ${hours} ${hours > 1 ? 'heures' : 'heure'}`
	}

	const yesterday = now
	yesterday.setDate(now.getDate() - 1)
	if (date.toDateString() === yesterday.toDateString()) {
		return 'Hier'
	}

	const days = Math.round(diff / (3600))
	return `Il y a ${days} ${days > 1 ? 'jours' : 'jour'}`
}
