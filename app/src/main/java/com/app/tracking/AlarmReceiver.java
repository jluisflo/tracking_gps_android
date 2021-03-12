package com.app.tracking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {

    //interval for execute task service
    private Long INTERVAL = 5L;
    PendingIntent pi;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in = new Intent(context, AlarmService.class);
        context.startService(in);
        setAlarm(context);
    }

    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReceiver.class);
        pi = PendingIntent.getBroadcast(context, 0, i, 0);

        assert am != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() / 1000L + INTERVAL) * 1000L, pi); //Next alarm in 15s
        }
    }

    public void cancelAlarm(){
        pi.cancel();
    }
}