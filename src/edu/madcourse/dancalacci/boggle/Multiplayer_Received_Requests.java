package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;
import java.util.List;

import edu.madcourse.dancalacci.R;
import edu.neu.mobileclass.apis.KeyValueAPI;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter;


public class Multiplayer_Received_Requests  extends ListActivity{
	private String TAG = "Multiplayer_Received_Requests";
	private static final String BOGGLE_PREF = "edu.madcourse.dancalacci.boggle";
	private static final String PREF_USER = "prefUser";
	private ServerAccessor sa;
	private String USERNAME;
	Multiplayer_Received_Request_Adaptor adapter;

	public void setUsername(){
		SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
		USERNAME = pref.getString(PREF_USER, null);
	}

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.d(TAG, "set contentview");
		setContentView(R.layout.multiplayer_received);

		setUsername();

		getListView().setEmptyView(findViewById(android.R.id.empty));

		sa = new ServerAccessor();
		adapter = new Multiplayer_Received_Request_Adaptor(this, R.layout.multiplayer_received, this.generate_received_request_list());
		Log.d(TAG, "set adapter");
		/*adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				this.generate_request_list());
		 */
		setListAdapter(adapter);
	}

	public void onResume(){
		super.onResume();
		getListView().setEmptyView(findViewById(R.id.emptyView));
		adapter = new Multiplayer_Received_Request_Adaptor(this, R.layout.multiplayer_received, sa.getReceivedRequests(USERNAME));
		setListAdapter(adapter);
	}

	private ArrayList<String> generate_received_request_list(){
		Log.v(TAG, "starting a GetKeyTask");
		class generateReceivedRequestsTask extends AsyncTask<String, Integer, ArrayList<String>> {

			@Override
			protected ArrayList<String> doInBackground(String... unused) {
				Log.d(TAG, "request list: " + sa.getReceivedRequests(USERNAME).toString());
				return sa.getReceivedRequests(USERNAME);
			}
		}

		try {
			//Log.v(TAG, "executing getKeyTask with '" +key +"'");
			return new generateReceivedRequestsTask().execute().get();
		} catch(Exception e) {
			//Log.e(TAG, "GetKeyTask thread died: " +e);
			return null;
		}



	}


	public class Multiplayer_Received_Request_Adaptor extends BaseAdapter{
		private ArrayList<String> mReceivedRequests = new ArrayList<String>();
		private Context mContext;
		private int rowResID;

		public Multiplayer_Received_Request_Adaptor(Context c, int rowResID, ArrayList<String> receivedRequestsList) {
			mContext = c;
			mReceivedRequests = receivedRequestsList;
			this.rowResID = rowResID;
		}


		public int getCount() {
			// TODO Auto-generated method stub
			return mReceivedRequests.size();
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
			return mReceivedRequests.contains("");
		}

		public View getView(int position, View view, ViewGroup parent) {
			final String row = this.mReceivedRequests.get(position);
			boolean isEmpty = isEmptyList();

			if(view == null ){
				getListView().setEmptyView(findViewById(android.R.id.empty));
				if(!isEmpty){
					LayoutInflater inflater = LayoutInflater.from(parent.getContext());
					view = inflater.inflate(R.layout.multiplayer_received_rows, parent, false);
				}else{
					LayoutInflater inflater = LayoutInflater.from(parent.getContext());
					view = inflater.inflate(R.layout.multiplayer_received_rows_empty, parent, false);
				}
			}

			TextView username = (TextView) 
					view.findViewById(R.id.multiplayer_received_requests_textView_content);
			if(!isEmpty){
				username.setText(mReceivedRequests.get(position));
			}else{
				username.setText("No New Requests");
			}

			// Give it a nice background

			buttonClickHandler btn_Handler = new buttonClickHandler(username, row);

			Button btnAccept = (Button) view.findViewById(R.id.multiplayer_received_accept_button);
			btnAccept.setOnClickListener(btn_Handler);
			btnAccept.setTag(row);

			Button btnReject = (Button) view.findViewById(R.id.multiplayer_received_reject_button);
			btnReject.setOnClickListener(btn_Handler);
			btnReject.setTag(row);

			return view;
		}			


		/**
		 * Delete a row from the list of rows
		 * @param row row to be deleted
		 */
		public void deleteRow(String row) {
			if(this.mReceivedRequests.contains(row)) {
				this.mReceivedRequests.remove(row);
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
				case R.id.multiplayer_received_accept_button:
					Log.d(TAG, "Accept Button Clicked");
					sa.addGame(USERNAME, row);

					/* Removes Player2 from Player1 list */
					sa.removeSentRequest(USERNAME, row);

					/* Removes Player1 from Player2 List */
					sa.removeSentRequest(row, USERNAME);

					deleteRow(row);
					notifyDataSetChanged();

					//Start new game activity
					//TODO: Update Request List & Create new game pair -> Server Call
					//TODO: Send user2 name to activity
					Intent i = new Intent(mContext, Multiplayer_Game.class);
					sa.sendRequest("user1", "user2");
					// should be row but...  i.putExtra("opponent", row);
					i.putExtra("opponent", "user2");
					startActivity(i);

					Log.d(TAG, "Accept Button Clicked Delete Row");
					break;
				case R.id.multiplayer_received_reject_button:
					//TODO: Update Request List -> Server Call
					Log.d(TAG, "Reject Button Clicked");
					sa.removeSentRequest(USERNAME, row);
					deleteRow(row);
					notifyDataSetChanged();
					Log.d(TAG, "Reject Button Clicked Delete Row");
					break;
				}
			}


		}

		// TODO: add ACCEPT button listener
		// TODO: add ACCEPT button listener

	}


	public void onClick(View v) {
		Button button = (Button) v;
		String row = (String) button.getTag();

		switch(v.getId()){
		case R.id.multiplayer_received_accept_button:
			//Start new game activity
			//TODO: Update Request List & Create new game pair -> Server Call
			Log.d(TAG, "Accept Button Clicked");
			if(!row.equals(null)){
				sa.createNewGame(USERNAME, row);
			}
			adapter.deleteRow(row);
			adapter.notifyDataSetChanged();
			break;
		case R.id.multiplayer_received_reject_button:
			//TODO: Update Request List -> Server Call
			Log.d(TAG, "Reject Button Clicked");
			adapter.deleteRow(row);
			adapter.notifyDataSetChanged();
			break;
		}


	}

	public void onMultiplayerReceivedRequestsSendNewRequestsButtonClicked(View v) {
		Intent i = new Intent(this, Multiplayer_New_Request_Form.class);
		startActivity(i);
	}

	public void onMultiplayersReceivedRequestsBackButtonClicked(View v) {
		finish();
	}
}
