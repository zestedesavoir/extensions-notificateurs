/* Variables de configuration */

export const DEBUG = false

export const BASE_URL = DEBUG ? 'https://beta.zestedesavoir.com' : 'https://zestedesavoir.com'

export const TOKEN = 'zds-notifier'

export const HTTP_STATUS_LOGGED_OUT = 401
export const HTTP_STATUS_LOGGED_IN = 200

export const BADGE_COLOR_DEFAULT = '#f8ab30'
export const BADGE_COLOR_ERROR = '#d90000'
export const BADGE_COLOR_OK = '#65931a'

export const POLLING_RATE = 30 // Secondes entre chaque mise à jour
