/**
 *  email_composer.js
 *  Cordova EmailComposer Plugin
 *
 *  Created by Sebastian Katzer (github.com/katzer) on 10/08/2013.
 *  Copyright 2013 Sebastian Katzer. All rights reserved.
 *  GPL v2 licensed
 */

var Telephony = function () {

};

Telephony.prototype = {
    /**
     * Öffnet den Email-Kontroller mit vorausgefüllten Daten.
     *
     * @param {Object?} options
     */
    getCarrier: function (callback) {
        var callbackFn = null;

		if (typeof callback === 'function'){
			callbackFn = function (code) {
				callback.call(window, code);
			};
		}

        cordova.exec(callbackFn, callbackFn, 'Telephony', 'getCarrier', []);
    },
	getSignal: function(callback){
		var callbackFn = null;

		if (typeof callback === 'function'){
			callbackFn = function (code) {
				callback.call(window, code);
			};
		}

        cordova.exec(callbackFn, callbackFn, 'Telephony', 'getSignal', []);
    }



};

//var plugin = new Telephony();
//module.exports = plugin;
window.Telephony = new Telephony();