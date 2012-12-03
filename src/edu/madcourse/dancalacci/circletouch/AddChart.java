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
  private Chart_View cir;
  private boolean mCanEdit = true;
  private String fileName;

  public void onCreate(Bundle savedInstanceState) {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    Bundle extras = getIntent().getExtras();
    ArrayList<String> categories = extras.getStringArrayList("categories");
    String static_data = extras.getString("DATA");

    mCanEdit = extras.getBoolean("canEdit");

    super.onCreate(savedInstanceState);
    setContentView(R.layout.platechart_add);
    final Chart_View c = (Chart_View) this.findViewById(R.id.circle);
    cir = c;

    cir.setCanEdit(mCanEdit);

    // coming from categoryselection
    if(static_data == null){
      addCategories(categories);
    }
    // coming from history
    else{
      fileName = extras.getString("FileName");
      // get the array of categories from the extra
      ArrayList<Category> cats = 
        JSONParser.getCatListFromString(static_data, this);
      // set the circles' categories and touchpoints
      this.selectCategoryObjects(cats);
      cir.setmCategory(cats);
      cir.setmPoints(Category.getTouchPointsFromCategoryList(cats));
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
