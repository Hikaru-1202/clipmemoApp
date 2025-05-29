package com.example.clipmemoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserMenu extends AppCompatActivity {
    private Button backBtn;
    private Button passBtn;
    private Button codeBtn;
    private Button notificationBtn;
    private Button clipBtn;
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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}