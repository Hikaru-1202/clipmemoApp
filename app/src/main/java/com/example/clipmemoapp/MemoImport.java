package com.example.clipmemoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo_import);
        mRealm = Realm.getDefaultInstance();

        backBtn = findViewById(R.id.returnButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                I_Title = findViewById(R.id.importTitle);
                I_Text = findViewById(R.id.importText);
                G_Title = I_Title.getText().toString();
                G_Text = I_Text.getText().toString();

                mRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
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
                        memo.password_needed_flag = 1;
                        memo.made_date = new Date();
                        memo.updated_date = new Date();

                        Intent intent = new Intent(MemoImport.this,MemoDisplay.class);
                        intent.putExtra("KeyNUM", newId);
                        startActivity(intent);
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