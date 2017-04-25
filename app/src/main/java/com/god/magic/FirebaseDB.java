package com.god.magic;

import com.firebase.client.Firebase;

/**
 * HISTORY
 * 	Date			Author				Comments
 * 	24.02.2017		Eduard Arefjev 		Firebase Context
 */

public class FirebaseDB extends android.app.Application{
    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
