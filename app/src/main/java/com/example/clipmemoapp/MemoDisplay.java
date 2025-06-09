package com.example.clipmemoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class MemoDisplay extends AppCompatActivity {
    Realm mRealm;
    private Button backBtn;
    private Button editBtn;
    private TextView TitleView;
    private TextView TextView;
    private long ID_NUM = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo_display);

        ID_NUM = getIntent().getLongExtra("KeyNUM",0);
        Log.v("MY_LOG","Display:"+String.valueOf(ID_NUM));

        ((TextView) findViewById(R.id.textView)).setMovementMethod(new ScrollingMovementMethod());

        mRealm = Realm.getDefaultInstance();

        TitleView = findViewById(R.id.titleView);
        TextView = findViewById(R.id.textView);

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = realm.where(Memo.class)
                        .equalTo("memo_id",ID_NUM).findFirst();

                String title = memo.title;
                String text = memo.text;

                TitleView.setText(title);
                TextView.setText(text);
            }
        });

        backBtn = findViewById(R.id.returnButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoDisplay.this,MainActivity.class);
                startActivity(intent);
            }
        });
        editBtn = findViewById(R.id.editButton);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoDisplay.this,MemoEdit.class);
                intent.putExtra("KeyNUM", ID_NUM);
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

