package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class Multiplayer_Game_Service extends Service {
	private final IBinder mBinder = new MyBinder();
	private String currentTurn;
	private String user1; 
	private String user2;
	private ServerAccessor sa = new ServerAccessor();

	public Multiplayer_Game_Service(String user1, String user2) {
		super();
		this.user1 = user1;
		this.user2 = user2;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		this.currentTurn = sa.getTurn(user1, user2);
		
		return Service.START_STICKY;
	}

	
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class MyBinder extends Binder {
		Multiplayer_Game_Service getService() {
			return Multiplayer_Game_Service.this;
		}
	}

	public String getCurrentTurn() {
		return currentTurn;
	}

} 