package com.feedhenry.phonegap.analytics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.feedhenry.phonegap.FHUtils;
import com.flurry.android.FlurryAgent;

/**
 * A plugin which can be used to interact with Flurry android SDK for analytics purpose. 
 * A file called config.json should be exist in assets/settings directory and the flurry API key should be specified as the value of key "flurryAppKey". 
 * If the key is not presented the Flurry session will not be started.
 * @author weili
 *
 */

public class Analytics extends CordovaPlugin {

  private static final String LOG_EVENT = "logEvent";
  private static final String TAG = "Analytics";
  private static final String CONFIGURATION_FILE_PATH = "settings/config.json";
  private static final String FLURRY_KEY_STRING = "flurryAppKey";
  private boolean flurryEnabled = false;
  
  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    setup();
  }
  
  private void setup(){
    JSONObject config = FHUtils.readConfiguration(this.cordova.getActivity().getAssets(), CONFIGURATION_FILE_PATH);
    try {
      if (null!=config && config.has(FLURRY_KEY_STRING) && config.getString(FLURRY_KEY_STRING).length() > 0) {
        String flurryKey = config.getString(FLURRY_KEY_STRING);
        Log.d("PhoneGap", "Found flurry key " + flurryKey);
        FlurryAgent.onStartSession(this.cordova.getActivity(), flurryKey);
        Log.d("PhoneGap", "Flurry enabled");
        flurryEnabled = true;
      }
    } catch (JSONException e) {
      Log.e("PhoneGap", e.getMessage());
    }
  }
  
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if(LOG_EVENT.equals(action)){
      doLogEvent(args);
    }
    callbackContext.success();
    return true;
  }
  
  private void doLogEvent(JSONArray pArgs){
    if(flurryEnabled){
      Log.d(TAG, "Flurry event log enabled");
      String eventName = pArgs.optString(0, null);
      String param = null;
      if(pArgs.length() > 1){
        param = pArgs.optString(1, null);
      }
      JSONObject params = null;
      try{
        if(null != param){
          params = new JSONObject(param);
        }
      } catch(Exception e){
        Log.d(TAG, e.getMessage());
      }
      if(null != params){
        Map<String, String> paramMap = new HashMap<String, String>();
        Iterator it = params.keys();
        while(it.hasNext()){
          String key = (String) it.next();
          String value = params.optString(key);
          paramMap.put(key, value);
        }
        if(null != eventName){
          Log.d(TAG, "log flurry event " + eventName + " with params");
          FlurryAgent.onEvent(eventName, paramMap);
        }
      } else {
        if(null != eventName){
          Log.d(TAG, "log flurry event " + eventName);
          FlurryAgent.onEvent(eventName);
        }
      }
    } else {
      Log.d(TAG, "Flurry event log not enabled");
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if(flurryEnabled){
      FlurryAgent.onEndSession(this.cordova.getActivity());
      flurryEnabled = false;
    }
    
  }

  @Override
  public void onReset() {
    super.onReset();
    if(flurryEnabled){
      FlurryAgent.onEndSession(this.cordova.getActivity());
      flurryEnabled = false;
    }
  }
}
