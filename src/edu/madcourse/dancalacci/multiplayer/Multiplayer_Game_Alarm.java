package edu.madcourse.dancalacci.multiplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Process;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class Multiplayer_Game_Alarm extends Service
{
	Multiplayer_Game_Alarm_BroadcastReceiver alarm = new Multiplayer_Game_Alarm_BroadcastReceiver();
	public void onCreate()
	{
		super.onCreate();       
	}

	public void onStart(Context context,Intent intent, int startId)
	{
		alarm.SetAlarm(context);	
	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
}

