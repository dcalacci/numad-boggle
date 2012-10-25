package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;

import edu.madcourse.dancalacci.R;
import edu.madcourse.dancalacci.boggle.Multiplayer_Received_Requests.Multiplayer_Received_Request_Adaptor;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Multiplayer_New_Request_Form extends ListActivity{
	private String TAG = "Multiplayer_New_Request_Form";
	ServerAccessor sa;
	String USERNAME = "user1";
	private String selectedUser = "";
	private ArrayList<String> mUserList = new ArrayList<String>();
	//Multiplayer_Send_Requests_Form_Adaptor adapter;

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate"); // log the event

		setContentView(R.layout.multiplayer_send_requests_form);

		getListView().setEmptyView(findViewById(android.R.id.empty));

		sa = new ServerAccessor();	

		// Creating an array adapter for listView
		mUserList = this.generate_user_list();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, mUserList);
		setListAdapter(adapter);

		// Defining the item click listener for listView
		OnItemClickListener itemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				selectedUser = mUserList.get(position);
				Toast.makeText(getBaseContext(), "You selected : " + mUserList.get(position), Toast.LENGTH_SHORT).show();
			}
		};

		// Setting the item click listener for listView
		getListView().setOnItemClickListener(itemClickListener);


	}

	/* 
	 * Creates a list of Potential players based on Web Call
	 */
	private ArrayList<String> generate_user_list(){
		Log.d(TAG, "request list: " + sa.getUserList().toString());
		ArrayList<String> temp = sa.getUserList();
		temp.remove(USERNAME);
		return temp;
	}

	public void onMultiplayerRequestsOkButtonClicked(View v) {
		if (selectedUser.length() > 0){
			sa.addRequest(USERNAME, selectedUser);
			finish();
		}else{
			Toast.makeText(getBaseContext(), "Please select a User", Toast.LENGTH_SHORT).show();
		}
	}

	public void onMultiplayerRequestsCancelButtonClicked(View v) {
		finish();
	}
}
