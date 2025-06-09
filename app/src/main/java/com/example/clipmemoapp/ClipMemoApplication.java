package com.example.clipmemoapp;

import android.app.Application;

import java.util.Date;

import io.realm.Realm;

public class ClipMemoApplication extends Application {
//    private long newId;
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
