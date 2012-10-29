package edu.madcourse.dancalacci.boggle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class OnAlarmReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Multiplayer_Game_Intent_Service.acquireStaticLock(context);
    
    Toast.makeText(context, "onAlarm", Toast.LENGTH_LONG).show();
    
    Log.d("AlarmReceiver", "AlarmReceiver");
    context.startService(new Intent(context, Multiplayer_Game_Service.class));
  }
}