package com.example.mimo.musiquendo.Player;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.mimo.musiquendo.Provider.RequestManager;
import com.example.mimo.musiquendo.R;
import com.squareup.picasso.Picasso;

public class NotificationBuilder {

    private Context context;
    private String CHANNEL = "DEFAULT_CHANNEL";
    public static int ID = 1;

    public NotificationBuilder(Context context) {
        this.context = context;
    }

    public void showNotification(String trackName, String artist) {
        PendingIntent pi;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL)
                .setContentTitle(trackName)
                .setContentText(artist)
                .setContentInfo("3:21")
                .setTicker("Reproduciendo")
                .setSmallIcon(R.drawable.auriculares)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.auriculares))
                .addAction(R.drawable.ic_skip_previous_black, "Prev", null)
                .addAction(R.drawable.ic_pause_black, "Pause", null)
                .addAction(R.drawable.ic_skip_next_black, "Next",null);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();

        bigTextStyle.bigText(artist)
                .setBigContentTitle(trackName)
                .setSummaryText(artist);

        builder.setStyle(bigTextStyle);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID, builder.build());
    }
}
