package com.fidenz.dev.crmdemo;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by fidenz on 4/17/18.
 */

public class crmdemo extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }
}
