package net.zoo9.moti;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by sheldon on 2016. 8. 14..
 */
public class MyRemindMessageReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("alert", Context.MODE_PRIVATE);
        Long lastUpdateTimeInMills = sharedPreferences.getLong("last_update_time_in_mills", System.currentTimeMillis());

        Calendar lastUpdateDate = Calendar.getInstance();
        lastUpdateDate.setTimeInMillis(lastUpdateTimeInMills);

        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());

        if (lastUpdateDate.get(Calendar.DATE) != today.get(Calendar.DATE)) {
            showNotificatioMessage(context);
        }
    }

    private void showNotificatioMessage(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, SplashActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(context);
        mCompatBuilder.setSmallIcon(R.drawable.moti_launch);
        mCompatBuilder.setTicker("Moti 알림");
        mCompatBuilder.setWhen(System.currentTimeMillis());
        mCompatBuilder.setNumber(10);
        mCompatBuilder.setContentTitle("오늘도 우리 아이 칭찬~");
        mCompatBuilder.setContentText("오늘도 우리 아이 좋은 습관 칭찬해주세요 ^^");
        mCompatBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mCompatBuilder.setContentIntent(pendingIntent);
        mCompatBuilder.setAutoCancel(true);

        nm.notify(222, mCompatBuilder.build());

    }
}
