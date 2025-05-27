package com.example.clipmemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MemoImport extends AppCompatActivity{
    private Button backBtn;
    private EditText editTitle;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo_import);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        backBtn = findViewById(R.id.returnButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoImport.this,MemoDisplay.class);
                editTitle = findViewById(R.id.editTitle);
                editText = findViewById(R.id.editText);
                intent.putExtra("MEMO_TITLE",editTitle.getText().toString());
                intent.putExtra("MEMO_TEXT",editText.getText().toString());
                startActivity(intent);
            }
        });
    }
}