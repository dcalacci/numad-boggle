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
import android.widget.TextView;
import android.widget.Toast;

public class AddChart extends Activity {
  private final String TAG = "circletouch.circle";
	/** Called when the activity is first created. */
	Chart_View cir;
  boolean mCanEdit = true;
  
	public void onCreate(Bundle savedInstanceState) {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    Bundle extras = getIntent().getExtras();
    ArrayList<String> categories = extras.getStringArrayList("categories");
    mCanEdit = extras.getBoolean("canEdit");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.platechart_add);
		final Chart_View c = (Chart_View) this.findViewById(R.id.circle);
		cir = c;

    cir.setCanEdit(mCanEdit);
    addCategories(categories);
	}

  private void addCategories(ArrayList<String> categories) {
    for (String cat : categories) {
      addCategory(cat);
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
    } else if (category.equals("Oil/Sugar")) {
      final View oil_view = (View) this.findViewById(R.id.oil_view);
      onOilSugarClicked(oil_view);
    }
  }


	public void onProteinClicked(View v){
    if (mCanEdit) {
      cir.onProteinClicked(v);
    }
  }

	public void onVegetableClicked(View v){
    if (mCanEdit) {
      cir.onVegetableClicked(v);
    }
	}

	public void onDairyClicked(View v){
    if (mCanEdit) {
      cir.onDairyClicked(v);
    }
	}

	public void onFruitClicked(View v){
    if (mCanEdit) {
      cir.onFruitClicked(v);
    }
	}

	public void onGrainClicked(View v){
    if (mCanEdit) {
      cir.onGrainClicked(v);
    }
	}

	public void onOilSugarClicked(View v){
    if (mCanEdit) {
      cir.onOilSugarClicked(v);
    }
	}

	public void onSaveClicked(View v){
		String date_time = getDateTime();
		//TODO: SAVE TO DISK
		JSONArray chartData = cir.getChartData();
		Log.d("TAG", chartData.toString());
		String data = parseData(chartData);
		save(data);
		finish(); // 
	}
	
	public String parseData(JSONArray jArray){
		String data = this.getDateTime() + "_" + jArray.toString();
		return data;
	}

	public void onBackClicked(View v){
		finish();
	}

	/**
	 * Clears the chart and set the deselects for all categories
	 * @param v
	 */
	public void onClearClicked(View v){
		cir.clearChart();

		//Required due to the context of the circle view:

		//Protein
		cir.setProteinDeselect(this.findViewById(R.id.protein_view));

		//Vegetable
		cir.setVegetableDeselect(this.findViewById(R.id.vegetable_view));

		//Dairy
		cir.setDairyDeselect(this.findViewById(R.id.dairy_view));

		//Fruit
		cir.setFruitDeselect(this.findViewById(R.id.fruit_view));

		//Grain
		cir.setGrainDeselect(this.findViewById(R.id.grain_view));

		//Oil Sugar
		cir.setOilSugarDeselect(this.findViewById(R.id.oil_view));
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
	public void save(String data){
		String date = getDate();
		String fileName = date+".txt";

		try{
			FileWriter write = new FileWriter(this.getFilesDir() + "/" + fileName, true);
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
