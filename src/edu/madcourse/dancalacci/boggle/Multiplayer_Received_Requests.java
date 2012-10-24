package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;
import java.util.List;

import edu.madcourse.dancalacci.R;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
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
	ServerAccessor sa;
	String USERNAME = "user1";
	Multiplayer_Received_Request_Adaptor adapter;

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.d(TAG, "set contentview");
		setContentView(R.layout.multiplayer_received);



		sa = new ServerAccessor();
		adapter = new Multiplayer_Received_Request_Adaptor(this, R.layout.multiplayer_received, this.generate_request_list());
		Log.d(TAG, "set adapter");
		/*adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				this.generate_request_list());
		 */
		setListAdapter(adapter);
	}

	private ArrayList<String> generate_request_list(){
		sa.addRequest("user1", "user2");
		Log.d(TAG, "request list: " + sa.getRequests(USERNAME).toString());
		return sa.getRequests(USERNAME);
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
			if(view == null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.multiplayer_received_rows, parent, false);
			}
			
			TextView username = (TextView) 
					view.findViewById(R.id.multiplayer_received_requests_textView_content);
			username.setText(mReceivedRequests.get(position));
			// Give it a nice background
			return view;
		}			
		
		// TODO: add ACCEPT button listener
		// TODO: add ACCEPT button listener

	}
}
