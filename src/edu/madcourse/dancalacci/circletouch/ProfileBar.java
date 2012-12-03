package edu.madcourse.dancalacci.circletouch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;



public class ProfileBar {
  ArrayList<Category> mCategories;
  ArrayList<ProfileBarSegment> mSegments;
  Context mContext;
  private static final String TAG = "edu.madcourse.circletouch.ProfileBar";

  // Constructor for a given list of categories
  public ProfileBar(ArrayList<Category> categories) {
    mCategories = categories;
    mProfileBarSegments = categoriesToSegments(categories);
  }

  // Constructor with only a context means we have to get categories from
  // files.
  public ProfileBar(Context ctx) {
    mContext = ctx;
  }

  /**
   * Creates an arrayList of segments that represents the averaged totals of
   * all segments - the sum of the value of the segments returned is 1.
   * god this is awful
   */
  private ArrayList<ProfileBarSegment> averageAllSegments() {
    // Keeping track of all the segments total percentages
    HashMap<Integer, Double> segmentTotal = 
      new HashMap<Integer, Double>();
    // Count the number of segments by color
    HashMap<Integer, Integer> numberOfSegmentsWithColor =
      new HashMap<Integer, Integer>();

    // The hashmap to return, that contains the averages for each color
    HashMap<Integer, Double> segmentAverage = 
      new HashMap<Integer, Double>();

    // populate the counting map and the total map
    for (ProfileBarSegment segment : getAllSegments()) { 
      // add 1 to the number of segments with this color
      numberOfSegmentsWithColor.put(segment.getColor(), 
          numberOfSegmentsWithColor.get(segment.getColor()) + 1);
      // add this segment's value to the total
      segmentTotal.put(segment.getColor(),
          segmentTotal.get(segment.getColor()) + segment.getValue());
    }

    // Turn the totals into averages
    Iterator<HashMap.Entry<Integer, Double>> it = segmentTotal.entrySet().iterator();
    while (it.hasNext()) {
      HashMap.Entry<Integer, Double> pair = it.next();
      // put the average percentage in te segmentAverage map
      segmentAverage.put(pair.getKey(), 
          // compute the average percentage
          (pair.getValue() /
           numberOfSegmentsWithColor.get(pair.getKey())));
    }
    ArrayList<ProfileBarSegment> segments = new ArrayList<ProfileBarSegment>();
    // segmentAverage now has Color -> Average Percentage. 

    Iterator<HashMap.Entry<Integer, Double>> iter = segmentAverage.entrySet().iterator();
    // add all of the average percentages to a list of segments
    while(iter.hasNext()) {
      HashMap.Entry<Integer, Double> pair = it.next();
      segments.add(new ProfileBarSegment(pair.getValue(), pair.getKey()));
    }
    double total = 0;

    // compute the total for everything
    for (ProfileBarSegment segment : segments) {
      total+=segment.getValue();
    }
    // using the total, compute the correct fraction for each average
    for (ProfileBarSegment segment : segments) {
      double val = segment.getValue();
      segment.setValue(val / total);
    }
    // now each segment is a decimal representing it's percentage representation
    return segments;
  }

  private int countSegmentsWithColor(ArrayList<ProfileBarSegment> segments, int color) {
    int counter = 0;
    for (ProfileBarSegment seg : segments) {
      if (seg.getColor() == color) {
        counter++;
      }
    }
  }
  /**
   * Returns an ArrayList of all the segments stored in files
   */
  private ArrayList<ProfileBarSegment> getAllSegments() {
    ArrayList<ProfileBarSegment> segments = new ArrayList<ProfileBarSegment>();
    for (Category cat : getAllCategoriesFromFiles()) {
      segments.add(new ProfileBarSegment(cat));
    }
    return segments;
  }

  /**
   * Returns a list of all the categories stored in files
   */
  private ArrayList<Category> getAllCategoriesFromFiles() {
    String[] savedFiles = mContext.fileList();
    ArrayList<Category> categories = new ArrayList<Category>();
    for (String filename : savedFiles) {
      String fileData = getFileData(filename);
      // if we parsed the file correctly
      if (!fileData.equals("error")) {
        // add all the categories in the current file to the list
        categories.addAll(JSONParser.getCatListFromString(fileData, mContext));
      }
    }
  }

  /**
   * Gets file data from a given filename
   * @param filename the file to get data from
   */
  private String getFileData(String filename) {
    String data = "error";
    try {
      FileInputStream fis = mContext.openFileInput(filename);
      BufferedReader r = new BufferedReader(new InputStreamReader(fis));
      data = r.readLine();
      r.close();
    } catch (FileNotFoundException e) {
      /* Toast.makeText(mContext, "Something's wrong with the filesystem..."); */
      Log.e(TAG, "Couldn't find a file in Profile: ");
          e.printStackTrace();
    } catch (IOException e) {
      /* Toast.makeText(mContext, "I burped while reading a file, so I can't show your profile"); */
      Log.e(TAG, "Had trouble reading from a file inn Profile: ");
      e.printStackTrace();
    }
    return data;
  }

/**
 * Converts an ArrayList of categories to an ArrayList of segments
 * @param cats The list of categories to convert
 */
private ArrayList<ProfileBarSegment> categoriesToSegments(ArrayList<Category> cats) {
  ArrayList<ProfileBarSegment> segments = new ArrayList<ProfileBarSegment>();
  // iterate through cats, calculating percentage for each
  for (Category cat : cats) {
    segments.add(new ProfileBarSegment(cat));
  }
  return segments;
}

/**
 * Uses the rad values of the given category to calculate the percentage
 * of the circle it takes up
 * @param cat The category we're using to calculate the percentages
 */
protected static double categoryToPercentage(Category cat) {
  double diff = TouchPoint.getDistCW(cat.getpCCW(), cat.getpCW());
  double frac = diff/(2*Math.PI);
  return frac;
}


}
