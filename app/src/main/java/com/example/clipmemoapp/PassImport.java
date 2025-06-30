package com.example.clipmemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

import io.realm.Realm;

public class PassImport extends AppCompatActivity {
    Realm mRealm;

    private Button backBtn;
    private Button checkBtn;
    private Button codeBtn;
    private EditText impPass;
    private TextView PassMsg;
    private String Password;
    private String hashPass;
    private String DB_pass;
    private long ID_NUM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pass_import);
        ID_NUM = getIntent().getLongExtra("KeyNUM",0);

        PassMsg =findViewById(R.id.pass_miss_message);

        mRealm = Realm.getDefaultInstance();


        impPass = findViewById(R.id.import_pass);
        Password = impPass.getText().toString();
        backBtn = findViewById(R.id.returnButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassImport.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Pass pass = realm.where(Pass.class)
                        .equalTo("pass_id",0).findFirst();
                DB_pass = pass.password;
                checkBtn = findViewById(R.id.checkButton);
                checkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Password = impPass.getText().toString();
                        try {
                            MessageDigest md = MessageDigest.getInstance("SHA-256");
                            md.update(Password.getBytes());
                            byte[] hashBytes = md.digest();
                            hashPass = Base64.getEncoder().encodeToString(hashBytes);
                            Log.v("MY_LOG","ハッシュ値:"+hashPass+"\nDBハッシュ値:"+DB_pass);
                            if (Objects.equals(hashPass, DB_pass)){
                                Intent intent = new Intent(PassImport.this,MemoDisplay.class);
                                intent.putExtra("KeyNUM",ID_NUM);
                                startActivity(intent);
                            }else {
                                PassMsg.setText("パスワードが一致していません");
                            }
                        } catch (NoSuchAlgorithmException e) {
                            Intent intent = new Intent(PassImport.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number max = realm.where(Code.class).max("code_id");
                if (max != null){
                    codeBtn = findViewById(R.id.codeButton);
                    codeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PassImport.this,CodeImport.class);
                            intent.putExtra("KeyNUM",ID_NUM);
                            startActivity(intent);
                        }
                    });
                }else {
                    codeBtn = findViewById(R.id.codeButton);
                    codeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(PassImport.this,R.string.NULL_code,Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }
}