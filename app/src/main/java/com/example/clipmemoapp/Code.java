package com.example.clipmemoapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Code extends RealmObject {
    @PrimaryKey
    public int code_id;
    public int question_id;
    public String keyword;
}
