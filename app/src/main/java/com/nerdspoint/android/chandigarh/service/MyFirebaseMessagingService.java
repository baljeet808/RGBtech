package com.nerdspoint.android.chandigarh.service;

/**
 * Created by android on 3/31/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.system.Os;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import com.nerdspoint.android.chandigarh.activities.MainPage;
import com.nerdspoint.android.chandigarh.app.Config;
import com.nerdspoint.android.chandigarh.fragments.Notification;
import com.nerdspoint.android.chandigarh.util.NotificationUtils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{


        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "\n\n\n\n\n\n\n\n\t\t\t\t\t\t\t\t\t\tpush json: " + json.toString()+"\n\n\n\n\n\n\n\n\n\n");

        try {

            JSONObject data = json.getJSONObject("data");

            JSONObject payload = data.getJSONObject("payload");
            String status = payload.getString("status");

            if(status.equals("accepted")  || status.equals("rejected")) {
                String msgId = payload.getString("messageId");
                String name = payload.getString("name");


                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("status", status);
                    pushNotification.putExtra("messageId",msgId);
                    pushNotification.putExtra("name",name);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                } else {
                    // app is in background, show the notification in notification tray
                    Intent resultIntent = new Intent(getApplicationContext(), MainPage.class);
                    resultIntent.putExtra("message",""+name+" has "+status+" your request");
                    // check for image attachment

                        showNotificationMessage(getApplicationContext(),""+name+" "+status+" your demand","message",""+name+" has "+status+" your demand", resultIntent);
                    /*else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                    }*/
                }

            }
            else {


                String messageId = payload.getString("messageId");
                String UID = payload.getString("UID");
                String name = payload.getString("Name");
                String UFid = payload.getString("uFid");


                String title = data.getString("title");
                String message = data.getString("message");
                boolean isBackground = data.getBoolean("is_background");
                String imageUrl = data.getString("image");
                String timestamp = data.getString("timestamp");
                int length = Integer.parseInt(payload.getString("length"));
                List<String> listOfIds = new ArrayList<>();

                for (int i = 0; i < length; i++) {
                    listOfIds.add(payload.getString("CPID" + i));
                }

                Log.e(TAG, "title: " + title);
                Log.e(TAG, "message: " + message);
                Log.e(TAG, "isBackground: " + isBackground);
                Log.e(TAG, "payload: " + payload.toString());
                Log.e(TAG, "imageUrl: " + imageUrl);
                Log.e(TAG, "timestamp: " + timestamp);
                Log.e(TAG, "length fetched " + length);
                Log.e(TAG, "mesage id " + messageId);


                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", message);

                    pushNotification.putExtra("title", title);
                    pushNotification.putExtra("timeStamp", timestamp);
                    pushNotification.putExtra("UID", UID);
                    pushNotification.putExtra("Name", name);
                    pushNotification.putExtra("uFid", UFid);
                    pushNotification.putExtra("length", "" + length);
                    pushNotification.putExtra("messageId", messageId);
                    pushNotification.putExtra("status", status);
                    for (int j = 0; j < length; j++) {
                        pushNotification.putExtra("cpid" + j, listOfIds.get(j));
                    }

                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                } else {
                    // app is in background, show the notification in notification tray
                    Intent resultIntent = new Intent(getApplicationContext(), MainPage.class);
                    resultIntent.putExtra("message", message);
                    resultIntent.putExtra("title", title);
                    resultIntent.putExtra("timeStamp", timestamp);
                    resultIntent.putExtra("UID", UID);
                    resultIntent.putExtra("Name", name);
                    // check for image attachment
                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}