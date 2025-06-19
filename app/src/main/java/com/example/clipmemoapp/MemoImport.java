package com.example.clipmemoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import io.realm.Realm;

public class MemoImport extends AppCompatActivity {
    Realm mRealm;
    private Button backBtn;
    private EditText I_Title;
    private EditText I_Text;
    private String G_Title;
    private String G_Text;
    private long newId = 0;
    private long ID_NUM;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo_import);

        ID_NUM = getIntent().getLongExtra("KeyNUM",-1);

        ((EditText) findViewById(R.id.importText)).setMovementMethod(new ScrollingMovementMethod());

        mRealm = Realm.getDefaultInstance();

        I_Title = findViewById(R.id.importTitle);
        I_Text = findViewById(R.id.importText);

        if (ID_NUM >= 0){
            Log.v("MY_LOG","統合完了");
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Memo memo =realm.where(Memo.class)
                            .equalTo("memo_id",ID_NUM).findFirst();
                    G_Title = memo.title;
                    G_Text = memo.text;

                    I_Title.setText(G_Title);
                    I_Text.setText(G_Text);
                }
            });
        }

        backBtn = findViewById(R.id.returnButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                I_Title = findViewById(R.id.importTitle);
//                I_Text = findViewById(R.id.importText);
                G_Title = I_Title.getText().toString();
                G_Text = I_Text.getText().toString();

                mRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        if (ID_NUM < 0){
                            Number max = realm.where(Memo.class).max("memo_id");
                            newId = 0;
                            if (max != null){
                                newId = max.longValue() + 1;
                            }
                            Memo memo
                                    = realm.createObject(Memo.class, newId);
                            if (G_Title.isEmpty()){
                                memo.title = "No Title";
                            }else {
                                memo.title = G_Title;
                            }
                            if (G_Text.isEmpty()){
                                memo.text = "No Text";
                            }else {
                                memo.text = G_Text;
                            }
                            memo.category = 0;
                            memo.favorite_flag = 0;
                            memo.block_flag = 0;
                            memo.password_needed_flag = 0;
                            memo.made_date = new Date();
                            memo.updated_date = new Date();

                            Intent intent = new Intent(MemoImport.this,MemoDisplay.class);
                            intent.putExtra("KeyNUM", newId);
                            startActivity(intent);
                        }else {
                            Memo memo = realm.where(Memo.class)
                                    .equalTo("memo_id",ID_NUM)
                                    .findFirst();
                            if (G_Title.isEmpty()){
                                memo.title = "No Title";
                            }else {
                                memo.title = G_Title;
                            }
                            if (G_Text.isEmpty()){
                                memo.text = "No Text";
                            }else {
                                memo.text = G_Text;
                            }
                            Intent intent = new Intent(MemoImport.this,MemoDisplay.class);
                            intent.putExtra("KeyNUM", ID_NUM);
                            startActivity(intent);
                        }

                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }
}