package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;
import java.util.List;

import edu.madcourse.dancalacci.R;
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
		adapter = new Multiplayer_Received_Request_Adaptor(this, R.layout.multiplayer_received, this.generate_request_list());
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
		adapter = new Multiplayer_Received_Request_Adaptor(this, R.layout.multiplayer_received, sa.getReceived(USERNAME));
		setListAdapter(adapter);
	}

	private ArrayList<String> generate_request_list(){
		Log.d(TAG, "request list: " + sa.getReceived(USERNAME).toString());
		return sa.getReceived(USERNAME);
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


		public View getView(int position, View view, ViewGroup parent) {
			final String row = this.mReceivedRequests.get(position);

			if(view == null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.multiplayer_received_rows, parent, false);
			}

			TextView username = (TextView) 
					view.findViewById(R.id.multiplayer_received_requests_textView_content);
			username.setText(mReceivedRequests.get(position));
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
					Intent i = new Intent(mContext, Multiplayer_Game.class);
					sa.addRequest("user1", "user2");
					// should be row but...  i.putExtra("opponent", row);
					i.putExtra("opponent", "user2");
					startActivity(i);
					//Start new game activity
					//TODO: Update Request List & Create new game pair -> Server Call
					//TODO: Send user2 name to activity
					Log.d(TAG, "Accept Button Clicked");
					sa.removeReceived(USERNAME, row);
					sa.addGame(USERNAME, row);
					deleteRow(row);
					notifyDataSetChanged();
					Log.d(TAG, "Accept Button Clicked Delete Row");
					break;
				case R.id.multiplayer_received_reject_button:
					//TODO: Update Request List -> Server Call
					Log.d(TAG, "Reject Button Clicked");
					sa.removeReceived(USERNAME, row);
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
