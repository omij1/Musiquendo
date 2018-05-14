package com.example.mimo.musiquendo.Player;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.example.mimo.musiquendo.BuildConfig;
import com.example.mimo.musiquendo.R;

public class NotificationBuilder {

    private String CHANNEL = "DEFAULT_CHANNEL";
    public static int ID = 1;
    private Context context;
    private String contentTitle;
    private String contentText;
    private String contentInfo;
    private PendingIntent startStopIntent;
    private PendingIntent prevIntent;
    private PendingIntent nextIntent;
    private PendingIntent deleteIntent;

    NotificationBuilder(Context context, String contentTitle, String contentText, String contentInfo) {
        this.context = context;
        this.contentTitle = contentTitle;
        this.contentText = contentText;
        this.contentInfo = contentInfo;
    }

    public Notification showNotification() {
        //Según el modo de reproducción debe mostrarse una notificación u otra
        startIntents(context);
        return fullNotification();
    }

    /**
     * Método que devuelve una notificación básica con un único botón para parar o reanudar la canción
     * @return La notificación
     */
    private Notification basicNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentInfo(contentInfo)
                .setTicker(context.getString(R.string.playing))
                .setSmallIcon(R.drawable.auriculares)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.auriculares))
                .setDeleteIntent(deleteIntent)
                .addAction(R.drawable.ic_reanudar, context.getResources().getString(R.string.play_pause), startStopIntent);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();

        bigTextStyle.bigText(contentText)
                .setBigContentTitle(contentTitle)
                .setSummaryText(contentText);

        builder.setStyle(bigTextStyle);

        return builder.build();
    }

    /**
     * Método que devuelve una notificación completa con botones para parar o reanudar la canción
     * e ir a las canciones anterior y siguiente
     * @return La notificación
     */
    private Notification fullNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentInfo(contentInfo)
                .setTicker(context.getString(R.string.playing))
                .setSmallIcon(R.drawable.auriculares)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.auriculares))
                .setDeleteIntent(deleteIntent)
                .addAction(R.drawable.ic_skip_previous_black, context.getResources().getString(R.string.prev), prevIntent)
                .addAction(R.drawable.ic_reanudar, context.getResources().getString(R.string.play_pause), startStopIntent)
                .addAction(R.drawable.ic_skip_next_black, context.getResources().getString(R.string.next),nextIntent);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();

        bigTextStyle.bigText(contentText)
                .setBigContentTitle(contentTitle)
                .setSummaryText(contentText);

        builder.setStyle(bigTextStyle);

        return builder.build();
    }

    /**
     * Método que inicializa los intents y pendingintents relacionados con las acciones de la notificación
     * @param context Contexto en el que se van a ejecutar
     */
    private void startIntents(Context context) {
        Intent starStop = new Intent(context, MyBroadCastReceiver.class);
        starStop.setAction(BuildConfig.STARTSTOP);
        startStopIntent = PendingIntent.getBroadcast(context, 0, starStop, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent previous = new Intent(context, MyBroadCastReceiver.class);
        previous.setAction(BuildConfig.PREVIOUS);
        prevIntent = PendingIntent.getBroadcast(context, 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent next = new Intent(context, MyBroadCastReceiver.class);
        next.setAction(BuildConfig.NEXT);
        nextIntent = PendingIntent.getBroadcast(context, 0, next, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent delete = new Intent(context, MyBroadCastReceiver.class);
        delete.setAction(BuildConfig.DELETE);
        deleteIntent = PendingIntent.getBroadcast(context, 0, delete, PendingIntent.FLAG_ONE_SHOT);
    }
}
