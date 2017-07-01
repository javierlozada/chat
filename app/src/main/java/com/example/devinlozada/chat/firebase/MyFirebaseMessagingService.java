/* Copyright 2013,2014,2015 AITECH SOFTWARE CORPORATION, INC., all rights reserved

        Name:	MyFirebaseMessagingService.java
        Author:	Devin Javier Lozada
        Date:	16-Sept-2016
        Desc:	class to get message from server and show it on notification

        *****	Revision History

        Name:   Devin Javier Lozada
        Date:   29-nov-2016
        Desc:   fixed logic to get data from backend(erases switch/case)

        Name:   Devin Javier Lozada
        Date:   23-May-2017
        Desc:   added switch/case so when a push is show to get MODULE and when tapped go to intent

        Name:   Devin Javier Lozada
        Date:   31-May-2017
        Desc:   added switch/case depending on the tabMessagesIndicator to fo to tab in messages

        Name:   Devin Javier Lozada
        Date:   02-Jun-2017
        Desc:   added case login




*/

package com.example.devinlozada.chat.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.devinlozada.chat.Chat;
import com.example.devinlozada.chat.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG     = "MyFirebaseMsgService";
    private String message              = "";
    private String messageTopic         = "";

    public MyFirebaseMessagingService(){}

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if(message.equals("")){
                messageTopic = remoteMessage.getData().get("message");
                showNotification(messageTopic);
            }else {

                message = remoteMessage.getData().get("MSG");
                showNotification(message);

            }

        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See method below.
    }

    /*muestra notificaciones en un push notificaciones*/
    private void showNotification(String message){

        NotificationCompat.Builder notificationBuilder = null;
        NotificationManager notificationManager;
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = null;
        int uniqueInt = new Random().nextInt(100000);
        PendingIntent pendingIntent;


        intent = new Intent(this, Chat.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(), uniqueInt, intent,0);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Causa y Efecto")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(uniqueInt, notificationBuilder.build());

    }

}
