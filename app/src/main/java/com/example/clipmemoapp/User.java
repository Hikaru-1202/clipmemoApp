package com.example.clipmemoapp;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @PrimaryKey
    public long user_id;
    public int question_id;
    public String keyword;
    public Date made_date;
    public Date updated_date;
}
