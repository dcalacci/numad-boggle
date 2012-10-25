package edu.madcourse.dancalacci.boggle;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.Process;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class Multiplayer_Game_Service extends Service{
	private static final String TAG = "Multiplayer_Game_Service";
	private Looper mServiceLooper;
	private GameServiceHandler mServiceHandler;

	// Handler that receives messages from the thread
	private final class GameServiceHandler extends Handler {
		public GameServiceHandler(Looper looper) {
			super(looper);
		}
		
		public void handleMessage(Message msg) {
			// Normally we would do some work here, like download a file.
			// For our sample, we just sleep for 5 seconds.
			long endTime = System.currentTimeMillis() + 5*1000;
			while (System.currentTimeMillis() < endTime) {
				synchronized (this) {
					try {
						wait(endTime - System.currentTimeMillis());
					} catch (Exception e) {
					}
				}
			}
			// Stop the service using the startId, so that we don't stop
			// the service in the middle of handling another job
			stopSelf(msg.arg1);
		}
	}

	public void onCreate() {
		// Start up the thread running the service.  Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block.  We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		HandlerThread thread = new HandlerThread("ServiceStartArguments",
				Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		// Get the HandlerThread's Looper and use it for our Handler 
		mServiceLooper = thread.getLooper();
		mServiceHandler = new GameServiceHandler(mServiceLooper);
	}

	
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		
		// Stuff I think
		return null;
	}

	public void onDestroy() {
		Toast.makeText(this, "Game Disconnected", Toast.LENGTH_SHORT).show(); 
	}

}
