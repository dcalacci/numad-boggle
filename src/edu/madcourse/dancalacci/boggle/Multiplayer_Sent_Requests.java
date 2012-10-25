package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;

import edu.madcourse.dancalacci.R;
import edu.madcourse.dancalacci.boggle.Multiplayer_Received_Requests.Multiplayer_Received_Request_Adaptor;
import edu.madcourse.dancalacci.boggle.Multiplayer_Received_Requests.Multiplayer_Received_Request_Adaptor.buttonClickHandler;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Multiplayer_Sent_Requests extends ListActivity{
	private static final String BOGGLE_PREF = "edu.madcourse.dancalacci.boggle";
	private static final String PREF_USER = "prefUser";
	private ServerAccessor sa;
	private String USERNAME;
	private String TAG = "Multiplayer_Sent_Requests";
	Multiplayer_Sent_Request_Adaptor adapter;

	public void setUsername(){
		SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
		USERNAME = pref.getString(PREF_USER, null);
	}

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.d(TAG, "set contentview");

		setUsername();

		setContentView(R.layout.multiplayer_sent);

		getListView().setEmptyView(findViewById(android.R.id.empty));

		sa = new ServerAccessor();
		adapter = new Multiplayer_Sent_Request_Adaptor(this, R.layout.multiplayer_sent, this.generate_sent_request_list());
		Log.d(TAG, "set adapter");
		/*
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				this.generate_request_list());
		 */
		setListAdapter(adapter);
	}

	public void onResume(){
		super.onResume();		
		getListView().setEmptyView(findViewById(R.id.emptyView));
		adapter = new Multiplayer_Sent_Request_Adaptor(this, R.layout.multiplayer_sent, sa.getRequests(USERNAME));
		setListAdapter(adapter);
	}

	/* 
	 * Creates a list of Potential players based on Web Call
	 */
	private ArrayList<String> generate_sent_request_list(){
		Log.d(TAG, "request list username: " + USERNAME);
		Log.d(TAG, "request list: " + sa.getRequests(USERNAME).toString());
		return sa.getRequests(USERNAME);
	}


	public class Multiplayer_Sent_Request_Adaptor extends BaseAdapter{
		private ArrayList<String> mSentRequests = new ArrayList<String>();
		private Context mContext;
		private int rowResID;

		public Multiplayer_Sent_Request_Adaptor(Context c, int rowResID, ArrayList<String> sentRequestsList){
			this.rowResID = rowResID;
			this.mContext = c;
			this.mSentRequests = sentRequestsList;
		}


		public int getCount() {
			// TODO Auto-generated method stub
			return mSentRequests.size();
		}


		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}


		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		public boolean isEmptyList(){
			return mSentRequests.contains("");
		}

		public View getView(int position, View view, ViewGroup parent) {
			final String row = this.mSentRequests.get(position);
			//boolean isEmpty = isEmptyList();
			if(view == null){
				getListView().setEmptyView(findViewById(android.R.id.empty));

				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.multiplayer_sent_rows, parent, false);
			}
			//Log.d(TAG, "Empty ArrayList "+ isEmpty);
			
			TextView username = (TextView) 
					view.findViewById(R.id.multiplayer_sent_requests_textView_content);
			username.setText(mSentRequests.get(position));

			buttonClickHandler btn_Handler = new buttonClickHandler(username, row);

			Button btnDelete = (Button) view.findViewById(R.id.multiplayer_sent_delete_button);
			btnDelete.setOnClickListener(btn_Handler);
			Log.d(TAG,"mSentRequests list: "+ mSentRequests.toString());
			Log.d(TAG,"mSentRequests row: "+ row);
			btnDelete.setTag(row);

			// Give it a nice background
			return view;
		}			

		public void deleteRow(String row) {
			if(this.mSentRequests.contains(row)) {
				this.mSentRequests.remove(row);
			}
		}

		class buttonClickHandler implements View.OnClickListener {
			TextView textView;
			String row;

			public buttonClickHandler(TextView textView, String row ) {
				this.textView = textView;
				this.row = row;
			}

			public void onClick(View v) {
				Button button = (Button) v;
				String row = (String) button.getTag();

				switch(v.getId()){
				case R.id.multiplayer_sent_delete_button:
					//Start new game activity
					//TODO: Update Request List & Create new game pair -> Server Call
					Log.d(TAG, "Delete Button Clicked");
					sa.removeRequest(USERNAME, row);
					Log.d(TAG, "updated list after delete: "+sa.getRequests(USERNAME).toString());
					deleteRow(row);
					notifyDataSetChanged();
					Log.d(TAG, "Delete Button Clicked Delete Row");
					break;
				}
			}

		}

	}

	/*	
	 * Starts new Multiplayer Boggle Game	 
	 */
	public void onMultiplayerSentRequestsSendNewRequestButtonClicked(View v) {
		Intent i = new Intent(this, Multiplayer_New_Request_Form.class);
		startActivity(i);
	}

	public void onMultiplayerSentRequestsBackButtonClicked(View v) {
		finish();
	}
}
