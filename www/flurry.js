// clobbers: navigator.flurry

var exec = require('cordova/exec');

var flurry = {

	// These functions must be called before you start the Flurry session

	setAppVersion: function(version, success, fail) {
		exec(success, fail, 'Flurry', 'setAppVersion', [version]);
	},

	// argument must be Yes or No, because it's objective C
	setShowErrorInLogEnabled: function(enableValue, success, fail) {
		exec(success, fail, 'Flurry', 'setShowErrorInLogEnabled', [enableValue]);
	},

	// argument must be Yes or No, because it's objective C
	setEventLoggingEnabled: function(enableValue, success, fail) {
		exec(success, fail, 'Flurry', 'setEventLoggingEnabled', [enableValue]);
	},

	// argument must be Yes or No, because it's objective C
	setDebugLogEnabled: function(enableValue, success, fail) {
		exec(success, fail, 'Flurry', 'setDebugLogEnabled', [enableValue]);
	},

	// argument must be Yes or No, because it's objective C
	setSecureTransportEnabled: function(enableValue, success, fail) {
		exec(success, fail, 'Flurry', 'setSecureTransportEnabled', [enableValue]);
	},

	// seconds must be an integer
	setSessionContinueSeconds: function(seconds, success, fail) {
		exec(success, fail, 'Flurry', 'setSessionContinueSeconds', [seconds]);
	},

	// argument must be Yes or No, because it's objective C
	setCrashReportingEnabled: function(enableValue, success, fail) {
		exec(success, fail, 'Flurry', 'setCrashReportingEnabled', [enableValue]);
	},

	// End of functions that must be called before Flurry session starts



	// key is a string
	startSession: function(key, success, fail) {
		exec(success, fail, 'Flurry', 'startSession', [key]);
	},

	// event must be a string
	//logEvent: function(event, success, fail) {
	//	exec(success, fail, 'Flurry', 'logEvent', [event]);
	//};
	//backward compatible
	logEvent: function(event, parameters, success, fail) {
		exec(success, fail, 'Flurry', 'logEventWithParameters', [event, parameters || {}]);
	},

	// parameters must be a JSON dictionary that contains only strings like {id:"4", price: "471", location: "New York"}
	logEventWithParameters: function(event, parameters, success, fail) {
		exec(success, fail, 'Flurry', 'logEventWithParameters', [event, parameters]);
	},

	logPageView: function(success, fail) {
		exec(success, fail, 'Flurry', 'logPageView', []);
	},

	// timed must be Yes or No, because it's objective C
	logTimedEvent: function(event, timed, success, fail) {
		exec(success, fail, 'Flurry', 'logTimedEvent', [event, timed]);
	},

	// parameters must be a JSON dictionary that contains only strings like {id:"4", price: "471", location: "New York"}
	// timed must be Yes or No, because it's objective C
	logTimedEventWithParameters: function(event, parameters, timed, success, fail) {
		exec(success, fail, 'Flurry', 'logTimedEventWithParameters', [event, parameters, timed]);
	},

	endTimedEvent: function(event, success, fail) {
		exec(success, fail, 'Flurry', 'endTimedEvent', [event]);
	},

	// parameters must be a JSON dictionary that contains only strings like {id:"4", price: "471", location: "New York"}
	// only used if you want to override the original parameters
	endTimedEventWithParameters: function(event, parameters, success, fail) {
		exec(success, fail, 'Flurry', 'endTimedEventWithParameters', [event, parameters]);
	},

	// userID must be a string
	setUserID: function(userID, success, fail) {
		exec(success, fail, 'Flurry', 'setUserID', [userID]);
	},

	// gender must be a string. male and female are acceptable values
	setGender: function(gender, success, fail) {
		exec(success, fail, 'Flurry', 'setGender', [gender]);
	},

	// age must be an integer
	setAge: function(age, success, fail) {
		exec(success, fail, 'Flurry', 'setAge', [age]);
	},

	// latitude and longitude must be doubles; horizontal and vertical accuracy must be float
	setLatitude: function(latitude, longitude, horizontalAccuracy, verticalAccuracy, success, fail) {
		exec(success, fail, 'Flurry', 'setLatitude', [latitude, longitude, horizontalAccuracy, verticalAccuracy]);
	},

	// argument must be Yes or No, because it's objective C
	setSessionReportsOnCloseEnabled = function(enabled, success, fail) {
		exec(success, fail, 'Flurry', 'setSessionReportsOnCloseEnabled', [enabled]);
	},

	// argument must be Yes or No, because it's objective C
	setSessionReportsOnPauseEnabled = function(enabled, success, fail) {
		exec(success, fail, 'Flurry', 'setSessionReportsOnPauseEnabled', [enabled]);
	},

	logError: function(errorID, message, success, fail) {
		exec(success, fail, 'Flurry', 'logError', [errorID, message]);
	}

};

module.exports = flurry;
