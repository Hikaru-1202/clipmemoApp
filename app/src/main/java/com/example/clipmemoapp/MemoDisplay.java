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

    Realm mRealm;
    private Button backBtn;
    private TextView TitleView;
    private TextView TextView;
    public String TitleDisplay;
    public String TextDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo_display);
        Intent intent = getIntent();
        TitleDisplay = intent.getStringExtra("MEMO_TITLE");
        TextDisplay = intent.getStringExtra("MEMO_TEXT");
        mRealm = Realm.getDefaultInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TitleView = findViewById(R.id.titleView);
        TextView = findViewById(R.id.textView);
        TitleView.setText(TitleDisplay);
        TextView.setText(TextDisplay);
        backBtn = findViewById(R.id.returnButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        memo memo
                                = realm.createObject(memo.class,0);
                        memo.title = TitleDisplay;
                        memo.text = TextDisplay;
                        memo.category = 0;
                        memo.favorite_flag = 0;
                        memo.block_flag = 0;
                        memo.password_needed_flag = 0;
                        memo.memo_delete_flag = 0;
                        memo.made_date = new Date();
                        memo.updated_date = new Date();
                    }
                });
                Intent intent = new Intent(MemoDisplay.this,MainActivity.class);
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