/* Copyright 2013,2014,2015 AITECH SOFTWARE CORPORATION, INC., all rights reserved

        Name:	MyFirebaseInstanceIDService.java
        Author:	Devin Javier Lozada
        Date:	16-Sept-2016
        Desc:	class to get refreshed token

        *****	Revision History

        Name:
        Date:
        Desc:
*/

package com.example.devinlozada.chat.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    String refreshedToken;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // [END refresh_token]
    }


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
       // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       // sharedPreferences.edit().putString("FireBaseToken",token ).commit();
    }

    public String getFireBaseToken(){
        return refreshedToken;
    }

}