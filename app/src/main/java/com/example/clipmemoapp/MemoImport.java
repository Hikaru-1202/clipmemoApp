package com.example.clipmemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;

import io.realm.Realm;

public class MemoImport extends AppCompatActivity {
    Realm mRealm;
    private Button backBtn;
//    private EditText EditTitle;
//    private EditText EditText;
//    private Editable getTitle;
//    private Editable Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo_import);

//        EditTitle = findViewById(R.id.editTitle);
//        EditText = findViewById(R.id.editText);

        mRealm = Realm.getDefaultInstance();
        backBtn = findViewById(R.id.returnButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Memo memo
                                = realm.createObject(Memo.class,0);
                        memo.title = "a";
                        memo.text = "a";
                        memo.category = 0;
                        memo.favorite_flag = 0;
                        memo.block_flag = 0;
                        memo.password_needed_flag = 0;
                        memo.memo_delete_flag = 0;
                        memo.made_date = new Date();
                        memo.updated_date = new Date();
                    }
                });
                Intent intent = new Intent(MemoImport.this,MemoDisplay.class);
                startActivity(intent);
            }
        });
    }
}