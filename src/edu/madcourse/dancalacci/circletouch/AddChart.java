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

import org.json.JSONArray;

import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddChart extends Activity {
	/** Called when the activity is first created. */
	Chart_View cir;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.platechart_add);
		final Chart_View c = (Chart_View) this.findViewById(R.id.circle);
		cir = c;
	}

	public void onProteinClicked(View v){
		cir.onProteinClicked(v);
	}

	public void onVegetableClicked(View v){
		cir.onVegetableClicked(v);
	}

	public void onDairyClicked(View v){
		cir.onDairyClicked(v);
	}

	public void onFruitClicked(View v){
		cir.onFruitClicked(v);
	}

	public void onGrainClicked(View v){
		cir.onGrainClicked(v);
	}

	public void onOilSugarClicked(View v){
		cir.onOilSugarClicked(v);
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
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd");
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
				write.append(data+"\r\n"); //adds new line.
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
