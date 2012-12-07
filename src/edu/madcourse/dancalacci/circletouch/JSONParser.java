package edu.madcourse.dancalacci.circletouch;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class JSONParser {
  private static final String TAG = "JSONParser";

  public static String TAG_PCW = "pCW";
  public static String TAG_PCCW = "pCCW";
  public static String TAG_CATEGORY = "Category";


  public JSONParser() { }

  /**
   * Creates a JSONObject from a given string
   * @param string The string to parse
   */
  public static JSONObject getJSONFromString(String string) {
    JSONObject jObj = new JSONObject();
    try {
      jObj = new JSONObject(string);
    } catch(JSONException e) {
      Log.e(TAG, "Error parsing data: " +e.toString());
    }
    return jObj;
  }

  /** 
   * Creates a JSONArray from a given string
   * @param string The string to parse
   */
  public static JSONArray getJSONArrayFromString(String string) {
    JSONArray jArray = new JSONArray();
    try {
      jArray = new JSONArray(string);
    } catch(JSONException e) {
      Log.e(TAG, "Error parsing data: " +e.toString());
    }
    return jArray;
  }

  /**
   * Creates a Category from a given JSONObject
   * @param jObj The JSONObject to "parse"
   */
  public static Category getCatFromJSON(JSONObject jObj, Context ctx) {
    Category cat = new Category();
    try {
      // get the radians and the category string
      double pCW = jObj.getDouble(TAG_PCW);
      double pCCW = jObj.getDouble(TAG_PCCW);
      String category = jObj.getString(TAG_CATEGORY);
      // make the touchPoints
      TouchPoint ptCW = new TouchPoint(pCW);
      TouchPoint ptCCW = new TouchPoint(pCCW);
      // get the color, make the actual category
      int color = Category.getColor(category, ctx);
      cat = new Category(ptCCW, ptCW, category, color);

    } catch(JSONException e) {
      Log.e(TAG, "Error parsing Category data: "+e.toString());
    }
    return cat;
  }

  /**
   * Creates an arrayList of categories from a JSONArray.
   * @param jArray the JSONArray to parse
   * @param ctx The context, so we can actually create the categories
   */
  public static ArrayList<Category> getCatListFromJSONArray(
      JSONArray jArray, Context ctx) {
    ArrayList<Category> cats = new ArrayList<Category>();
    for (int i=0; i<jArray.length(); i++) {
      try {
       JSONObject jObj = jArray.getJSONObject(i);
       Category cat = getCatFromJSON(jObj, ctx);
       cats.add(cat);
      } catch(JSONException e) {
        Log.e(TAG, "Error parsing jSONArray to category list: "+e.toString());
      }
    }
      return cats;
  }

  /**
   * Creates an arraylist of categories from a JSON string
   * @param obj The string to parse
   * @param ctx The context, so we can actually create the categories
   */
  public static ArrayList<Category> getCatListFromString(
      String obj, Context ctx) {
    ArrayList<Category> cats = new ArrayList<Category>();
    try {
      JSONArray jArray = getJSONArrayFromString(obj);
      cats = getCatListFromJSONArray(jArray, ctx);
    } catch(Exception e) {
      Log.e(TAG, "Error parsing String to Category list: "+e.toString());
    }
    return cats;
      }

  public static ArrayList<Category> getCatListFromFile(String filename, Context ctx) {
    // get the time, format it, and add it to the date to make the filename
    String data = "";
    try {
      FileInputStream fis = ctx.openFileInput(filename);
      BufferedReader r = new BufferedReader(new InputStreamReader(fis));
      data = r.readLine();
      r.close();
    } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    return getCatListFromString(data, ctx);
  }

}














