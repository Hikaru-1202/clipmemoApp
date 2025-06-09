package com.example.clipmemoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.realm.Realm;

public class DeleteAccept extends AppCompatActivity {

    Realm mRealm;
    private Button backBtn;
    private long ID_NUM = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_accept);

        mRealm = Realm.getDefaultInstance();

        ID_NUM = getIntent().getLongExtra("KeyNUM",0);
        Log.v("MY_LOG","ID:"+String.valueOf(ID_NUM));
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = realm.where(Memo.class)
                        .equalTo("memo_id",ID_NUM)
                        .findFirst();
                memo.deleteFromRealm();
            }
        });
        backBtn = findViewById(R.id.returnButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteAccept.this,MainActivity.class);
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