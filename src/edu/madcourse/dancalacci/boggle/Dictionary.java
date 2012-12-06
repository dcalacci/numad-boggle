package edu.madcourse.dancalacci.boggle;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import edu.madcourse.dancalacci.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;


public class Dictionary extends Activity{
	private static final String TAG = "Dictionary";	
	
	public Dictionary() {
		super();
	}
	
	/** Searches through dictionary partitions*/
	public boolean searchDict(Context con, int length, String targetWord) throws IOException{
		Log.d(TAG,"searchDictionary");
		if (targetWord.isEmpty()) {
			   return false;
		   }
		   //int length = targetWord.length();
		   char c = targetWord.charAt(0);
		   AssetManager am = con.getResources().getAssets();
		   try {
			   InputStreamReader reader = 
				   new InputStreamReader(am.open(length + "/" + c + ".jpg"), "UTF-8");
			   BufferedReader br = new BufferedReader(reader); 
			   String line;
			   
			   line = br.readLine();
			   
			   while (line !=null) {
				   if (line.equals(targetWord)) {
					   return true;
				   } else {
					   line = br.readLine();
				   }
			   }
		   } catch (Exception e) {
			   Log.e("isWord", "Reading file", e);
		   }
		   //if it doesn't work, return false.
		   return false;
		/*
		 
		String firstLetter = "";
		AssetManager mngr = con.getResources().getAssets();
		if (targetWord != null || targetWord != ""){
			firstLetter = targetWord.substring(0, 1);
		}
		InputStream inStream = mngr.open(""+length+"/"+firstLetter+".txt");
		InputStreamReader inReader = new InputStreamReader(inStream);
		BufferedReader buffReader = new BufferedReader(inReader);
		String line = "";
		while((line = buffReader.readLine()) != null){
			if (line.equalsIgnoreCase(targetWord)){
				check = true;
			}
		}
		inStream.close();
		inReader.close();
		buffReader.close();
		return check;
		*/
	}

	
	

}
