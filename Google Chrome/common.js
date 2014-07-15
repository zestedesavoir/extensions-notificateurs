
/**
 * Prototypes
 */
Object.prototype.extend = function(obj) {
	for(i in obj) {
		if (typeof obj[i] === "object") {
			if (!this[i]) {
				this[i] = {};
			}
			this[i].extend(obj[i]);
		}
		else {
			this[i] = obj[i];
		}
	}
};





var AjaxRequest = function() {
	if (typeof arguments[0] == 'string') {
		this.url = arguments[0];
	}
	else {
		this.url = arguments[0].url;
	}
	
	if (arguments[1] && typeof arguments[1] == 'string') {
		this.postData = arguments[1];
	}
	else if (arguments[1] && typeof arguments[1] == 'object') {
		this.postData = arguments[1];
	}
	else {
		this.postData = arguments[0].data || null;
	}
	
	if (this.postData && arguments[2] && typeof arguments[2] == 'function') {
		this.successCallback = arguments[2];
	}
	else if (!this.postData && arguments[1] && typeof arguments[1] == 'function') {
		this.successCallback = arguments[1];
	}
	else {
		this.successCallback = arguments[0].success || null;
	}

	if (this.postData && typeof arguments[3] == 'function') {
		this.errorCallback = arguments[3];
	}
	else if (!this.postData && arguments[2] && typeof arguments[2] == 'function') {
		this.errorCallback = arguments[2];
	}
	else {
		this.errorCallback = arguments[0].error || null;
	}

	if (this.postData && typeof arguments[4] == 'boolean' || arguments[0].cache != undefined) {
		this.date = new Date();
		this.timestamp = this.date.getTime();
		this.cache = arguments[4] == 'boolean' || arguments[0].cache;
	}
	else if (!this.postData && typeof arguments[3] == 'boolean' || arguments[0].cache != undefined) {
		this.date = new Date();
		this.timestamp = this.date.getTime();
		this.cache = arguments[3] == 'boolean' || arguments[0].cache;
	}
	else {
		this.cache = true;
	}

	var XHR = new XMLHttpRequest();

	XHR.onreadystatechange = function (e) {
		if (this.readyState == 4 && this.status == 200) {
			if(this._successCallback)
				this._successCallback(e);
		}
		else if (this.readyState == 4) {
			console.dir(this);
			if(this._errorCallback)
				this._errorCallback(e);
		}
	}

	XHR._successCallback = this.successCallback || null;
	XHR._errorCallback = this.errorCallback || null;


	if (this.postData) {
		XHR.open('POST', this.url+(this.cache ? '' : '?timestamp='+this.timestamp), true);
		XHR.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
		XHR.setRequestHeader('Content-type','application/x-www-form-urlencoded');
		XHR.send(typeof this.postData == 'string' ? this.postData : objectToQueryString(this.postData));
	}
	else {
		XHR.open('GET', this.url+(this.cache ? '' : '?timestamp='+this.timestamp), true);
		XHR.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
		XHR.send();
	}

	return this;
}