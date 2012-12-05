package edu.madcourse.dancalacci.circletouch;

import java.util.ArrayList;

import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.TableRow;

public class CategorySelection extends Activity {
	private static final String PLATE_PREF = "edu.madcourse.dancalacci.circletouch";
	private static final String PREF_CATEGORY_INITIAL_RUN = "category_initial_run";

	ArrayList<String> mCategories = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no  titlebar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.platechart_select_categories);
	}

	public void onResume(){
		super.onResume();

		SharedPreferences pref = getSharedPreferences(PLATE_PREF, MODE_PRIVATE);
		boolean initial_run = pref.getBoolean(PREF_CATEGORY_INITIAL_RUN, false);

		if(initial_run == false){
			alerts();
		}
	}


	private void alerts(){

		SharedPreferences prefs = getSharedPreferences(PLATE_PREF, MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean(PREF_CATEGORY_INITIAL_RUN, true);
		editor.commit();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		// set title
		alertDialogBuilder.setTitle("Track a Meal");

		// set dialog message
		alertDialogBuilder
		.setMessage(getResources().getText(R.string.track_meal_instructions))
		.setCancelable(false)
		.setPositiveButton("Got it!",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity
				vibrate(25);
				dialog.dismiss();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}


	public void onNextClicked(View v) {
		vibrate(25);
		
		Intent i = new Intent(this, AddChart.class);
		// need to give AddChart the array of items selected.
		i.putExtra("categories", mCategories);
		i.putExtra(AddChart.TAG_ISEDITING, true);
		i.putExtra(AddChart.TAG_FILENAME, "");
		startActivity(i);

	}

	//--------
	// Protein
	// ------
	public void onProteinClicked(View v) {
		String category = "Protein";
		vibrate(25);
		boolean inList = mCategories.contains(category);
		if (inList) {
			setProteinDeselect(v);
			removeCategory(category);
		}else{
			setProteinSelect(v);
			addCategory(category);
		}
	}

	private void setProteinSelect(View v){
		TextView box = (TextView) v.findViewById(R.id.protein_cs_box);
		TextView text = (TextView) v.findViewById(R.id.protein_cs_label);
		TableRow row = (TableRow)this.findViewById(R.id.csTableRowProtein);

		box.setBackgroundColor(getResources().getColor(R.color.Protein));
		text.setTextColor(getResources().getColor(R.color.Protein));
		row.setBackgroundColor(getResources().getColor(R.color.Protein_Grayed));
	}

	private void setProteinDeselect(View v) {
		TextView box = (TextView) v.findViewById(R.id.protein_cs_box);
		TextView text = (TextView) v.findViewById(R.id.protein_cs_label);
		TableRow row = (TableRow)this.findViewById(R.id.csTableRowProtein);

		box.setBackgroundColor(getResources().getColor(R.color.Protein_Grayed));
		text.setTextColor(getResources().getColor(R.color.Protein_Grayed));
		row.setBackgroundColor(getResources().getColor(R.color.Chart_Background));
	}

	// ---------
	// Vegetable
	// ---------
	public void onVegetableClicked(View v) {
		String category = "Vegetable";
		vibrate(25);
		boolean inList = mCategories.contains(category);
		if (inList) {
			setVegetableDeselect(v);
			removeCategory(category);
		}else{
			setVegetableSelect(v);
			addCategory(category);
		}
	}

	private void setVegetableSelect(View v){
		TextView box = (TextView) v.findViewById(R.id.vegetable_cs_box);
		TextView text = (TextView) v.findViewById(R.id.vegetable_cs_label);
		TableRow row = (TableRow)this.findViewById(R.id.csTableRowVegetable);

		box.setBackgroundColor(getResources().getColor(R.color.Vegetable));
		text.setTextColor(getResources().getColor(R.color.Vegetable));
		row.setBackgroundColor(getResources().getColor(R.color.Vegetable_Grayed));
	}

	private void setVegetableDeselect(View v) {

		TextView box = (TextView) v.findViewById(R.id.vegetable_cs_box);
		TextView text = (TextView) v.findViewById(R.id.vegetable_cs_label);
		TableRow row = (TableRow)this.findViewById(R.id.csTableRowVegetable);

		box.setBackgroundColor(getResources().getColor(R.color.Vegetable_Grayed));
		text.setTextColor(getResources().getColor(R.color.Vegetable_Grayed));
		row.setBackgroundColor(getResources().getColor(R.color.Chart_Background));
	}

	// --------
	// Dairy
	// --------
	public void onDairyClicked(View v) {
		String category = "Dairy";
		vibrate(25);
		boolean inList = mCategories.contains(category);
		if (inList) {
			setDairyDeselect(v);
			removeCategory(category);
		}else{
			setDairySelect(v);
			addCategory(category);
		}
	}

	private void setDairySelect(View v){
		TextView box = (TextView) v.findViewById(R.id.dairy_cs_box);
		TextView text = (TextView) v.findViewById(R.id.dairy_cs_label);
		TableRow row = (TableRow)this.findViewById(R.id.csTableRowDairy);

		box.setBackgroundColor(getResources().getColor(R.color.Dairy));
		text.setTextColor(getResources().getColor(R.color.Dairy));
		row.setBackgroundColor(getResources().getColor(R.color.Dairy_Grayed));
	}

	private void setDairyDeselect(View v) {

		TextView box = (TextView) v.findViewById(R.id.dairy_cs_box);
		TextView text = (TextView) v.findViewById(R.id.dairy_cs_label);
		TableRow row = (TableRow)this.findViewById(R.id.csTableRowDairy);

		box.setBackgroundColor(getResources().getColor(R.color.Dairy_Grayed));
		text.setTextColor(getResources().getColor(R.color.Dairy_Grayed));
		row.setBackgroundColor(getResources().getColor(R.color.Chart_Background));
	}

	// --------
	// Fruit
	// --------
	public void onFruitClicked(View v) {
		String category = "Fruit";
		vibrate(25);
		boolean inList = mCategories.contains(category);
		if (inList) {
			setFruitDeselect(v);
			removeCategory(category);
		}else{
			setFruitSelect(v);
			addCategory(category);
		}
	}

	private void setFruitSelect(View v){
		TextView box = (TextView) v.findViewById(R.id.fruit_cs_box);
		TextView text = (TextView) v.findViewById(R.id.fruit_cs_label);
		TableRow row = (TableRow)this.findViewById(R.id.csTableRowFruit);

		box.setBackgroundColor(getResources().getColor(R.color.Fruit));
		text.setTextColor(getResources().getColor(R.color.Fruit));
		row.setBackgroundColor(getResources().getColor(R.color.Fruit_Grayed));
	}

	private void setFruitDeselect(View v) {

		TextView box = (TextView) v.findViewById(R.id.fruit_cs_box);
		TextView text = (TextView) v.findViewById(R.id.fruit_cs_label);
		TableRow row = (TableRow)this.findViewById(R.id.csTableRowFruit);

		box.setBackgroundColor(getResources().getColor(R.color.Fruit_Grayed));
		text.setTextColor(getResources().getColor(R.color.Fruit_Grayed));
		row.setBackgroundColor(getResources().getColor(R.color.Chart_Background));
	}


	// --------
	// Grain
	// --------
	public void onGrainClicked(View v) {
		String category = "Grain";
		vibrate(25);
		boolean inList = mCategories.contains(category);
		if (inList) {
			setGrainDeselect(v);
			removeCategory(category);
		}else{
			setGrainSelect(v);
			addCategory(category);
		}
	}

	private void setGrainSelect(View v){
		TextView box = (TextView) v.findViewById(R.id.grain_cs_box);
		TextView text = (TextView) v.findViewById(R.id.grain_cs_label);
		TableRow row = (TableRow)this.findViewById(R.id.csTableRowGrain);

		box.setBackgroundColor(getResources().getColor(R.color.Grain));
		text.setTextColor(getResources().getColor(R.color.Grain));
		row.setBackgroundColor(getResources().getColor(R.color.Grain_Grayed));
	}

	private void setGrainDeselect(View v) {

		TextView box = (TextView) v.findViewById(R.id.grain_cs_box);
		TextView text = (TextView) v.findViewById(R.id.grain_cs_label);
		TableRow row = (TableRow)this.findViewById(R.id.csTableRowGrain);

		box.setBackgroundColor(getResources().getColor(R.color.Grain_Grayed));
		text.setTextColor(getResources().getColor(R.color.Grain_Grayed));
		row.setBackgroundColor(getResources().getColor(R.color.Chart_Background));
	}

	// --------
	// Oil/Sugar
	// --------
	public void onOilSugarClicked(View v) {
		String category = "OilSugar";
		vibrate(25);
		boolean inList = mCategories.contains(category);
		if (inList) {
			setOilDeselect(v);
			removeCategory(category);
		}else{
			setOilSelect(v);
			addCategory(category);
		}
	}

	private void setOilSelect(View v){
		TextView box = (TextView) v.findViewById(R.id.oil_cs_box);
		TextView text = (TextView) v.findViewById(R.id.oil_cs_label);
		TableRow row = (TableRow)this.findViewById(R.id.csTableRowOil);

		box.setBackgroundColor(getResources().getColor(R.color.Oil_Sugar));
		text.setTextColor(getResources().getColor(R.color.Oil_Sugar));
		row.setBackgroundColor(getResources().getColor(R.color.Oil_Sugar_Grayed));
	}

	private void setOilDeselect(View v) {
		TextView box = (TextView) v.findViewById(R.id.oil_cs_box);
		TextView text = (TextView) v.findViewById(R.id.oil_cs_label);
		TableRow row = (TableRow)this.findViewById(R.id.csTableRowOil);

		box.setBackgroundColor(getResources().getColor(R.color.Oil_Sugar_Grayed));
		text.setTextColor(getResources().getColor(R.color.Oil_Sugar_Grayed));
		row.setBackgroundColor(getResources().getColor(R.color.Chart_Background));
	}

  private void vibrate(int ms) {
    Vibrator v = 
      (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    v.vibrate(ms);
  }
	
	
	public void removeCategory(String category){
		mCategories.remove(category);
	}

	public void addCategory(String category){
		mCategories.add(category);
	}

}
