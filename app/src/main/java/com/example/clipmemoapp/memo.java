package com.example.clipmemoapp;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class memo extends RealmObject {
    @PrimaryKey
    public long memo_id;
    public int category;
    public int favorite_flag;
    public int block_flag;
    public int password_needed_flag;
    public int memo_delete_flag;
    public String title;
    public String text;
    public Date made_date;
    public Date updated_date;
}
