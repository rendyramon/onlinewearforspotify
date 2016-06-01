package renatoprobst.mobileandwear.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Renato on 01/05/2016.
 */
public class NotificationController {
    public int icon;
    public String title;
    public String subTitle;
    public Context context;
    public MediaSessionCompat mSession;

    public NotificationController(Context context, MediaSessionCompat mSession) {
        this.context = context;
        this.mSession = mSession;
    }

    public void buildNotification(android.support.v4.app.NotificationCompat.Action action) {

        final Notification noti = new NotificationCompat.Builder(context)
                // Hide the timestamp
                .setShowWhen(false)
                        // Set the Notification style
                .setStyle(new NotificationCompat.MediaStyle()
                        // Attach our MediaSession token
                        .setMediaSession(mSession.getSessionToken())
                                // Show our playback controls in the compat view
                        .setShowActionsInCompactView(0, 1, 2))
                        // Set the Notification color
                        // Set the large and small icons
                        //  .setLargeIcon(artwork)
                .setSmallIcon(icon)
                        // Set Notification content information

                .setContentText(subTitle)
                .setContentTitle(title)
                        // Add some playback controls
                .addAction(android.R.drawable.ic_media_previous, "prev", retreivePlaybackAction(3))
                .addAction(action)
                .addAction(android.R.drawable.ic_media_next, "next", retreivePlaybackAction(2))
                .build();

        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, noti);
    }


    public PendingIntent retreivePlaybackAction(int which) {
        Intent action;
        PendingIntent pendingIntent;
        final ComponentName serviceName = new ComponentName(context, MusicPlayerService.class);
        switch (which) {
            case 1:
                // Play and pause
                action = new Intent(MusicPlayerService.ACTION_TOGGLE_PLAYBACK);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(context, 1, action, 0);
                return pendingIntent;
            case 2:
                // Skip tracks
                action = new Intent(MusicPlayerService.ACTION_NEXT);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(context, 2, action, 0);
                return pendingIntent;
            case 3:
                // Previous tracks
                action = new Intent(MusicPlayerService.ACTION_PREV);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(context, 3, action, 0);
                return pendingIntent;
            default:
                break;
        }
        return null;
    }
}
