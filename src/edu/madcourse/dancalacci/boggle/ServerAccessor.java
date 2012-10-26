package edu.madcourse.dancalacci.boggle;
import java.util.*;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

//go_ogle_me
//googleme

import edu.neu.mobileclass.apis.KeyValueAPI;


public class ServerAccessor {
	private static final String TEAM_NAME = "go_ogle_me";
	private static final String PASSWORD  = "googleme";
	private static String USER_NAME = "";
	private static String USER_PASS = "";

	private static final String BOARD_PREFIX = "board_";
	private static final String TURN_PREFIX = "turn_";
	private static final String ENTERED_WORDS_PREFIX = "entered_";
	private static final String SCORES_PREFIX = "scores_";
	private static final String NUM_TURNS_PREFIX = "numTurns_";
	private static final String REQUESTS_PREFIX = "req_";
	private static final String RECEIVED_PREFIX = "rec_";
	private static final String USERS_KEY = "users";
	private static final String GAMES_KEY = "games";
	
	// Context for doing asyncTask
	private static Context c;

	private static final String TAG = "ServerAccessor";
	
	private static final ArrayList<List<Character>> dice = new ArrayList<List<Character>>() {{
		add( Arrays.asList( 'a', 'a', 'a', 'f', 'r', 's'));
		add( Arrays.asList( 'a', 'a', 'e', 'e', 'e', 'e'));
		add( Arrays.asList( 'a', 'a', 'f', 'i', 'r', 's'));
		add( Arrays.asList( 'a', 'd', 'e', 'n', 'n', 'n'));
		add( Arrays.asList( 'a', 'e', 'e', 'e', 'e', 'm'));
		add( Arrays.asList( 'a', 'e', 'e', 'g', 'm', 'u'));
		add( Arrays.asList( 'a', 'e', 'g', 'm', 'n', 'n'));
		add( Arrays.asList( 'a', 'f', 'i', 'r', 's', 'y'));
		add( Arrays.asList( 'b', 'j', 'k', 'q', 'x', 'z'));
		add( Arrays.asList( 'c', 'c', 'n', 's', 't', 'w'));
		add( Arrays.asList( 'c', 'e', 'i', 'i', 'l', 't'));
		add( Arrays.asList( 'c', 'e', 'i', 'l', 'p', 't'));
		add( Arrays.asList( 'c', 'e', 'i', 'p', 's', 't'));
		add( Arrays.asList( 'd', 'd', 'l', 'n', 'o', 'r'));
		add( Arrays.asList( 'd', 'h', 'h', 'l', 'o', 'r'));
		add( Arrays.asList( 'd', 'h', 'h', 'n', 'o', 't'));
		add( Arrays.asList( 'd', 'h', 'l', 'n', 'o', 'r'));
		add( Arrays.asList( 'e', 'i', 'i', 'i', 't', 't'));
		add( Arrays.asList( 'e', 'm', 'o', 't', 't', 't'));
		add( Arrays.asList( 'e', 'n', 's', 's', 's', 'u'));
		add( Arrays.asList( 'f', 'i', 'p', 'r', 's', 'y'));
		add( Arrays.asList( 'g', 'o', 'r', 'r', 'v', 'w'));
		add( Arrays.asList( 'h', 'i', 'p', 'r', 'r', 'y'));
		add( Arrays.asList( 'n', 'o', 'o', 't', 'u', 'w'));
		add( Arrays.asList( 'o', 'o', 'o', 't', 't', 'u'));
	}};

	public ServerAccessor() {
	}

	/**
	 * Gets the value associated with the given key
	 * @param key    the key associated with the value to get
	 */
	public String get(String key) {
		return KeyValueAPI.get(TEAM_NAME, PASSWORD, key);
	}

	/**
	 * Stores given key/value pair on the server
	 * @param key    the key of the key/value pair
	 * @param value  the value of the key/value pair.
	 */
	public void put(String key, String value) {
		KeyValueAPI.put(TEAM_NAME, PASSWORD, key, value);
	}

