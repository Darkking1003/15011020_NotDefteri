package yildiz.com.a15011020_notdefteri;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReciever extends BroadcastReceiver {
    // This is the Notification Channel ID. More about this in the next section
    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    //User visible Channel Name
    public static final String CHANNEL_NAME = "Notification Channel";
    public static final int NOTIFICATION_ID = 101;
    int importance;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Deneme", "onReceive: Burdayım");
        String Title=(String) intent.getStringExtra("Title");
        Log.d("Deneme", "onReceive: "+Title);
        String Oncelik=(String) intent.getStringExtra("Priority");
        if(Oncelik.equals("Yüksek Öncelik")){
            importance= NotificationManager.IMPORTANCE_HIGH;
        }else if(Oncelik.equals("Orta Öncelik")){
            importance=NotificationManager.IMPORTANCE_DEFAULT;
        }else{
            importance=NotificationManager.IMPORTANCE_LOW;
        }
        Log.d("Deneme", "onReceive: "+Integer.toString(importance));

        Toast.makeText(context,"Alarm ÇAlışıyor "+Title+" "+Oncelik+" "+importance,Toast.LENGTH_SHORT).show();
        BuildChannerl(context);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(Title);
        builder.setContentText("Hatırlatma!! ");
        builder.setPriority(importance);
        builder.setSmallIcon(R.drawable.ic_more_vert_24);

        builder.setLights(0xff0000, 100, 100);

        builder.setVibrate(null);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    public void BuildChannerl(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
            //Boolean value to set if lights are enabled for Notifications from this Channel
            notificationChannel.enableLights(true);
            //Boolean value to set if vibration are enabled for Notifications from this Channel

            //Sets the color of Notification Light
            notificationChannel.setLightColor(Color.GREEN);
            //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
//            if(importance==4) {
//                Log.d("Deneme", "BuildChannerl: Vibration");
//                notificationChannel.enableVibration(true);
//                notificationChannel.setVibrationPattern(new long[]{
//                        500,
//                        500,
//                        500,
//                        500,
//                        500
//                });
//            }
            //Sets whether notifications from these Channel should be visible on Lockscreen or not
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
