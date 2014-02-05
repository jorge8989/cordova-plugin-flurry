package com.feedhenry.phonegap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.AssetManager;
import android.util.Log;

public class FHUtils {
  
  public static final String EXTERNAL_CACHE_DIR = "";
  
  /**
   * Read a file in assets directory
   * @param pAssetManager
   * @param pFilePath
   * @return the content of the file
   */
  public static byte[] getAssetFileContent(AssetManager pAssetManager, String pFilePath) {
    byte[] content = null;
    InputStream is = null;
    try {
      is = pAssetManager.open(pFilePath);
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      byte[] buf = new byte[1024];
      int len;
      while ((len = is.read(buf)) > 0) {
        bos.write(buf, 0, len);
      }
      content = bos.toByteArray();
    } catch (IOException e) {
      Log.e("PhoneGap", "Can not load asset file : " + pFilePath + " : error : " + e.getMessage());
    } finally {
      if (null != is) {
        try {
          is.close();
        } catch (IOException ex) {
          Log.e("PhoneGap", "Error when try to close stream. Reason : " + ex.getMessage());
        }
      }
    }
    return content;
  }
  
  /**
   * Read the content of a file in assets directory as JSONObject
   * @param pAssetManager
   * @param pFilePath
   * @return the JSONObject from the content of the file
   */
  public static JSONObject readConfiguration(AssetManager pAssetManager, String pFilePath) {
    byte[] content = getAssetFileContent(pAssetManager, pFilePath);
    JSONObject contentJson = null;
    try {
      if (null != content) {
        String contentString = new String(content);
        contentJson = new JSONObject(contentString);
      }
    } catch (JSONException e) {
      Log.e("PhoneGap", "Configuration content is not a valid JSON object. " + e.getMessage());
    }
    return contentJson;
  }

}
