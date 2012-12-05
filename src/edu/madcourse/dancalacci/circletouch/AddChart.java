package edu.madcourse.dancalacci.circletouch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddChart extends Activity {
  private final String TAG = "circletouch.circle.AddChart";
  public final static String TAG_ISEDITING = "editing";
  public final static String TAG_FILENAME = "FileName";
  public final static String FILE_NAME_FORMAT = "yyyy-MM-dd_HH+mm+ss";
  public final static String HISTORY_DATE_FORMAT = "yyyy-MM-dd";
  public final static String HISTORY_TIME_FORMAT = "HH+mm+ss";
  /** Called when the activity is first created. */
  private Chart_View cir;
  private String mOrigin = "";
  private String mFileName;
  private String static_data;
  private ArrayList<String> categories;

  private Date mDate;

  private Boolean mIsEditing = false;

  public void onCreate(Bundle savedInstanceState) {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.platechart_add);

    Bundle extras = getIntent().getExtras();

    // The list of categories, populated either from the category selection
    // screen or from historty
    categories =  extras.getStringArrayList("categories");

    // data we get from reading the file from history
    static_data = extras.getString("DATA");

    // file name of the file we're getting data from or saving
    mFileName = extras.getString("FileName");

    // where we're being called from - either category select or history.
    mOrigin = extras.getString("origin");

    mIsEditing = extras.getBoolean(TAG_ISEDITING);
    final Chart_View c = (Chart_View) this.findViewById(R.id.circle);
    cir = c;

    cir.setmOrigin(mOrigin);

    cir.setIsEditing(mIsEditing);
  }

  public void onResume(){
    super.onResume();
    Button deleteButton = (Button) this.findViewById(R.id.addChart_Delete);
    Button edit_addButton = (Button) this.findViewById(R.id.addChart_Save);

    // if we're editing the chart...
    if (mIsEditing) {
      Log.d(TAG, "in onResume, We're editing...");
      deleteButton.setVisibility(Button.VISIBLE);
      edit_addButton.setText(R.string.save_label);
    } else {
      Log.d(TAG, "in onResume, We're NOT editing...");
      deleteButton.setVisibility(Button.INVISIBLE);
      edit_addButton.setText(R.string.edit_label);
    }

    // coming from categoryselection
    if(static_data == null){
      Log.d(TAG, "coming from category selection, no data given");
      // delete button shouldn't exist if we're just starting to add a chart.
      deleteButton.setVisibility(Button.INVISIBLE);
      addCategories(categories);
    }
    // coming from history
    else{
      Log.d(TAG, "coming from history, have data");
      // get the array of categories from the extra
      ArrayList<Category> cats = 
          JSONParser.getCatListFromString(static_data, this);
      // set the circles' categories and touchpoints
      this.selectCategoryObjects(cats);
      cir.setmCategory(cats);
      cir.setmPoints(Category.getTouchPointsFromCategoryList(cats));
      cir.linkPointsAndCategories();
    }

    // make the date format for the title

    SimpleDateFormat fileDate =
      new SimpleDateFormat(FILE_NAME_FORMAT, Locale.US);

   // dates in java are terrible.  what the fuck is a parseposition.
    if (!mFileName.equals("")) {
      Log.d(TAG, "@@@@no filename");
      String filename = mFileName;
      mDate = fileDate.parse(filename.replace(".txt", ""), 
          new ParsePosition(0));
      /* mDate = titleDate.format(mFileName.replace(".txt", "")); */
    }

    String title = "New Chart";
    if (mDate != null) { 
      title = DateFormat.getDateInstance().format(mDate);
      /* title = titleDate.format(mDate); */
    }
    TextView tv = (TextView)this.findViewById(R.id.addChart_Title);
    tv.setText(title);

  }

  /**
   * Gets the category data from a given filename
   * @param filename The name of the file to get data from.
   */
  private String getDataFromFile(String filename) {
    String data = "ERROR";
    Log.d(TAG, "ReadData");
    try {
      Log.d("readData", "File: "+mFileName);
      FileInputStream fis = this.openFileInput(mFileName);
      BufferedReader r = new BufferedReader(new InputStreamReader(fis));
      data = r.readLine();
      r.close(); 
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return data;
  }


  private void selectCategoryObjects(ArrayList<Category> cats) {
    for (Category cat : cats) {
      selectCategory(cat.getCategory());
    }
  }

  private void addCategories(ArrayList<String> categories) {
    for (String cat : categories) {
      addCategory(cat);
    }
  }

  private void selectCategory(String category) {
    if (category.equals("Protein")) {
      cir.setProteinSelect(this.findViewById(R.id.protein_view));
    } else if (category.equals("Vegetable")) {
      cir.setVegetableSelect(this.findViewById(R.id.vegetable_view));
    } else if ( category.equals("Fruit")) {
      cir.setFruitSelect(this.findViewById(R.id.fruit_view));
    } else if (category.equals("Dairy")) {
      cir.setDairySelect(this.findViewById(R.id.dairy_view));
    } else if (category.equals("Grain")) {
      cir.setGrainSelect(this.findViewById(R.id.grain_view));
    } else if (category.equals("OilSugar")) {
      cir.setOilSugarSelect(this.findViewById(R.id.oil_view));
    }
  }

  private void addCategory(String category) {
    if (category.equals("Protein")) {
      final View protein_view = (View) this.findViewById(R.id.protein_view);
      onProteinClicked(protein_view);
    } else if (category.equals("Vegetable")) {
      final View vegetable_view = (View) this.findViewById(R.id.vegetable_view);
      onVegetableClicked(vegetable_view);
    } else if ( category.equals("Fruit")) {
      final View fruit_view = (View) this.findViewById(R.id.fruit_view);
      onFruitClicked(fruit_view);
    } else if (category.equals("Dairy")) {
      final View dairy_view = (View) this.findViewById(R.id.dairy_view);
      onDairyClicked(dairy_view);
    } else if (category.equals("Grain")) {
      final View grain_view = (View) this.findViewById(R.id.grain_view);
      onGrainClicked(grain_view);
    } else if (category.equals("OilSugar")) {
      final View oil_view = (View) this.findViewById(R.id.oil_view);
      onOilSugarClicked(oil_view);
    }
  }


  public void onProteinClicked(View v){
    if (mIsEditing) {
      vibrate(25);
      cir.onProteinClicked(v);
    }
  }

  public void onVegetableClicked(View v){
    if (mIsEditing) {
      vibrate(25);
      cir.onVegetableClicked(v);
    }
  }

  public void onDairyClicked(View v){
    if (mIsEditing) {
      vibrate(25);
      cir.onDairyClicked(v);
    }
  }

  public void onFruitClicked(View v){
    if (mIsEditing) {
      vibrate(25);
      cir.onFruitClicked(v);
    }
  }

  public void onGrainClicked(View v){
    if (mIsEditing) {
      vibrate(25);
      cir.onGrainClicked(v);
    }
  }

  public void onOilSugarClicked(View v){
    if (mIsEditing) {
      vibrate(25);
      cir.onOilSugarClicked(v);
    }
  }

  private void vibrate(int ms) {
    Vibrator v = 
      (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    v.vibrate(ms);
  }

  public void onSaveClicked(View v){
    // clicked save
    if (mIsEditing) {
      Log.d(TAG, "Save clicked, saving...");
      JSONArray chartData = cir.getChartData();
      String data = parseData(chartData);
      save(data, mFileName);
      mIsEditing = false;
      static_data = getDataFromFile(mFileName);
      cir.setIsEditing(false);
          onResume();
    } else { // clicked edit
      Log.d(TAG, "Edit clicked, editing...");
      mIsEditing = true;
      cir.setIsEditing(true);
      cir.invalidate();
      onResume();
    }
  }

public String parseData(JSONArray jArray){
  String data = jArray.toString();
  return data;
}

/**
 * Delete the chart and set the deselects for all categories
 * @param v
 */
public void onDeleteClicked(View v){
  boolean deleted;
  if(!mFileName.equals("")){
    File file = new File(this.getFilesDir(), mFileName);
    deleted = file.delete();
  }else {
    deleted = true;
  }
  // show a toast, tell the user if it succeeded.
  if (deleted) {
    Toast.makeText(getBaseContext(), "Entry deleted!", Toast.LENGTH_SHORT).show();
  } else {
    Toast.makeText(getBaseContext(), "Error deleting entry.",
        Toast.LENGTH_LONG).show();
  }
  finish();
}

/**
 * Gets the current date and time based on the system settings/timezone
 * @return String formattedDate
 */
public String getDateTime(){
  Calendar c = Calendar.getInstance();
  SimpleDateFormat df = new SimpleDateFormat(FILE_NAME_FORMAT);
  String formattedDate = df.format(c.getTime());

  return formattedDate;
}
/**
 * Gets the Current date for file name
 * @return String date - format YYYY_MM_DD
 */
public String getDate(){
  Calendar c = Calendar.getInstance();
  SimpleDateFormat df = new SimpleDateFormat(FILE_NAME_FORMAT);
  String formattedDate = df.format(c.getTime());    
  return formattedDate;
}

/**
 * Save file to internal memory
 * @param data - Stored data
 */
public void save(String data, String FileName){
  String date = getDate();
  if(FileName.equals("")){         // generates name of file
    FileName = date+".txt";
    mFileName = FileName;
    Log.d(TAG, "making a new filename...");

    SimpleDateFormat fileDate =
      new SimpleDateFormat(FILE_NAME_FORMAT, Locale.US);
    mDate = fileDate.parse(mFileName.replace(".txt", ""), 
          new ParsePosition(0));

  }else if (!FileName.equals("")){    // Deletes current file
    Log.d(TAG, "saving to a file that already exists");
    File file = new File(this.getFilesDir(), FileName);
    boolean deleted = file.delete();
  }
  Log.d(TAG, "save() filename: " + FileName);
  try{
    FileWriter write = new FileWriter(this.getFilesDir() + "/" + FileName, true);
    write.write(data+"\r\n"); //adds new line.
    write.close();
    // if we get here with no exceptions, we did it!
    Toast.makeText(getBaseContext(), "Entry saved!", 
          Toast.LENGTH_SHORT).show();

  } catch (FileNotFoundException e) {
  Toast.makeText(getBaseContext(), "Uh-Oh.  We couldn't save the chart. Try Again?", 
          Toast.LENGTH_SHORT).show();
    e.printStackTrace();
  } catch (IOException e) {
  Toast.makeText(getBaseContext(), "Oops! We couldn't read from your phone. Try Again!", 
          Toast.LENGTH_SHORT).show();
    e.printStackTrace();
  }
}


}
