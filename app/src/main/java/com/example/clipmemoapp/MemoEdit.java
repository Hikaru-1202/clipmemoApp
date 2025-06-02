package com.example.clipmemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class MemoEdit extends AppCompatActivity {
    Realm mRealm;
    private Button backBtn;
    private EditText E_Title;
    private EditText E_Text;
    private String G_Title;
    private String G_Text;
    private long ID_NUM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo_edit);

        ID_NUM = getIntent().getLongExtra("KeyNUM",0);

        ((EditText) findViewById(R.id.editText)).setMovementMethod(new ScrollingMovementMethod());

        mRealm = Realm.getDefaultInstance();

        E_Title = findViewById(R.id.editTitle);
        E_Text = findViewById(R.id.editText);

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo =realm.where(Memo.class)
                        .equalTo("memo_id",ID_NUM).findFirst();
                G_Title = memo.title;
                G_Text = memo.text;

                E_Title.setText(G_Title);
                E_Text.setText(G_Text);
            }
        });

        backBtn = findViewById(R.id.returnButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                G_Title = E_Title.getText().toString();
                G_Text = E_Text.getText().toString();

                mRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
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
                    }
                });
                Intent intent = new Intent(MemoEdit.this,MemoDisplay.class);
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