package com.example.clipmemoapp;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Pass extends RealmObject {
    @PrimaryKey
    public long pass_id;
    public String password;
    public int non_active_flag;
    public Date made_date;
}
