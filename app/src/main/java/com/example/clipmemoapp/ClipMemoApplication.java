package com.example.clipmemoapp;

import android.app.Application;

import io.realm.Realm;

public class ClipMemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
