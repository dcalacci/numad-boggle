package edu.madcourse.dancalacci.circletouch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import edu.madcourse.dancalacci.R;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class History_byDate extends ListActivity{
  private String TAG = "circletouch.History";
  //	History_Adaptor adapter;
  final History_byDate thisActivity = this;
  ArrayList<String> entryList = new ArrayList<String>();


	//NOT USED FOR NOW
	//History _View hist;


  //NOT USED FOR NOW
  //History_View hist;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");
    setContentView(R.layout.platechart_history);

    //addContent();
  }



  protected void onResume() {
    super.onResume();
    entryList.clear();
    getDateList();

    Collections.sort(entryList);
    Collections.reverse(entryList);

    this.setListAdapter(new ArrayAdapter<String>(this, R.layout.platechart_history_rows, R.id.label, entryList));

    ListView lv = getListView();

    // listening to single list item on click

    lv.setOnItemClickListener(
        new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
            int position, long id) {

            // selected item
            String entry = ((TextView) view).getText().toString();
            String date_entry = formatEntry(entry);
            // Launching new Activity on selecting single List Item
            Intent i = new Intent(getApplicationContext(), History_byTime.class);
            // sending data to new activity
            i.putExtra("DATE", date_entry);
            startActivity(i);

          }


        }
        );

  }

  /**
   * Gets the current date and time based on the system settings/timezone
   * @return String formattedDate
   */
  public String getTime(){
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String formattedDate = df.format(c.getTime());

    return formattedDate;
  }

  public void addContent(String content){
    //entryList.add(content);
  }


  /**
   * Adds all dates from files stored on the phone to the listAdapter, 
   * in a human-readable format.
   */
  public void getDateList(){
    SimpleDateFormat fileDate = 
      new SimpleDateFormat(AddChart.FILE_NAME_FORMAT, Locale.US);
    DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);

    String[] myFiles = this.fileList();
    Log.d(TAG, "list: "+ myFiles);
    for(String file : myFiles){
      Date date = fileDate.parse(file.replace(".txt", ""),
          new ParsePosition(0));
      String entryName = df.format(date);
      if (!entryList.contains(entryName)) {
        entryList.add(entryName);
      }
/*  */
/*       String[] fileName = file.split("_"); */
/*       String date = fileName[0]; */
/*       if(!(entryList.contains(date))){ */
/*         entryList.add(date); */
/*       } */
    }
  }




  /**
   * Returns the date of this entry, formatted for referring to the file:
   * yyyy-MM-dd.
   * @param entry The entry string to format
   */
  public String formatEntry(String entry){
    SimpleDateFormat fileFormat = 
      new SimpleDateFormat(AddChart.HISTORY_DATE_FORMAT, Locale.US);
    DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
    String entryName = "";
    try {
      // parse the entry into a date, and then format it according to our
      // file names.
      Date date = df.parse(entry);
      entryName = fileFormat.format(date);
    } catch(ParseException e) {
      e.printStackTrace();
      Toast.makeText(getBaseContext(), "Sorry, I couldn't open that entry...",
          Toast.LENGTH_SHORT).show();
    }
    Log.d(TAG, "entryName: "+ entryName);
    return entryName;
  }

  private void vibrate(int ms) {
    Vibrator v = 
      (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    v.vibrate(ms);
  }
  
  public void onAddChartClick(View v){
  	vibrate(25);
    Intent i = new Intent(this, CategorySelection.class);
    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    startActivity(i);
  }
}
