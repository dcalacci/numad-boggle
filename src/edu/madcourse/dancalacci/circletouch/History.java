package edu.madcourse.dancalacci.circletouch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import edu.madcourse.dancalacci.R;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class History extends ListActivity{
  private String TAG = "circletouch.History";
  History_Adaptor adapter;
  final History thisActivity = this;
  ArrayList<String> list = new ArrayList<String>();

  //NOT USED FOR NOW
  //History_View hist;

  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");
    setContentView(R.layout.platechart_history);

    //addContent();
  }



  protected void onResume() {
    super.onResume();
    list.clear();
    getEntryList();
    Collections.reverse(list);

    getListView().setEmptyView(findViewById(android.R.id.empty));
    adapter = new History_Adaptor(thisActivity, R.layout.platechart_history, list);
    setListAdapter(adapter);
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

  //	public void addContent(){
  //		list.add(getTime());
  //	}

  public void addContent(String content){
    list.add(content);
  }

  public void getEntryList(){
    Log.d(TAG, "ReadData");
    String[] myFiles = this.fileList();
    for (String list : myFiles)
    {
      try {
        Log.d("readData", "File: "+list);
        FileInputStream fis = this.openFileInput(list);
        BufferedReader r = new BufferedReader(new InputStreamReader(fis));
        String s = "";
        while ((s = r.readLine()) != null) {
          int end = s.indexOf("_");
          addContent(s.substring(0, end).replace("+", " "));
        }
        r.close(); 
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
  }

  public String getEntryData(String entry){
    String data = "ERROR";

    Log.d(TAG, "ReadData");
    String[] myFiles = this.fileList();
    for (String list : myFiles)
    {
      try {
        Log.d("readData", "File: "+list);
        FileInputStream fis = this.openFileInput(list);
        BufferedReader r = new BufferedReader(new InputStreamReader(fis));
        String s = "";
        while ((s = r.readLine()) != null) {
          String[] line = s.split("_");
          if (line[0].equalsIgnoreCase(entry)){
            return data = line[0];
          }

        }
        r.close(); 
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        // toast with error message
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        // Toast with error message here
        e.printStackTrace();
      }

    }

    return data;

  }

  /**
   * Opens Chart based on date
   * Sets ListItem Click Listener
   * (non-Javadoc)
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
   */
  protected void onListItemClick(ListView l, View v, int position, long id) {
    String chart = adapter.getContent(position);
    Log.d(TAG, "onListItemClick: " + chart);
    if(! (chart.equals("ERROR"))){
      TextView entry = (TextView) v.findViewById(R.id.platechart_history_textView_content);
      String date_entry = formatEntry(entry.getText().toString());
      String data = getEntryData(date_entry);
      if(!(data.equalsIgnoreCase("ERROR"))){
        //TODO: change to proper activity
        Intent i = new Intent(this, AddChart.class);
        i.putExtra("DATA", data);
        startActivity(i);

      }else{
        Toast.makeText(v.getContext(), "ERROR: Entry not found!", Toast.LENGTH_SHORT).show();
      }
    }

  }

  public String formatEntry(String entry){
    String record_log; 
    record_log = entry.replace(" ", "+");
    return record_log;		
  }




  public class History_Adaptor extends BaseAdapter{
    private ArrayList<String> mHistoryList = new ArrayList<String>();
    private Context mContext;
    private int rowResID;

    public History_Adaptor(Context c, int rowResID, ArrayList<String> historyList){
      this.mContext = c;
      this.mHistoryList = historyList;
      this.rowResID = rowResID;
    }


    public int getCount() {
      // TODO Auto-generated method stub
      return mHistoryList.size();
    }

    public String getContent(int pos){
      return mHistoryList.get(pos);
    }


    public Object getItem(int position) {
      // TODO Auto-generated method stub
      return position;
    }


    public long getItemId(int position) {
      // TODO Auto-generated method stub
      return position;
    }

    public boolean isEmptyList(){
      return mHistoryList.contains("");
    }

    public View getView(int position, View view, ViewGroup parent) {
      boolean isEmpty = isEmptyList();

      if(view == null ){
        getListView().setEmptyView(findViewById(android.R.id.empty));
        if(!isEmpty){
          LayoutInflater inflater = LayoutInflater.from(parent.getContext());
          view = inflater.inflate(R.layout.platechart_history_rows, parent, false);
        }else{
          LayoutInflater inflater = LayoutInflater.from(parent.getContext());
          view = inflater.inflate(R.layout.platechart_history_empty, parent, false);
        }
      }

      TextView date = (TextView) 
        view.findViewById(R.id.platechart_history_textView_content);
      if(!isEmpty){
        date.setText(mHistoryList.get(position));
      }else{
        date.setText("No Entries Found");
      }

      // Give it a nice background
      return view;
    }			


  }


  public void onAddChartClick(View v){
    Intent i = new Intent(this, CategorySelection.class);
    startActivity(i);
  }

}
