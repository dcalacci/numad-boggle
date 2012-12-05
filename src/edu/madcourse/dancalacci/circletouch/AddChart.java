package edu.madcourse.dancalacci.circletouch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import org.json.JSONArray;
import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddChart extends Activity {
	private final String TAG = "circletouch.circle";
	private final String TAG_FROMHISTORY = "FROMHISTORY";
	private final String TAG_FROMCATEGORYSELECT = "FROMCATEGORYSELECT";
	/** Called when the activity is first created. */
	private Chart_View cir;
	private String mOrigin = "";
	private String fileName;
	private String static_data;
	private ArrayList<String> categories;

	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.platechart_add);
		
		Bundle extras = getIntent().getExtras();
		categories =  extras.getStringArrayList("categories");
		static_data = extras.getString("DATA");
		fileName = extras.getString("FileName");
		mOrigin = extras.getString("origin");
		final Chart_View c = (Chart_View) this.findViewById(R.id.circle);
		cir = c;

		cir.setmOrigin(mOrigin);
		
	}
	
	public void onResume(){
		super.onResume();
		Button deleteButton = (Button) this.findViewById(R.id.addChart_Delete);
		Button edit_addButton = (Button) this.findViewById(R.id.addChart_Save);

		if(mOrigin.equals(this.TAG_FROMHISTORY)){
			deleteButton.setVisibility(Button.VISIBLE);
			edit_addButton.setText(R.string.edit_label);
		}else if(mOrigin.equals(this.TAG_FROMCATEGORYSELECT)){
			deleteButton.setVisibility(Button.INVISIBLE);
			edit_addButton.setText(R.string.save_label);
		}

		// coming from categoryselection
		if(static_data == null){
			addCategories(categories);
		}
		// coming from history
		else{
			// get the array of categories from the extra
			ArrayList<Category> cats = 
					JSONParser.getCatListFromString(static_data, this);
			// set the circles' categories and touchpoints
			this.selectCategoryObjects(cats);
			cir.setmCategory(cats);
			cir.setmPoints(Category.getTouchPointsFromCategoryList(cats));
			cir.linkPointsAndCategories();
		}
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
		if (mOrigin.equals(this.TAG_FROMCATEGORYSELECT)) {
			cir.onProteinClicked(v);
		}
	}

	public void onVegetableClicked(View v){
		if (mOrigin.equals(this.TAG_FROMCATEGORYSELECT)) {
			cir.onVegetableClicked(v);
		}
	}

	public void onDairyClicked(View v){
		if (mOrigin.equals(this.TAG_FROMCATEGORYSELECT)) {
			cir.onDairyClicked(v);
		}
	}

	public void onFruitClicked(View v){
		if (mOrigin.equals(this.TAG_FROMCATEGORYSELECT)) {
			cir.onFruitClicked(v);
		}
	}

	public void onGrainClicked(View v){
		if (mOrigin.equals(this.TAG_FROMCATEGORYSELECT)) {
			cir.onGrainClicked(v);
		}
	}

	public void onOilSugarClicked(View v){
		if (mOrigin.equals(this.TAG_FROMCATEGORYSELECT)) {
			cir.onOilSugarClicked(v);
		}
	}

	public void onSaveClicked(View v){
		if(mOrigin.equals(this.TAG_FROMCATEGORYSELECT)){
			String date_time = getDateTime();
			Log.d(TAG, "is saving ?");
			//TODO: SAVE TO DISK
			JSONArray chartData = cir.getChartData();
			Log.d("TAG", chartData.toString());
			String data = parseData(chartData);
			save(data, fileName);
			/* cir.setmOrigin(this.TAG_FROMHISTORY); */
			/* mOrigin = this.TAG_FROMHISTORY; */
      /* onResume(); */
			finish(); // 
		}else if(mOrigin.equals(this.TAG_FROMHISTORY)){
			cir.setmOrigin(this.TAG_FROMCATEGORYSELECT);
			mOrigin = this.TAG_FROMCATEGORYSELECT;
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
		if(fileName != null){
			File file = new File(this.getFilesDir(), fileName);
			boolean deleted = file.delete();
		}
		finish();
	}

	/**
	 * Gets the current date and time based on the system settings/timezone
	 * @return String formattedDate
	 */
	public String getDateTime(){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");
		String formattedDate = df.format(c.getTime());

		return formattedDate;
	}
	/**
	 * Gets the Current date for file name
	 * @return String date - format YYYY_MM_DD
	 */
	public String getDate(){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH+mm+ss");
		String formattedDate = df.format(c.getTime());		
		return formattedDate;
	}

	/**
	 * Save file to internal memory
	 * @param data - Stored data
	 */
	public void save(String data, String FileName){
		String date = getDate();
		if(FileName == null){ 				// generates name of file
			FileName = date+".txt";
		}else if (FileName != null){ 		// Deletes current file
			File file = new File(this.getFilesDir(), FileName);
			boolean deleted = file.delete();
		}
		Log.d(TAG, "save() filename: " + FileName);
		try{			
			FileWriter write = new FileWriter(this.getFilesDir() + "/" + FileName, true);
			write.write(data+"\r\n"); //adds new line.
			write.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}	


}
