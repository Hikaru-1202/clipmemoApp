package com.example.clipmemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;

import io.realm.Realm;

public class MemoDisplay extends AppCompatActivity {
    private Button backBtn;
    private TextView TitleView;
    private TextView TextView;
    private String TitleDisplay;
    private String TextDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo_display);
        Intent intent = getIntent();
        TitleDisplay = intent.getStringExtra("MEMO_TITLE");
        TextDisplay = intent.getStringExtra("MEMO_TEXT");

        TitleView = findViewById(R.id.titleView);
        TextView = findViewById(R.id.textView);
        TitleView.setText(TitleDisplay);
        TextView.setText(TextDisplay);
        backBtn = findViewById(R.id.returnButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoDisplay.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}