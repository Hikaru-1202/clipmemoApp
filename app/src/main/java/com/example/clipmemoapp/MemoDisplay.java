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

import org.w3c.dom.Text;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MemoDisplay extends AppCompatActivity {
    Realm mRealm;
    private Button backBtn;
    private TextView TitleView;
    private TextView TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo_display);

        mRealm = Realm.getDefaultInstance();

        TitleView = findViewById(R.id.titleView);
        TextView = findViewById(R.id.textView);

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Memo> memos
                        = realm.where(Memo.class).findAll();
                TextView.setText("取得");
                for (Memo memo:
                        memos) {
                    String text = TextView.getText() + "\n"
                            + memo.toString();
                    TextView.setText(text);
                }
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }
}