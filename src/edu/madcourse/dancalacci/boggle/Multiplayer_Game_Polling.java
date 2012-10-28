package edu.madcourse.dancalacci.boggle;

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

public class Multiplayer_Game_Polling extends Service
{
	Alarm alarm = new Alarm();
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

