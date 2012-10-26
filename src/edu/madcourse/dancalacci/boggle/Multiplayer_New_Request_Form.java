package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;

import edu.madcourse.dancalacci.R;
import edu.madcourse.dancalacci.boggle.Multiplayer_Received_Requests.Multiplayer_Received_Request_Adaptor;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
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
	private static final String BOGGLE_PREF = "edu.madcourse.dancalacci.multiplayer";
	private static final String PREF_USER = "prefUser";
	private ServerAccessor sa;
	private String USERNAME;
	private String selectedUser = null;
	private ArrayList<String> mUserList = new ArrayList<String>();
	ArrayAdapter<String> adapter;

	public void setUsername(){
		SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
		USERNAME = pref.getString(PREF_USER, null);
	}

	public void sendRequest(String user1, String user2) {

		final Multiplayer_New_Request_Form thisActivity = this;
		final String user1f = user1;
		final String user2f = user2;

		sa.sendRequest(user1, user2, new OnBooleanReceivedListener() {

			public void run(Boolean exitState) {
				if (!exitState) {
					thisActivity.sendRequest(user1f, user2f, 2);
				} else {
					Log.d(TAG, "Requst ran successfully, sending confirmation toast");
					Toast.makeText(getBaseContext(), 
							"Request sent to " +user2f+"!", 
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void sendRequest(String user1, String user2, final int count) {

		final Multiplayer_New_Request_Form thisActivity = this;
		final String user1f = user1;
		final String user2f = user2;

		sa.sendRequest(user1, user2, new OnBooleanReceivedListener() {

			public void run(Boolean exitState) {
				if (!exitState && count != 0) {
					Log.d(TAG, "trying for the " + (3-count) +"th time");
					thisActivity.sendRequest(user1f, user2f, count - 1);
				} else if(!exitState && count == 0) {
					Log.e(TAG, "Couldn't send the request.");
					Log.d(TAG, "Sending error toast!");
					Toast.makeText(getBaseContext(), 
							"Sorry, I couldn't send the request. Have you already sent one to this user?", 
							Toast.LENGTH_LONG).show();
				} else {
					Log.d(TAG, "Requst ran successfully, sending confirmation toast");
					Toast.makeText(getBaseContext(), 
							"Request sent to " +user2f+"!", 
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate"); // log the event

		setUsername();

		setContentView(R.layout.multiplayer_send_requests_form);

		getListView().setEmptyView(findViewById(android.R.id.empty));

		sa = new ServerAccessor();	

		// Creating an array adapter for listView
	//	mUserList = this.generate_user_list();
		
		final Multiplayer_New_Request_Form thisActivity = this;
		sa.getUserList(new OnStringArrayListLoadedListener() {
			
			public void run(ArrayList<String> list) {
				if (list.get(0).startsWith("ERROR")) {
					Toast.makeText(getBaseContext(), 
							"Cannot access the user list.", 
							Toast.LENGTH_SHORT).show();
					finish();
				} 
				list.remove(USERNAME);
				adapter = new ArrayAdapter<String>(thisActivity, android.R.layout.simple_list_item_single_choice, list);
				setListAdapter(adapter);
			}
		});

		// Defining the item click listener for listView
		OnItemClickListener itemClickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				selectedUser = mUserList.get(position);
				Toast.makeText(getBaseContext(), "You selected : " + mUserList.get(position), Toast.LENGTH_SHORT).show();
			}
		};

		// Setting the item click listener for listView
		getListView().setOnItemClickListener(itemClickListener);


	}

	public void onMultiplayerRequestsOkButtonClicked(View v) {
		Log.d(TAG, "requestOkButton: " + selectedUser);
		if (!(selectedUser == null)){

			this.sendRequest(USERNAME, selectedUser);

			finish();
		}else{
			Toast.makeText(getBaseContext(), "Please select a User", Toast.LENGTH_SHORT).show();
		}
	}

	public void onMultiplayerRequestsCancelButtonClicked(View v) {
		finish();
	}
}
