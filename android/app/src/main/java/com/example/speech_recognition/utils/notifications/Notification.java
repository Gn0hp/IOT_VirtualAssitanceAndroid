//package com.example.speech_recognition.utils.notifications;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.os.Build;
//
//import androidx.core.app.NotificationCompat;
//
//import com.example.speech_recognition.R;
//
//public class Notification {
//
//    private static final String CHANNEL_ID = ;
//
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't chan ge the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    public void doSomething(){
//            android.app.Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                    // Show controls on lock screen even when user hides sensitive content.
//                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                    .setSmallIcon(R.drawable.ic_stat_player)
//                    // Add media control buttons that invoke intents in your media service
//                    .addAction(R.drawable.ic_prev, "Previous", prevPendingIntent) // #0
//                    .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)  // #1
//                    .addAction(R.drawable.ic_next, "Next", nextPendingIntent)     // #2
//                    // Apply the media style template
//                    .setStyle(new android.support.v4.media.app.Notification.MediaStyle()
//                            .setShowActionsInCompactView(1 /* #1: pause button */)
//                            .setMediaSession(mediaSession.getSessionToken()))
//                    .setContentTitle("Wonderful music")
//                    .setContentText("My Awesome Band")
//                    .setLargeIcon(albumArtBitmap)
//                    .build();
//        }
//}
