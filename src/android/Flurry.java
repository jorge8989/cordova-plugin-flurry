package com.phonegap.plugins;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

import android.app.Application;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.urbanairship.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Flurry extends CordovaPlugin {
  
    private boolean flurryEnabled = false;
    private static final String FLURRY_KEY_STRING = "com.flurry.app_key";
  
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
      super.initialize(cordova, webView);
      setup();
    }
    
    private void setup(){
      String flurryKey = getFlurryKey(this.cordova.getActivity().getApplication());
      if (null != flurryKey) {
        FlurryAgent.onStartSession(this.cordova.getActivity(), flurryKey);
        flurryEnabled = true;
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
    
    private String getFlurryKey(Application pApplication){
      int id = pApplication.getResources().getIdentifier("config", "xml", pApplication.getPackageName());
      if (id == 0) {
          return null;
      }
      String flurryKey = null;
      XmlResourceParser xml = pApplication.getResources().getXml(id);
      int eventType = -1;
      while (eventType != XmlResourceParser.END_DOCUMENT) {

          if (eventType == XmlResourceParser.START_TAG) {
              if (xml.getName().equals("preference")) {
                  String name = xml.getAttributeValue(null, "name").toLowerCase();
                  String value = xml.getAttributeValue(null, "value");

                  if (name.startsWith(FLURRY_KEY_STRING) && value != null && !value.isEmpty()) {
                      flurryKey = value;
                      Logger.verbose("Found " + name + " in config.xml with value: " + value);
                      break;
                  }
              }
          }

          try {
              eventType = xml.next();
          } catch (Exception e) {
              Logger.error("Error parsing config file", e);
          }
      }
      
      return flurryKey;
    }

    private Map<String, String> JsonToMap(JSONObject jdata) throws JSONException {
        if(jdata == null){
            Log.d("Flurry", "not a json");
            return null;
        }
        @SuppressWarnings ("unchecked")
        Iterator<String> nameItr = jdata.keys();
        Map<String, String> params = new HashMap<String, String>();
        while(nameItr.hasNext()) {
            String name = nameItr.next();
            params.put(name, jdata.getString(name));
        }
        return params;
    }
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try{
            Log.d("Flurry", action);
            if(action.equals("startSession")) {
                FlurryAgent.onStartSession(cordova.getActivity().getApplicationContext(), args.getString(0));
            } else if(action.equals("setAppVersion")) {
                FlurryAgent.setVersionName(args.getString(0));
            } else if(action.equals("setUserID")) {
                FlurryAgent.setUserId(args.getString(0));
            } else if(action.equals("setGender")) {
                FlurryAgent.setGender((byte)args.getLong(0));
            } else if(action.equals("setAge")) {                
                FlurryAgent.setAge((int)args.getLong(0));
            } else if (action.equals("logEvent") || action.equals("logEventWithParameters")
                    || action.equals("logTimedEvent") || action.equals("logTimedEventWithParameters")) {
                boolean timed = false;
                if(args.optString(2).equalsIgnoreCase("Yes")) timed = true;
                this.logEvent(args.getString(0), args.optJSONObject(1), timed);                
            } else if(action.equals("endTimedEvent")) {
                FlurryAgent.endTimedEvent(args.getString(0));
            } else if(action.equals("endTimedEventWithParameters")) {
                Map<String, String> params = this.JsonToMap(args.optJSONObject(1));
                if(params != null)
                    FlurryAgent.endTimedEvent(args.getString(0), params);
            }
            else if (action.equals("setCrashReportingEnabled")) {
                FlurryAgent.setCaptureUncaughtExceptions(args.optString(0).equalsIgnoreCase("Yes"));
            }
            else if (action.equals("logPageView")) {
                FlurryAgent.onPageView();
            }
            else if (action.equals("logError")) {
                FlurryAgent.onError(args.getString(0), args.getString(1), " ");
            }
            else {
                Log.d("Flurry", "invalid/na flurry method: " + action);
                callbackContext.error("invalid/na flurry method: " + action);
                return false;
            }
            callbackContext.success("");
            return true; 
        } catch (JSONException e){
            Log.d("Flurry exception: ", e.getMessage());
            callbackContext.error("flurry json exception: " + e.getMessage());
            return false;
        }
    }
    
    private void logEvent(String eventName, JSONObject options, boolean timed)  throws JSONException {
        if(options != null){
            Map<String, String> params = this.JsonToMap(options);
            if(params != null)
                FlurryAgent.logEvent(eventName, params, timed);
        }else{
            FlurryAgent.logEvent(eventName, timed);
        }
    }   
}
