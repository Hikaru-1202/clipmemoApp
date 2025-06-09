package com.example.clipmemoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmResults;

public class UserMenu extends AppCompatActivity {
    Realm mRealm;
    private Button backBtn;
    private Button passBtn;
    private Button codeBtn;
    private Button notificationBtn;
    private Button clipBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_menu);
        backBtn = findViewById(R.id.returnButton);
        passBtn = findViewById(R.id.passButton);
        codeBtn = findViewById(R.id.codeButton);
        notificationBtn = findViewById(R.id.notificationButton);
        clipBtn = findViewById(R.id.clipButton);

        mRealm = Realm.getDefaultInstance();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMenu.this,MainActivity.class);
                startActivity(intent);
            }
        });
        passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMenu.this, PassEdit.class);
                startActivity(intent);
            }
        });
        codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMenu.this,CodeSetting.class);
                startActivity(intent);
            }
        });
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        clipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}