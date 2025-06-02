package com.example.clipmemoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    Realm mRealm;
    private Button importBtn;
    private Button viewBtn;
    private Button userBtn;
    private String[] Memo_Data = {};


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();

        importBtn = findViewById(R.id.memo_import_Button);
        viewBtn = findViewById(R.id.memo_view_Button);
        userBtn = findViewById(R.id.userButton);

        //月曜日（6/2）にレルムの処理追加

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MemoImport.class);
                startActivity(intent);
            }
        });
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MemoDisplay.class);
                startActivity(intent);
            }
        });
        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UserMenu.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }
}