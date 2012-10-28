package edu.madcourse.dancalacci.boggle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Multiplayer_Game_AutoStart extends BroadcastReceiver
{   
    Multiplayer_Game_Alarm_BroadcastReceiver alarm = new Multiplayer_Game_Alarm_BroadcastReceiver();
    @Override
    public void onReceive(Context context, Intent intent)
    {   
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.SetAlarm(context);
        }
    }
}