	/**
	 * Clears all key/value pairs from the server.
	 */
	public void clear() {
		KeyValueAPI.clear(TEAM_NAME, PASSWORD);
	}

	/**
	 * Clears a specific key/value pairing
	 * @param key the key of the key/value pair to delete.
	 */
	public void clearKey(String key) {
		KeyValueAPI.clearKey(TEAM_NAME, PASSWORD, key);
	}

	/**
	 * Returns true if the server is available.
	 */
	public boolean isAvailable() {
		return KeyValueAPI.isServerAvailable();
	}

	/**
	 * Converts a list of Strings to a single String
	 * @param users The list of users to convert
	 */
	public String arrayListToString(ArrayList<String> users) {
		StringBuilder builder = new StringBuilder();
		for (String user : users) {
			if (!user.equals("")) {
				builder.append(user + ",");
			}
		}
		return builder.toString();
	}

	public ArrayList<String> stringToArrayList(String strUsers) {
		ArrayList<String> users = new ArrayList<String>(Arrays.asList(strUsers.split(",")));
		return users;
	}
	// Requests

	/**
	 * Adds a game request for the given user
	 * @param user the user for whom to add the request
	 * @param req  the user to add to users' request list
	 */
	private void addRequest(String user, String req) {
		String key = REQUESTS_PREFIX + user;
		ArrayList<String> reqs = this.stringToArrayList(this.get(key));
		reqs.add(req);
		String val = this.arrayListToString(reqs);
		this.put(key, val);
	}

	//TODO: Deprecated.  Implement calling getSentRequests(asynchronous)
	/**
	 * Returns a list of the given users' requests
	 * @param user the user whose requests we're getting
	 * @return an ArrayList<String> of all the given users' requests
	 */
	private ArrayList<String> getSentRequests(String user) {
		String key = REQUESTS_PREFIX + user;
		ArrayList<String> reqs = this.stringToArrayList(this.get(key));
		return reqs;
	}
	
	public void getSentRequests(String user, final OnStringArrayListLoadedListener l) {
		
		final ServerAccessor thisSA = this;
		Log.d(TAG, "About to create an AsyncTask GetKeyTask");
		class GetKeyTask extends AsyncTask<String, Integer, ArrayList<String>> {
			
			protected ArrayList<String> doInBackground(String... key) {
				Log.d(TAG, "in doInBackground for getSentRequests");
				return stringToArrayList(thisSA.get(key[0]));
			}
			
			protected void onPostExecute(ArrayList<String> result) {
				Log.d(TAG, "in onPostExecute for getSentRequests");
				l.run(result);
			}
		}
		
		try {
			new GetKeyTask().execute(REQUESTS_PREFIX + user);
		} catch(Exception e) {
			Log.e(TAG, "GetKeyTask thread died: " +e);
		}
	}

	/**
	 * Adds all the given requests to the users' requests list
	 * @param user The user to add requests to
	 * @param reqsToAdd The list of Strings that represents all the requests to add
	 */
	public void addRequestList(String user, ArrayList<String> reqsToAdd) {
		ArrayList<String> reqs = this.getSentRequests(user);
		reqs.addAll(reqsToAdd);
		String key = REQUESTS_PREFIX + user;
		String val = this.arrayListToString(reqs);
		this.put(key, val);
	}

	/**
	 * Removes the given request from the given users' sent request list
	 * @param user  The user whose request list we're removing stuff from
	 * @param req   The user request we're removing
	 */
	private void removeRequest(String user, String req) {
		String key = REQUESTS_PREFIX + user;
		ArrayList<String> curReqs = this.getSentRequests(user);
		System.out.println("getRequests for " +user +"looks like: " +this.arrayListToString(this.getSentRequests(user)));
		if (curReqs.contains(req)) {
			curReqs.remove(req);
			System.out.println("removing " +req +" from " +user+"'s sent request list");
			String val = this.arrayListToString(curReqs);
			System.out.println("putting '" +val +"' at key " +key);
			this.put(key, val);
		}
		
//		
//		
//		String key = REQUESTS_PREFIX + user;
//		ArrayList<String> curReqs = this.getRequests(key);
//		curReqs.remove(req);
//		String val = this.arrayListToString(curReqs);
//		this.put(key, val);
	}

