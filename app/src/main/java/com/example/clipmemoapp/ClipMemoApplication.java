package com.example.clipmemoapp;

import android.app.Application;

import java.lang.reflect.Method;
import java.util.Date;

import io.realm.Realm;

public class ClipMemoApplication extends Application {

    Realm mRealm;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
