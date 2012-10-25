package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;

import edu.madcourse.dancalacci.R;
import edu.madcourse.dancalacci.boggle.Multiplayer_CurrentGames.Multiplayer_Current_Games_Adaptor;
import edu.madcourse.dancalacci.boggle.Multiplayer_Sent_Requests.Multiplayer_Sent_Request_Adaptor;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Multiplayer_HighScores extends ListActivity{
	private static final String BOGGLE_PREF = "edu.madcourse.dancalacci.boggle";
	private static final String PREF_USER = "prefUser";
	private ServerAccessor sa;
	private String USERNAME;
	String TAG = "Multiplayer_HighScores_Requests";
	Multiplayer_HighScores_Adaptor adapter;

	public void setUsername(){
		SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
		USERNAME = pref.getString(PREF_USER, null);
	}

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.d(TAG, "set contentview");
		setContentView(R.layout.multiplayer_highscores);

		SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
		USERNAME = pref.getString(PREF_USER, null);

		getListView().setEmptyView(findViewById(android.R.id.empty));

		sa = new ServerAccessor();
		adapter = new Multiplayer_HighScores_Adaptor(this, R.layout.multiplayer_highscores, this.generate_highscores_list());
		/*
		Log.d(TAG, "set adapter");
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				this.generate_request_list());
		 */
		setListAdapter(adapter);
	}

	public void onResume(){
		super.onResume();
		SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
		USERNAME = pref.getString(PREF_USER, null);

		getListView().setEmptyView(findViewById(R.id.emptyView));
		adapter = new Multiplayer_HighScores_Adaptor(this, R.layout.multiplayer_sent, generate_highscores_list());
		setListAdapter(adapter);
	}

	/* 
	 * TODO: Change to GET High Scores 
	 * Creates a list of Potential players based on Web Call
	 */
	private ArrayList<String> generate_highscores_list(){
		Log.d(TAG, "request list: " + sa.getSentRequests(USERNAME).toString());
		return sa.getSentRequests(USERNAME);
	}

	public class Multiplayer_HighScores_Adaptor extends BaseAdapter{
		private ArrayList<String> mHighScores = new ArrayList<String>();
		private Context mContext;
		private int rowResID;

		public Multiplayer_HighScores_Adaptor(Context c, int rowResID, ArrayList<String> highscoresList){
			this.mContext = c;
			this.mHighScores = highscoresList;
			this.rowResID = rowResID;
		}


		public int getCount() {
			// TODO Auto-generated method stub
			return mHighScores.size();
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
				view = inflater.inflate(R.layout.multiplayer_highscores_rows, parent, false);
			}


			TextView username = (TextView) 
					view.findViewById(R.id.multiplayer_high_scores_textView_player);
			username.setText(mHighScores.get(position));

			//TODO: change to actual scores
			TextView score = (TextView) 
					view.findViewById(R.id.multiplayer_high_scores_textView_score);
			score.setText(mHighScores.get(position));

			// Give it a nice background
			return view;
		}			


	}

	/*	
	 * Starts new Multiplayer Boggle Game	 
	 */
	public void onMultiplayerHighScoresSendNewRequestsButtonClicked(View v) {
		Intent i = new Intent(this, Multiplayer_New_Request_Form.class);
		startActivity(i);
	}

	/*
	 * Return to previous activity
	 */
	public void onMultiplayerHighScoresBackButtonClicked(View v) {
		finish();
	}
}