	// Received

	/**
	 * Adds user rec to user1's recieved list
	 * @param user  The user whose recieved list we're editing
	 * @param rec   The user to add to users's recieved list
	 */
	private void addReceived(String user, String rec) {
		String key = RECEIVED_PREFIX + user;
		ArrayList<String> recs = this.stringToArrayList(this.get(key));
		recs.add(rec);
		String val = this.arrayListToString(recs);
		this.put(key, val);
	}

	/**
	 * Returns a list of the given users' received requests...asynchronously
	 * @param user  The user whose requests we're returning
	 * @param arrayListListener The listener whose run(ArrayList<String>) function we're gonna call at the end
	 * @return    The list of users' received requests 
	 */
	public void getReceivedRequests(String user, final OnStringArrayListLoadedListener arrayListListener) {
		
		final ServerAccessor thisSA = this;
		Log.d(TAG, "About to create an AsyncTask GetKeyTask");
		class GetReceivedRequestsTask extends AsyncTask<String, Integer, ArrayList<String>> {
			
			protected ArrayList<String> doInBackground(String... key) {
				return stringToArrayList(thisSA.get(key[0]));
			}
			
			protected void onPostExecute(ArrayList<String> result) {
				Log.d(TAG, "in onPostExecute for getSentRequests");
				arrayListListener.run(result);
			}
		}
		
		try {
			new GetReceivedRequestsTask().execute(RECEIVED_PREFIX + user);
		} catch(Exception e) {
			Log.e(TAG, "GetKeyTask thread died: " +e);
		}
	}

	
	//TODO: Deprecated.  implement calling getReceivedRequests
	/**
	 * Returns a list of the given users' received requests
	 * @param user the user whose requests we're getting
	 * @return an ArrayList<String> of all the given users' requests
	 */
	private ArrayList<String> getReceivedRequests(String user) {
		String key = RECEIVED_PREFIX + user;
		ArrayList<String> recs = this.stringToArrayList(this.get(key));
		return recs;
	}
	
	/**
	 * Adds all users in the list to the given users' received list
	 * @param user  The user whose received list we're editing
	 * @param recs  The list of requests to add
	 */
	public void addReceivedRequests(String user, ArrayList<String> recs) {
		String key = RECEIVED_PREFIX + user;
		ArrayList<String> curRecs = this.stringToArrayList(this.get(key));
		curRecs.addAll(recs);
		String val = this.arrayListToString(curRecs);
		this.put(key, val);
	}

	/**
	 * Removes rec from the given users' list of received requests
	 * @param user  The user whose received list we're editing
	 * @param rec   The received request we're removing
	 */
	private void removeReceivedRequest(String user, String rec) {
		String key = RECEIVED_PREFIX + user;
		ArrayList<String> curRecs = this.getReceivedRequests(user);
		if (curRecs.remove(rec)) {
			System.out.println(rec + " was found in curRecs");
			String val = this.arrayListToString(curRecs);
			System.out.println("putting '" +val +"' at key " +key);
			this.put(key, val);
		}
	}

