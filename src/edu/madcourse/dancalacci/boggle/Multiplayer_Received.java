package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;
import java.util.List;

import edu.madcourse.dancalacci.R;
import edu.madcourse.dancalacci.boggle.Multiplayer_Received_array_adaptor;
import edu.madcourse.dancalacci.boggle.Multiplayer_Received_row_content;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.SimpleAdapter;


public class Multiplayer_Received  extends ListActivity{
	private String TAG = "Multiplayer_Received";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
}

/*
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.d(TAG, "onCreate");
		ListView listView = (ListView) findViewById(R.id.multiplayer_requests_list_view);
		// Create an array of Strings, that will be put to our ListActivity
		ArrayAdapter<Model> adapter = new InteractiveArrayAdapter(this,
				getModel());
		setListAdapter(adapter);
		Log.d(TAG, "End of onCreate");
	}

	/*	
	public void onCreate(Bundle savedInstanceState) {
		// Create an array of Strings, that will be put to our ListActivity
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiplayer_received);

		Multiplayer_Received_array_adaptor rows = new Multiplayer_Received_array_adaptor( 
				this, 
				R.layout.multiplayer_received_rows,
				list );
		setListAdapter( rows );
		addUser("Bob");
		addUser("Jim");

	}


	private List<Model> getModel() {
		List<Model> list = new ArrayList<Model>();
		list.add(get("Linux"));
		list.add(get("Windows7"));
		list.add(get("Suse"));
		list.add(get("Eclipse"));
		list.add(get("Ubuntu"));
		list.add(get("Solaris"));
		list.add(get("Android"));
		list.add(get("iPhone"));
		// Initially select one of the items
		list.get(1).setSelected(true);
		return list;
	}

	private Model get(String s) {
		return new Model(s);
	}
	public void addUser(String username){
		list.add( new Multiplayer_Received_row_content( username ));
	}
 */
