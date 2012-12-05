package edu.madcourse.dancalacci.circletouch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class History_byTime extends ListActivity{
  private String TAG = "circletouch.History";
  //	History_Adaptor adapter;
  final History_byTime thisActivity = this;
  ArrayList<String> entryList = new ArrayList<String>();
  String current_date;

  //NOT USED FOR NOW
  //History_View hist;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    Log.d(TAG, "onCreate");
    setContentView(R.layout.platechart_history);

    Button addChart_Button = (Button) this.findViewById(R.id.button_addChart);
    addChart_Button.setVisibility(Button.GONE);

    this.current_date = this.getIntent().getExtras().getString("DATE");
    //addContent();
  }



  protected void onResume() {
    super.onResume();
    entryList.clear();
    getTimeList();
    if (entryList.size() == 0){
      finish();
    }

    Collections.sort(entryList);
    Collections.reverse(entryList);

    this.setListAdapter(new ArrayAdapter<String>(this, R.layout.platechart_history_rows, R.id.label, entryList));

    ListView lv = getListView();

    lv.setOnItemClickListener(
        new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
            int position, long id) {

            // selected item
            String entry = ((TextView) view).getText().toString();
            String date_entry = formatEntry(entry.replace(":", "+"));
            String fileName = current_date+"_"+ date_entry +".txt";
            String data = getEntryData(date_entry);

            Log.d(TAG, "File Data : " + data); 
            Intent i = new Intent(getApplicationContext(), AddChart.class);
            i.putExtra("DATA", data);
            i.putExtra("FileName", fileName);
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


  public void getTimeList(){
    String[] myFiles = this.fileList();
    SimpleDateFormat fileDate = 
      new SimpleDateFormat(AddChart.FILE_NAME_FORMAT, Locale.US);
    SimpleDateFormat desiredFormat = 
      new SimpleDateFormat("hh:mm:ss a");

    for (String file : myFiles) {
      // if the date is the date we're interested in
      if (file.contains(current_date)) {
        Date date = fileDate.parse(file.replace(".txt", ""),
            new ParsePosition(0));
        String entryName = desiredFormat.format(date);
        if (!entryList.contains(entryName)) {
          entryList.add(entryName);
        }
      }
    }
/*  */
/*     Log.d(TAG, "ReadData"); */
/*     String[] myFiles = this.fileList(); */
/*     for (String list : myFiles) */
/*     { */
/*       if(list.contains(current_date)){ */
/*         String[] file = list.split("_"); */
/*         Log.d(TAG, "Content: "+file[1]); */
/*         String time = file[1].replace("+", ":").replace(".txt", ""); */
/*         entryList.add(time); */
/*       } */
/*     } */
  }

  public String getEntryData(String entry){
    String data = "ERROR";
    String fileName = current_date+"_"+entry+".txt";
    Log.d(TAG, "ReadData");
    try {
      Log.d("readData", "File: "+fileName);
      FileInputStream fis = this.openFileInput(fileName);
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


  /**
   * Returns the date of this entry from the list text, formatted for file
   * names: HH+MM+ss
   */
  public String formatEntry(String entry){
    SimpleDateFormat fileFormat = 
      new SimpleDateFormat(AddChart.HISTORY_TIME_FORMAT, Locale.US);
    SimpleDateFormat entryFormat= 
      new SimpleDateFormat("hh+mm+ss");
    entry = entry.substring(0, entry.length() - 3);
    String entryName = "";
    try {
      Log.d(TAG, "time entry: "+entry);
      Date date = entryFormat.parse(entry);
      entryName = fileFormat.format(date);
    } catch(ParseException e) {
      Log.e(TAG, "error parsing time");
      e.printStackTrace();
      Toast.makeText(getBaseContext(), "Sorry, I couldn't open that entry...",
          Toast.LENGTH_SHORT).show();
    }
    Log.d(TAG, "time entryName: "+entryName);
    return entryName;

     /* String record_log; */
     /* record_log = entry.replace(" ", "+"); */
     /* return record_log; */
  }


  public void onAddChartClick(View v){
    Intent i = new Intent(this, CategorySelection.class);
    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    startActivity(i);
  }

}
