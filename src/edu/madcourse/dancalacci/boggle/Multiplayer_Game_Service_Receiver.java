package edu.madcourse.dancalacci.boggle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Multiplayer_Game_Service_Receiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    Intent service = new Intent(context, Multiplayer_Game_Service.class);
    context.startService(service);
  }
} 