/* Copyright 2013,2014,2015 AITECH SOFTWARE CORPORATION, INC., all rights reserved

        Name:	GcmBroadcasReceiver.java
        Author:	Devin Javier Lozada
        Date:	17-May-2017
        Desc:	class to receive and instantiate the firebase service when the app isnt running, so, the user can receive push notification when the app is closed

        *****	Revision History



*/


package com.example.devinlozada.chat.externalFunctions;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.devinlozada.chat.firebase.MyFirebaseInstanceIDService;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(), MyFirebaseInstanceIDService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
