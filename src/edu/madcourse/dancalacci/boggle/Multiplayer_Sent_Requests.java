package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;

import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

public class Multiplayer_Sent_Requests extends ListActivity{

	ServerAccessor sa;
	String USERNAME = "user1";
	String TAG = "Multiplayer_Sent_Requests";
	Multiplayer_Sent_Request_Adaptor adapter;

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.d(TAG, "set contentview");
		setContentView(R.layout.multiplayer_sent);
		
		
		
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

	/* 
	 * Creates a list of Potential players based on Web Call
	 */
	private ArrayList<String> generate_sent_request_list(){
		sa.addRequest("user1", "user2");
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

		
		public View getView(int position, View view, ViewGroup parent) {
			
			if(view == null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.multiplayer_sent_rows, parent, false);
			}
			
			TextView username = (TextView) 
					view.findViewById(R.id.multiplayer_sent_requests_textView_content);
			username.setText(mSentRequests.get(position));
			// Give it a nice background
			return view;
		}			
		
		//TODO onClick listener for Delete button


	}

	/*	
	 * Starts new Multiplayer Boggle Game	 
	 */
	public void onMultiplayerSentRequestsSendNewRequestButtonClicked(View v) {

		//Intent i = new Intent(this, .class);
		//startActivity(i);
	}

	public void onMultiplayerSentRequestsBackButtonClicked(View v) {
		finish();
	}
}
