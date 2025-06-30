package com.example.clipmemoapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class QuestionList extends RealmObject {
    @PrimaryKey
    public int question_id;
    public String questions;
}
