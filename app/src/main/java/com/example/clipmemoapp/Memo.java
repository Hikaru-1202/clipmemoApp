package com.example.clipmemoapp;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Memo extends RealmObject {
    @PrimaryKey
    public long memo_id;
    public String title;
    public String text;
    public int category;
    public int favorite_flag;
    public int block_flag;
    public int password_needed_flag;
    public Date made_date;
    public Date updated_date;
}
