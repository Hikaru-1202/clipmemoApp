package com.example.clipmemoapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Code extends RealmObject {
    @PrimaryKey
    public long question_id;
    public String question;
}
