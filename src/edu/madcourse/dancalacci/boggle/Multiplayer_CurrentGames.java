package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;

import edu.madcourse.dancalacci.R;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Multiplayer_CurrentGames extends ListActivity{
	ServerAccessor sa;
	String USERNAME = "user1";							// Change to PREF
	String TAG = "Multiplayer_CurrnetGames_Requests";
	//ArrayAdapter<String> adapter;
	Multiplayer_Current_Games_Adaptor adapter;
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.d(TAG, "set contentview");
		setContentView(R.layout.multiplayer_current_games);

		sa = new ServerAccessor();

		adapter = new Multiplayer_Current_Games_Adaptor(this, R.layout.multiplayer_current_games, this.generate_games_list());
		Log.d(TAG, "set adapter");
		/*
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				this.generate_games_list());
		 */
		
		//TODO: add click listener to rows 
		
		setListAdapter(adapter);
	}
	
	

	/* 
	 * Creates a list of Potential players based on Web Call
	 */
	private ArrayList<String> generate_games_list(){
		sa.addRequest("user1", "user2");
		Log.d(TAG, "request list: " + sa.getGames(USERNAME).toString());
		return sa.getGames(USERNAME);
	}


	public class Multiplayer_Current_Games_Adaptor extends BaseAdapter{
		private ArrayList<String> mGames = new ArrayList<String>();
		private Context mContext;
		private int rowResID;

		public Multiplayer_Current_Games_Adaptor(Context c, int rowResID, ArrayList<String> currentGamesList) {
			this.rowResID = rowResID;
			mContext = c;
			mGames = currentGamesList;
		}

		
		public int getCount() {
			// TODO Auto-generated method stub
			return mGames.size();
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
				view = inflater.inflate(R.layout.multiplayer_current_games_rows, parent, false);
			}

			TextView player1 = (TextView) 
					view.findViewById(R.id.multiplayer_current_games_textView_player1);
			player1.setText(USERNAME);

			TextView player2 = (TextView) 
					view.findViewById(R.id.multiplayer_current_games_textView_player2);
			player2.setText(mGames.get(position));

			//view.setOnClickListener(new OnClickListener(position));

			return view;
		}			

		public void onClick(View v) {
			Log.v(TAG, "Row button clicked");
		}

		/**
		 * The click listener base class.
		 */
		public class OnClickListener implements View.OnClickListener {
			private String TAG = "Row Click";
			private int mPosition;

			OnClickListener(int position){
				mPosition = position;
			}
			
			public void onClick(View v) {
				Log.v(TAG, "onItemClick at position " + mPosition);          
			}       
		}


	}

	/*	
	 * Starts new Multiplayer Boggle Game	 
	 */
	public void onMultiplayerCurrentGamesSendNewRequestsButtonClicked(View v) {

		//Intent i = new Intent(this, .class);
		//startActivity(i);
	}

	public void onMultiplayersCurrentGamesBackButtonClicked(View v) {
		finish();
	}
}
