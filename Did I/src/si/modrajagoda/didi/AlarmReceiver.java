package si.modrajagoda.didi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
    public void onReceive(Context context, Intent intent) {
    	
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(context)
    	        .setSmallIcon(R.drawable.notification)
    	        .setContentTitle("Did I?")
    	        .setContentText("Time for the daily report!").setAutoCancel(true);
    	
    	// Creates an explicit intent for an Activity in your app
    	Intent launchAppIntent = new Intent(context, Main.class);

    	// The stack builder object will contain an artificial back stack for the
    	// started Activity.
    	// This ensures that navigating backward from the Activity leads out of
    	// your application to the Home screen.
    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    	// Adds the back stack for the Intent (but not the Intent itself)
    	stackBuilder.addParentStack(Main.class);
    	// Adds the Intent that starts the Activity to the top of the stack
    	stackBuilder.addNextIntent(launchAppIntent);
    	PendingIntent launchAppPendingIntent =
    	        stackBuilder.getPendingIntent(
    	            0,
    	            PendingIntent.FLAG_UPDATE_CURRENT
    	        );
    	mBuilder.setContentIntent(launchAppPendingIntent);
    	NotificationManager mNotificationManager =
    	    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	
    	// mId allows you to update the notification later on.
    	mNotificationManager.notify(1, mBuilder.build());
    }
}