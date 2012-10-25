package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;

import edu.madcourse.dancalacci.R;
import edu.madcourse.dancalacci.boggle.Multiplayer_HighScores.Multiplayer_HighScores_Adaptor;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Multiplayer_CurrentGames extends ListActivity {
	public static final String BOGGLE_PREF = "edu.madcourse.dancalacci.boggle";
	private static final String PREF_USER = "prefUser";
	ServerAccessor sa;
	String USERNAME;
	String TAG = "Multiplayer_CurrnetGames_Requests";
	//ArrayAdapter<String> adapter;
	Multiplayer_Current_Games_Adaptor adapter;

	public void setUsername(){
		Log.d(TAG, "setUsername");
		SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
		this.USERNAME = pref.getString(PREF_USER, null);
		Log.d(TAG, "setUsername: " + USERNAME);
	}

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.d(TAG, "set contentview");
		setContentView(R.layout.multiplayer_current_games);

		setUsername();

		getListView().setEmptyView(findViewById(android.R.id.empty));

		sa = new ServerAccessor();

		adapter = new Multiplayer_Current_Games_Adaptor(this, R.layout.multiplayer_current_games, this.generate_games_list());
		Log.d(TAG, "set adapter");

		setListAdapter(adapter);
	}

	public void onResume(){
		super.onResume();
		getListView().setEmptyView(findViewById(R.id.emptyView));
		adapter = new Multiplayer_Current_Games_Adaptor(this, R.layout.multiplayer_current_games, sa.getGames(USERNAME));
		setListAdapter(adapter);
	}

	/*
	 * TODO: Opens game based on user ID Key
	 * Sets ListItem Click Listener
	 * (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		//super.onListItemClick(l, v, position, id);
		String selection = l.getItemAtPosition(position).toString();
		Log.d(TAG, "onListItemClick: " + selection);
	}

	/* 
	 * Creates a list of Potential players based on Web Call
	 */
	private ArrayList<String> generate_games_list(){
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

		public void PlayerTurn(String currentPlayer, View view){
			String current_player = null;

			TextView player1 = (TextView) 
					view.findViewById(R.id.multiplayer_current_games_textView_player1);
			TextView player2 = (TextView) 
					view.findViewById(R.id.multiplayer_current_games_textView_player2);
			String P1 = player1.getText().toString();
			String P2 = player2.getText().toString();

			if (P1.equals(current_player)){
				player1.setBackgroundColor(getResources().getColor(R.color.boggle_boardColor));
				player2.setBackgroundColor(getResources().getColor(R.color.transparent));
			}else if(P2.equals(current_player)){
				player2.setBackgroundColor(getResources().getColor(R.color.boggle_boardColor));
				player1.setBackgroundColor(getResources().getColor(R.color.transparent));
			}


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
		Intent i = new Intent(this, Multiplayer_New_Request_Form.class);
		startActivity(i);
	}

	/*
	 * Returns to previous Activity
	 */
	public void onMultiplayersCurrentGamesBackButtonClicked(View v) {
		finish();
	}
}