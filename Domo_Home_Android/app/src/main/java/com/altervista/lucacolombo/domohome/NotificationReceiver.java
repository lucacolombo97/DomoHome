package com.altervista.lucacolombo.domohome;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;


public class NotificationReceiver extends BroadcastReceiver
{
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public NotificationReceiver()
    {

    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        Bundle extras = intent.getExtras();


        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty())
        {

            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                sendNotification(context,"Perimetro casa violato!");
            }
        }
    }

    private void sendNotification(Context ctx,String msg)
    {
        mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // scelta suoneria per notifica
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent i=new Intent(ctx,MessageActivity.class);
        PendingIntent pi= PendingIntent.getActivity(ctx, 0, i, 0);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(ctx)
                        .setContentTitle("INTRUSIONE!")
                        .setSmallIcon(R.drawable.small_icon_notification)
                        .setContentText(msg)
                        .setSound(sound)
                        .setContentIntent(pi)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setLights(Color.MAGENTA,1000,1000)
                        .setOngoing(true)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(false);

        Bitmap largeIcon = BitmapFactory.decodeResource(ctx.getResources(),
                R.drawable.domo_home_logo);
        mBuilder.setLargeIcon(largeIcon);

        // effettua la notifica
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
