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
import java.util.Comparator;

import edu.madcourse.dancalacci.R;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class History_byTime extends ListActivity{
	private String TAG = "circletouch.History";
	private final String TAG_FROMHISTORY = "FROMHISTORY";
	private final String TAG_FROMCATEGORYSELECT = "FROMCATEGORYSELECT";
	//	History_Adaptor adapter;
	final History_byTime thisActivity = this;
	ArrayList<String> entryList = new ArrayList<String>();
	String current_date;

	//NOT USED FOR NOW
	//History_View hist;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
						i.putExtra("origin", TAG_FROMHISTORY);
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
		Log.d(TAG, "ReadData");
		String[] myFiles = this.fileList();
		for (String list : myFiles)
		{
			if(list.contains(current_date)){
				String[] file = list.split("_");
				Log.d(TAG, "Content: "+file[1]);
				String time = file[1].replace("+", ":").replace(".txt", "");
				entryList.add(time);
			}
		}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return data;

	}


	public String formatEntry(String entry){
		String record_log; 
		record_log = entry.replace(" ", "+");
		return record_log;		
	}


	public void onAddChartClick(View v){
		Intent i = new Intent(this, CategorySelection.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(i);
	}

}
