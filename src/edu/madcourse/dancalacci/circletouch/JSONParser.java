package edu.madcourse.dancalacci.circletouch;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class JSONParser {
  private static final String TAG = "JSONParser";


  public JSONParser() { }

  public JSONObject getJSONFromString(String string) {

    JSONObject jObj = new JSONObject();
    try {
      jObj = new JSONObject(string);
    } catch(JSONException e) {
      Log.e(TAG, "Error parsing data: " +e.toString());
    }
    return jObj;
  }

}
