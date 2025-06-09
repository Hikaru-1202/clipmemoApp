package com.example.clipmemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.realm.Realm;

public class CodeSetting extends AppCompatActivity {
    Realm mRealm;

    private long codeId;

    private Button backBtn;
    private Button checkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_code_setting);

        mRealm = Realm.getDefaultInstance();

        backBtn = findViewById(R.id.returnButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CodeSetting.this,UserMenu.class);
                startActivity(intent);
            }
        });

        checkBtn = findViewById(R.id.checkButton);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CodeSetting.this,MemoDisplay.class);
                startActivity(intent);
            }
        });

    }
}