	/**
	 * Returns true if the given user/pass combination exists
	 * @param user  The given username
	 * @param pass  The given password
	 */
	public boolean login(String user, String pass) {
		String usersKey = USERS_KEY;
		String passKey = "pass_" + user;

		String passVal = this.get(passKey);
		ArrayList<String> users = stringToArrayList(this.get(usersKey));

		if (users.contains(user) && pass.equals(passVal)) {
			this.USER_NAME  = user;
			this.USER_PASS   = pass;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Registers a new user
	 * This will only be called if the given user/pass is valid
	 * @param user  The new username to register
	 * @param pass  The password for the new username
	 */
	public void register(String user, String pass) {
		String usersKey = USERS_KEY;
		String passKey = "pass_" + user;

		String passVal = pass;
		this.put(passKey, passVal); // enters the new user/pass combo into the server

		ArrayList<String> users = stringToArrayList(this.get(usersKey));
		users.add(user); // adds the given username to the list of users
		this.put(usersKey, this.arrayListToString(users)); // updates the list of users on the server
	}

	/**
	 * Gets the list of users
	 * @return An ArrayList representation of users
	 */
	public ArrayList<String> getUserList() {
		String usersKey = USERS_KEY;
		return this.stringToArrayList(get(usersKey));
	}
	
	/**
	 * Checks to see if a new user/pass combo can be registered
	 * @param user  The username to check
	 * @param pass  The password to check
	 * @return true if the username/pass can be registered, false otherwise
	 */
	public boolean canRegister(String user, String pass) {
		// if the username or password contains commas, it can't be registered
		if (user.contains(",") || pass.contains(",")) {
			return false;
		} else if (alreadyRegistered(user)) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Checks to see if the given username is already registered
	 * @param user  The username to check
	 * @return true if the username is already registered
	 */
	public boolean alreadyRegistered(String user) {
		String usersKey = USERS_KEY;
		ArrayList<String> users = stringToArrayList(this.get(usersKey));
		return users.contains(user);
	}

	/**
	 * Returns the key for the given two users - order unspecific
	 * I recognize that this does not generate a very unique
	 * key, but it needs to be an int and we have a very small use case.
	 * @param user1   The first user
	 * @param user2   The second user
	 * @return        An integer value that is the key value for the two users
	 */
	private String getUsersKey(String user1, String user2) {
		// String.hashCode has used the same algorithm since java 1.3.1,
		// so i'm comfortable using it here to generate a consistent "unique" identifier
		// for the two given users
		int hash1 = user1.hashCode();
		int hash2 = user2.hashCode();
		int key = hash1+hash2;
		return "" + key;
	}

	/**
	 * Returns true if a game between the two users exists
	 * @param user1   The first user
	 * @param user2   The second user
	 * @return        True if a game exists, false otherwise
	 */
	public boolean doesGameExist(String user1, String user2) {
		String gameKey = GAMES_KEY;
		// get the unique identifier for these two users
		String thisGame = this.getUsersKey(user1, user2);
		ArrayList<String> games = this.stringToArrayList(this.get(gameKey));
		return games.contains(thisGame);
	}

	/**
	 * Adds a game between the two given users to the server
	 * @param user1   The first user
	 * @param user2   The second user
	 */
	public void addGame(String user1, String user2) {
		String gameKey = GAMES_KEY;
		String usersKey = this.getUsersKey(user1, user2);
		ArrayList<String> games = this.stringToArrayList(this.get(gameKey));
		games.add(usersKey);
		String gameVal = this.arrayListToString(games);
		this.put(gameKey, gameVal);
	}
	
	public void removeGame(String user1, String user2) {
		String gameKey = GAMES_KEY;
		String usersKey = this.getUsersKey(user1, user2);
		ArrayList<String> games = this.stringToArrayList(this.get(gameKey));
		games.remove(usersKey);
		String gameVal = this.arrayListToString(games);
		this.put(gameKey,  gameVal);
	}

	/**
	 * Produces an arraylist of all the users that user has games with
	 * @param user  The user who we are concerned with
	 * @return      An arraylist that contains every user that user is in a game with
	 */
	public ArrayList<String> getGames(String user, final OnStringArrayListLoadedListener arrayListListener) {
		
		final ServerAccessor thisSA = this;
		Log.d(TAG, "About to create an AsyncTask GetKeyTask");
		class GetGamesTask extends AsyncTask<String, Integer, ArrayList<String>> {
			
			protected ArrayList<String> doInBackground(String... key) {
				Log.d(TAG, "in doInBackground for GetGamestask");
				try {
					Thread.sleep(3000);
				} catch(Exception e) {
					
				}
				String user = key[0];
				ArrayList<String> serverGames = thisSA.stringToArrayList(thisSA.get(GAMES_KEY));
				ArrayList<String> serverUsers = thisSA.stringToArrayList(thisSA.get(USERS_KEY));
				ArrayList<String> games = new ArrayList<String>();
				for (String user2 : serverUsers) {
					if (serverGames.contains(thisSA.getUsersKey(user, user2))) {
						games.add(user2);
					}
				}
				return games;
			}
			
			protected void onPostExecute(ArrayList<String> result) {
				Log.d(TAG, "in onPostExecute for GetGamesTask");
				arrayListListener.run(result);
			}
		}
		
		try {
			new GetGamesTask().execute(user);
		} catch(Exception e) {
			Log.e(TAG, "GetGames thread died: " +e);
		}
		
		
		ArrayList<String> serverGames = this.stringToArrayList(this.get(GAMES_KEY));
		ArrayList<String> serverUsers = this.stringToArrayList(this.get(USERS_KEY));
		ArrayList<String> games = new ArrayList<String>();
		for (String user2 : serverUsers) {
			if (serverGames.contains(this.getUsersKey(user, user2))) {
				games.add(user2);
			}
		}
		return games;
	}

	
	// only called when a previous game doesn't exist
	private void initializeNewGame(String creator, String opponent, String board) {
		String userskey = this.getUsersKey(creator, opponent);

		// add game to game list
		this.addGame(creator, opponent);
		System.out.println("adding game: " +creator +" vs " +opponent);

		// add board to the server
		String boardKey = BOARD_PREFIX + userskey;
		this.put(boardKey, board); // create the board on the server

		// value of turn element will always be a username - initiates to the creator.
		String turnKey = TURN_PREFIX + userskey;
		this.put(turnKey, creator); // create the turn element on the server
		
		System.out.println(turnKey + " is now: " + creator);

		// create the entered words element on the server
		String enteredKey = ENTERED_WORDS_PREFIX + userskey;
		this.put(enteredKey, "");

		// create the scores element on the server
		// score is a string like "user1|50,user2|44"
		String scoresKey = SCORES_PREFIX + userskey;
		this.put(scoresKey, creator + "|0," + opponent + "|0");
		
		System.out.println(scoresKey + " is now: " + creator + "|0," + opponent + "|0");

		// create the number of turns element on the server
		String numTurnsKey = NUM_TURNS_PREFIX + userskey;
		this.put(numTurnsKey, "0");
	}

	/**
	 * Send a request from user1 to user2
	 * @param user1 The sender of the request
	 * @param user2 The receiver of the request
	 */
	public void sendRequest(String user1, String user2) {
		this.addRequest(user1, user2);
		this.addReceived(user2, user1);
	}
	
	/**
	 * Remove a sent request from user1 to user2
	 * @param user1 The sender of the request to be removed
	 * @param user2 The receiver of the request to be removed
	 */
	public void removeSentRequest(String user1, String user2) {
		this.removeReceivedRequest(user2, user1);
		this.removeRequest(user1, user2);
	}
	
	// TODO: create a scores interpreter
	// TODO: create a numTurns interpreter
	// TODO: create a board interpreter
	// TODO: create an enteredKey interpreter
	// TODO: make a (makeTurn) method that takes in a users' name, a new board,
	//        the user they're playing against, that users' updated score, and
	//        the updated entered word list....or if we choose to store everything 
	//        on the server, the word that the user just entered(might be cleaner).
	//        turn_... should update itself.

	protected static ArrayList<Character> generateBoard() {
		ArrayList<Character> tiles = new ArrayList<Character>();
		int indexSize = (int)Math.pow((double)Multiplayer_Game.size, (double)2);
		for (int i=0; i<indexSize; i++) {
			tiles.add(' ');
		}

		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (int i = 0; i < (dice.size()); i++) {
			positions.add(i);  // positions now contains all numbers 1-16
		}

		Collections.shuffle(positions); //shuffle positions
		Collections.shuffle(dice);      //shuffle the dice themselves

		// step two: randomly assign a character to each space on the board
		for (Integer pos : positions) {
			Character c = dice.get(pos).get((int)(Math.random() * 5));
			//  c is now a random face of the current die
			tiles.set(pos, c);
		}
		return tiles;
	}

	/**
	 * Build a string representation of the board for state saving
	 * @param board	The list representation of the board
	 * @return		The string representation of the board
	 */
	static private String boardToString(List<Character> board) {
		StringBuilder builder = new StringBuilder();
		for (Character ch : board) {
			builder.append(ch);
		}
		return builder.toString();
	}

	/**
	 * Convert a string to a board
	 * @param rep	The string representation of the board
	 * @return		The list representation of the board
	 */
	protected ArrayList<Character> stringToBoard(String rep) {
		ArrayList<Character> board = new ArrayList<Character>();
		for (int i = 0; i < rep.length(); i++) {
			board.add(rep.charAt(i));
		}
		return board;
	}

	public void createNewGame(String user1, String user2) {
		System.out.println("Creating a new game!");
		String board = boardToString(generateBoard());
		this.initializeNewGame(user1, user2, board);
	}

	public void updateBoard(String user1, String user2, String board) {
		String boardKey = BOARD_PREFIX + this.getUsersKey(user1, user2);
		String boardVal = board;
		this.put(boardKey, boardVal);
	}

	public GameWrapper getGame(String user1, String user2) {
		String userskey = this.getUsersKey(user1, user2);

		String turnKey = TURN_PREFIX + userskey;
		String currentTurn = 
				this.get(turnKey);

		String boardKey = BOARD_PREFIX + userskey;
		ArrayList<Character> board =
				this.stringToBoard(this.get(boardKey));

		String enteredWordsKey = ENTERED_WORDS_PREFIX + userskey;
		ArrayList<String> enteredWords =
				this.stringToArrayList(this.get(enteredWordsKey));

		String scoresKey = SCORES_PREFIX + userskey;
		Hashtable<String, Integer> scores = this.scoresStringToHashtable(
				this.get(scoresKey));

		String numTurnsKey = NUM_TURNS_PREFIX + userskey;
		int numTurns = Integer.parseInt(this.get(numTurnsKey));

		return new GameWrapper(
				currentTurn,
				board,
				enteredWords,
				scores,
				numTurns);
	}

	/**
	 * Converts a given string representation of scores to a hashtable
	 * The string looks like: "user1|00,user2|00"
	 * @param scores the string representation to convert
	 * @return the hashtable conversion
	 */
	private Hashtable<String, Integer> scoresStringToHashtable(String scores) {
		
		//TODO: THIS AIN'T WORKIN'
		ArrayList<String> parts = new ArrayList<String>(
				Arrays.asList(scores.split(",")));
		System.out.println("Parts is: "+ parts.toString());
		Hashtable<String, Integer> scoresDic = new Hashtable<String, Integer>();
		for (String usrScore : parts) {
			List<String> part = Arrays.asList(usrScore.split("\\|"));
			System.out.println("part is now: " +part.toString());
			System.out.println("parsing " + part.get(1) +" as an integer.");
			scoresDic.put(part.get(0), Integer.parseInt(part.get(1)));
		}
		return scoresDic;
	}

	/**
	 * Converts a given scores hashtable to a string
	 * @param scores The hashtable of the two users' scores
	 * @return The string representation of the two users' scores.
	 */
	private String hashtableToScoresString(Hashtable<String, Integer> scores) {
		StringBuilder sb = new StringBuilder();
		List<String> usernames = Collections.list(scores.keys());
		String user1 = usernames.get(0);
		String user2 = usernames.get(1);

		sb.append(user1 + "|" + scores.get(user1) + "," +
				user2 + "|" + scores.get(user2));
		return sb.toString();
	}
}

