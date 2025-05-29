package com.example.clipmemoapp;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @PrimaryKey
    public long user_id;
    public int question_id;
    public String keyword;
    public int notification_flag;
    public int autocopy;
    public int category_1_flag;
    public int category_2_flag;
    public int category_3_flag;
    public int category_4_flag;
    public int category_5_flag;
    public int category_6_flag;
    public int category_7_flag;
    public int category_8_flag;
    public Date updated_date;
    public int first_password_flag;
}
