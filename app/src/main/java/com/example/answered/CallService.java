package com.example.answered;

import android.app.*;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CallService extends Service {

    public final static String CHANNEL_ID = "serviceID";
    public final static int NOTIFICATION_ID = 3;
    private static final String TAG = "CallService";
    public boolean serviceOn;
    private NotificationManagerCompat managerCompat;
    private Notification notification;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        managerCompat = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create custom foreground notification channel
            createChannel();
        }
        // Needs to create notification now for a foreground service
        Log.i(TAG, "onCreate called as well as createChannel");
        createNotification();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotification() {
//        Intent intent = new Intent(this, AnotherActivity.class);
//        // Use System.currentTimeMillis() to have a unique ID for the pending intent
//        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        // Building notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentTitle(getString(R.string.notificationName));
        builder.setContentText(getString(R.string.notificationDescription));
        builder.setCategory(Notification.CATEGORY_SERVICE);
        builder.setOngoing(true);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_LOW);
        // TODO Add button to cancel service from notification
//        builder.setContentIntent(pIntent);
//        // Creating Intent and Pending Intent for actions
//        Intent bdIntent = new Intent(this, MyServiceReceiver.class);
//        bdIntent.setAction(ACTION_STOP);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, bdIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        // Adding action to notification builder
//        builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, getString(R.string.stopService), pendingIntent);
        // Launch notification
        notification = builder.build();
        startForeground(NOTIFICATION_ID, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        String name = getString(R.string.channelName);
        String description = getString(R.string.channelDescription);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // For devices older than O
        Log.i("CallService", "onStartCommand called");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startForeground(NotificationManager.IMPORTANCE_LOW, new Notification());
            return START_STICKY;
        }
        if (intent.getAction() == null || intent.getAction().equals(MainActivity.START_SERVICE)) {
            createNotification();
            Log.i(TAG, "Start service");
        } else if (intent.getAction().equals(MainActivity.STOP_SERVICE)){
            Log.i(TAG, String.valueOf(intent.getAction()));
            managerCompat.cancel(NOTIFICATION_ID);
            stopForeground(true);
            Log.i(TAG, "Stop service");

            return START_NOT_STICKY;
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
