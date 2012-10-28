package edu.madcourse.dancalacci.boggle;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;
import edu.madcourse.dancalacci.R;
import edu.madcourse.dancalacci.boggle.Notification_Receiver;
import edu.madcourse.dancalacci.boggle.ServerAccessor;

public class Multiplayer_Game_Alarm_BroadcastReceiver extends BroadcastReceiver 
{    
	private ServerAccessor sa = new ServerAccessor();

	@Override
	public void onReceive(Context context, Intent intent) 
	{   
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
		wl.acquire();


		String username = intent.getStringExtra("username");
		String opponent = intent.getStringExtra("opponent");
		String currentTurn = intent.getStringExtra("currentTurn");
		String serverCurrentTurn = sa.getTurn(username, opponent);

		if (! (currentTurn.equals(serverCurrentTurn))){
			//			NotificationManager notificationManager =
			//				    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			//				Notification notification = new Notification(/* your notification */);
			//				PendingIntent pendingIntent = /* your intent */;
			//				notification.setLatestEventInfo(this, /* your content */, pendingIntent);
			//				notificationManager.notify(/* id */, notification);

			//			NotificationManager notificationManager = (NotificationManager) 
			//					  getSystemService(Context.NOTIFICATION_SERVICE); 

			Intent ns = new Intent(context, Notification_Receiver.class);
			PendingIntent pIntent = PendingIntent.getActivity(context, 0, ns, 0);
			
			NotificationManager nm = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
			Notification notif = new Notification(
		            R.drawable.tab_multiplayer_default_normal, 
		            "New move from " + opponent,
		            System.currentTimeMillis());
			
			
			// Pressing on the button brings the user back to our mood ring,
			// as part of the api demos app.  Note the use of NEW_TASK here,
			// since the notification display activity is run as a separate task.

			nm.notify(0, notif);
		}

		Toast.makeText(context, "Alarm !", Toast.LENGTH_LONG).show(); // For example

		wl.release();
	}

	public void SetAlarm(Context context)
	{
		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, Multiplayer_Game_Alarm_BroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 5, pi); // Millisec * Second * Minute
	}

	public void CancelAlarm(Context context)
	{
		Intent intent = new Intent(context, Multiplayer_Game_Alarm_BroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
}
