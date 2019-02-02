/* eslint-disable no-console */
const debugMode = false

const XPATH_to_test_connection = "count(//div[contains(concat(' ',normalize-space(@class),' '),' unlogged ') and contains(concat(' ',normalize-space(@class),' '),' logbox ')])"
const XPATH_to_get_notif_block = "//div[contains(concat(' ',normalize-space(@class),' '),' notifs-links ')]/div"
const XPATH_to_count_alert     = "count(.//ul/li[not(contains(concat(' ', normalize-space(@class), ' '), ' dropdown-empty-message '))])"

let state = 0;

const connection = document.evaluate( XPATH_to_test_connection, document, null, XPathResult.ANY_TYPE, null );

if( connection.numberValue == 0) { // Logged
  state = 1;
  const notifLinks = document.evaluate( XPATH_to_get_notif_block, document, null, XPathResult.ANY_TYPE, null );

  const pmDOM  = (notifLinks.iterateNext());
  const notifDOM = (notifLinks.iterateNext());
  const alertDOM = (notifLinks.iterateNext());

  const countPM = document.evaluate( XPATH_to_count_alert , pmDOM, null, XPathResult.ANY_TYPE, null );
  const countNotif = document.evaluate( XPATH_to_count_alert, notifDOM, null, XPathResult.ANY_TYPE, null );

  if( countPM.numberValue + countNotif.numberValue > 0 ) { // 1 notif or more
    state = 2;
  }
}

// Send the state to background script
var sending = browser.runtime.sendMessage({ state: state });
sending.then(null, null);
