/**
 *  Telephony.java
 *  Cordova Mobile Data Plugin
 *
 *  Created by Sebastian Katzer (github.com/katzer) on 16/08/2013.
 *  Copyright 2013 Sebastian Katzer. All rights reserved.
 *  GPL v2 licensed
 */

package de.inhji.cordova.plugin;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.os.Bundle;
import android.content.Context;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.LOG;

import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoWcdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;

public class Telephony extends CordovaPlugin {

	private CallbackContext ctx;
	private static final String TAG = "Telephony";
	public int MobileSignalStrength = 0;

	public Context context = null;
	public TelephonyManager tm = null;
	public MyPhoneStateListener listener = null;

	public void initialize(CordovaInterface cordova, CordovaWebView webView){
		super.initialize(cordova, webView);

		context = this.cordova.getActivity().getApplicationContext();
		tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

		listener = new MyPhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

		Log.i(TAG, "Starting PhoneStateListener..");
	}

	@Override
	public boolean execute (String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		if ("getCarrier".equals(action)) {
			getCarrier(callbackContext);
			return true;
		}

		if ("getSignal".equals(action)) {
			getSignal(callbackContext);
			return true;
		}

		// Returning false results in a "MethodNotFound" error.
		return false;
	}

	private class MyPhoneStateListener extends PhoneStateListener{
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);

			int strengthAmplitude = 0;
			
			if(signalStrength.isGsm()) {
				if (signalStrength.getGsmSignalStrength() != 99)
					strengthAmplitude = signalStrength.getGsmSignalStrength() * 2 - 113;
				else
					strengthAmplitude = -1;
			} else {
				strengthAmplitude = signalStrength.getCdmaDbm();
			}

			MobileSignalStrength = strengthAmplitude;
			
			Log.i(TAG, Integer.toString(strengthAmplitude));
		}
	}

	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Stopping PhoneStateListener..");
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
	}

	/**
	 * Ruft die derzeitige Signalstärke ab
	 */
	private void getSignal (CallbackContext ctx) {
		PluginResult result = new PluginResult(PluginResult.Status.OK, MobileSignalStrength);
		ctx.sendPluginResult(result);
	}

	/**
	 * Ruft den Mobilfunkanbieter ab
	 */
	private void getCarrier (CallbackContext ctx) {
		Context context = this.cordova.getActivity().getApplicationContext();
		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String carrierName = manager.getNetworkOperatorName();

		PluginResult result = new PluginResult(PluginResult.Status.OK, carrierName);
		ctx.sendPluginResult(result);
	}
	
}
