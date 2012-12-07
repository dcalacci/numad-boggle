package edu.madcourse.dancalacci.circletouch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import edu.madcourse.dancalacci.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class History_byDate_BarChart_LazyAdapter extends BaseAdapter {
	private static final String TAG = "circletouch.History_byTime_BarChart";
	private Context context;
	private ArrayList<String> items;
	private static LayoutInflater inflater=null;
	ProfileBar profileBar = new ProfileBar(context);

	public History_byDate_BarChart_LazyAdapter(Context context, 
			ArrayList<String> items) {
		this.context = context;
		this.items = items;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public String getEntry(int position){
		return items.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.platechart_history_rows_barchart, null);

		TextView title = (TextView)vi.findViewById(R.id.label); // title
		title.setLayoutParams(
				new LinearLayout.LayoutParams(0,LayoutParams.FILL_PARENT, (float)0.51));
		
		LinearLayout layout_history_barchart = (LinearLayout)vi.findViewById(R.id.history_barchart);
		layout_history_barchart.setLayoutParams(
				new LinearLayout.LayoutParams(0,LayoutParams.FILL_PARENT, (float)0.49));
		
		TextView empty = (TextView)vi.findViewById(R.id.byTime_empty_text); // Empty entry 
		empty.setVisibility(View.INVISIBLE);

		String entry = items.get(position);
		title.setText(entry);
		// Setting all values in listview

		/* setDefaultsegments(vi); */
		setSegments(vi, position);

		return vi;
	}
	
	private void setSegments(View v, int index) {
    Iterator<Map.Entry<String, Double>> it = getValueMap(index).entrySet().iterator();
    
    while (it.hasNext()) {
      Map.Entry<String, Double> pairs = (Map.Entry<String, Double>)it.next();
      //System.out.println(pairs.getKey() + " = " + pairs.getValue());
      setSegmentValue(pairs.getKey(), pairs.getValue(), v);
      
      Log.d(TAG, "Setting "+pairs.getKey() +" to " +pairs.getValue());
      /* it.remove(); // avoids a ConcurrentModificationException */
    }
  }
	
	private void setSegmentValue(String color, double value, View v) {
    TextView protein = (TextView)v.findViewById(R.id.byTime_protein_block);
    TextView vegetable = (TextView)v.findViewById(R.id.byTime_vegetable_block);
    TextView dairy = (TextView)v.findViewById(R.id.byTime_dairy_block);
    TextView fruit = (TextView)v.findViewById(R.id.byTime_fruit_block);
    TextView grain = (TextView)v.findViewById(R.id.byTime_grain_block);
    TextView oil_sugar = (TextView)v.findViewById(R.id.byTime_oil_block);

    if (color.equals("Protein")) {
    protein.setLayoutParams(
        new LinearLayout.LayoutParams(
          0, LayoutParams.FILL_PARENT, (float)value));
    }else if (color.equals("Vegetable")){
      vegetable.setLayoutParams(
          new LinearLayout.LayoutParams(
            0, LayoutParams.FILL_PARENT, (float)value));
    }else if(color.equals("Dairy")){
      dairy.setLayoutParams(new LinearLayout.LayoutParams(
          0, LayoutParams.FILL_PARENT, (float)value));
    } else if (color.equals("Fruit")) {
      fruit.setLayoutParams(new LinearLayout.LayoutParams(
            0, LayoutParams.FILL_PARENT, (float)value));
    } else if (color.equals("Grain")) {
      grain.setLayoutParams(new LinearLayout.LayoutParams(
            0, LayoutParams.FILL_PARENT, (float)value));
    } else if (color.equals("OilSugar")) {
      oil_sugar.setLayoutParams(new LinearLayout.LayoutParams(
            0, LayoutParams.FILL_PARENT, (float)value));
    }
  }
	
	/**
	 * Creates a hashmap of category names to values from the chart with the
	 * given index
	 */
	private HashMap<String, Double> getValueMap(int index) {
		HashMap<String, Double> valueMap = 
				new HashMap<String, Double>();
		ArrayList<HistoryBarChart> cats = averageAllSegments(index);
		
		for (HistoryBarChart cat : cats) {
			double percent = cat.getValue();
			if (percent == 0) {
				valueMap.put(cat.getCategory(), 1.00);
			}else {
				valueMap.put(cat.getCategory(), percent);
			}
		}
		return valueMap;
	}
//=======================================================================================

	/**
   * Creates an arrayList of segments that represents the averaged totals of
   * all segments - the sum of the value of the segments returned is 1.
   * god this is awful
   */
  public ArrayList<HistoryBarChart> averageAllSegments(int index) {
	//=========================================================
    // Keeping track of all the segments total percentages
	  Log.d(TAG, "## AverageAllSegment CALLED");
    HashMap<String, Double> segmentTotal = 
      new HashMap<String, Double>();
    // Count the number of segments by color
    HashMap<String, Integer> numberOfSegmentsWithColor =
      new HashMap<String, Integer>();

    // The hashmap to return, that contains the averages for each color
    HashMap<String, Double> segmentAverage = 
      new HashMap<String, Double>();
    //==========================================================
    
    // populate the counting map and the total map
    for (HistoryBarChart segment : getAllSegments(index)) { 
      // if we haven't added one of this color, initialize it with 0
      if (!numberOfSegmentsWithColor.containsKey(segment.getCategory())) {
        numberOfSegmentsWithColor.put(segment.getCategory(), 0);
      }
      // add 1 to the number of segments with this color
      numberOfSegmentsWithColor.put(segment.getCategory(), 
          numberOfSegmentsWithColor.get(segment.getCategory()) + 1);
      // add this segment's value to the total
      // if we havent added one of this color, initialize it with 0
      if (!segmentTotal.containsKey(segment.getCategory())) {
        segmentTotal.put(segment.getCategory(), (double)0);
      }
      // then/otherwise add the value
      segmentTotal.put(segment.getCategory(),
          segmentTotal.get(segment.getCategory()) + segment.getValue());
    }

    // Turn the totals into averages
    Iterator<HashMap.Entry<String, Double>> it = segmentTotal.entrySet().iterator();
    while (it.hasNext()) {
      HashMap.Entry<String, Double> pair = it.next();
      // put the average percentage in te segmentAverage map
      segmentAverage.put(pair.getKey(), 
          // compute the average percentage
          (pair.getValue() /
           numberOfSegmentsWithColor.get(pair.getKey())));
    }
    
    ArrayList<HistoryBarChart> segments = new ArrayList<HistoryBarChart>();
    // segmentAverage now has Color -> Average Percentage. 

    Iterator<Entry<String, Double>> iter = segmentAverage.entrySet().iterator();
    // add all of the average percentages to a list of segments
    while(iter.hasNext()) {
      Entry<String, Double> pair = iter.next();
      segments.add(new HistoryBarChart(pair.getValue(), pair.getKey()));
    }
    double total = 0;

    // compute the total for everything
    for (HistoryBarChart segment : segments) {
      total+=segment.getValue();
    }
    // using the total, compute the correct fraction for each average
    for (HistoryBarChart segment : segments) {
      double val = segment.getValue();
      segment.setValue(val / total);
    }
    // now each segment is a decimal representing it's percentage representation
    return segments;
  }

	public String getFileData(String filename) {
		String data = "error";
		try {
			FileInputStream fis = context.openFileInput(filename);
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
	
  private ArrayList<HistoryBarChart> getAllSegments(int index) {
    ArrayList<HistoryBarChart> segments = new ArrayList<HistoryBarChart>();
    for (Category cat : getAllDayCategoriesFromFiles(index)) {
      segments.add(new HistoryBarChart(cat));
    }
    return segments;
  }
  
	/**
	 * Returns an arrayList<Category> representation of the file data from 
	 * the given time index
	 * @param index The index of the item to get data for.
	 */  
	private ArrayList<Category> getAllDayCategoriesFromFiles(int index) {
		String[] savedFiles = context.fileList();
		String entry = History_byDate.formatEntry_FileType(items.get(index));
		for (String str : savedFiles) {
			Log.d(TAG, "@@@@ "+str);
		}
		ArrayList<Category> categories = new ArrayList<Category>();
		for (String filename : savedFiles) {
			if (filename.contains(entry));
			Log.d(TAG, "@@@@@@ "+filename);
			String fileData = getFileData(filename);
			// if we parsed the file correctly
			if (!fileData.equals("error")) {
				// add all the categories in the current file to the list
				categories.addAll(JSONParser.getCatListFromString(fileData, context));
			}
		}
		return categories;
	}
	
	
}

