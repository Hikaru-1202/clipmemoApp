package com.example.clipmemoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

import io.realm.Realm;

public class PassEdit extends AppCompatActivity {

    Realm mRealm;

    private Button backBtn;
    private Button checkBtn;
    private TextView old_text;
    private EditText old_pass;
    private EditText new_pass;
    private EditText check_pass;
    private TextView PassMsg;
    private String oldPass;
    private String newPass;
    private String checkPass;
    private String hashPass;
    private String DB_hashPass;
    private String Old_hashPass;
    private String Check_hashPass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pass_edit);

        PassMsg =findViewById(R.id.pass_miss_message);

        backBtn = findViewById(R.id.returnButton);
        checkBtn = findViewById(R.id.checkButton);
        new_pass = findViewById(R.id.import_new_pass);
        newPass = new_pass.getText().toString();
        check_pass = findViewById(R.id.import_check_pass);
        checkPass = check_pass.getText().toString();
        old_text = findViewById(R.id.old_pass_message);
        old_pass = findViewById(R.id.import_old_pass);

        mRealm = Realm.getDefaultInstance();

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number max = realm.where(Pass.class).max("pass_id");
                if (max != null){
                    Log.v("MY_LOG","パスワード登録アリ");
                    Pass pass = realm.where(Pass.class)
                            .equalTo("pass_id",0).findFirst();
                    DB_hashPass = pass.password;

                    checkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            oldPass = old_pass.getText().toString();
                            new_pass = findViewById(R.id.import_new_pass);
                            newPass = new_pass.getText().toString();
                            check_pass = findViewById(R.id.import_check_pass);
                            checkPass = check_pass.getText().toString();
                            Log.v("MY_LOG","newPass:"+newPass+"\ncheckPass:"+checkPass);
                            if (oldPass.isEmpty()&&newPass.isEmpty()&&checkPass.isEmpty()){
                                PassMsg.setText("全項目が入力されていません");
                            } else {
                                if (oldPass.isEmpty()&&newPass.isEmpty()){
                                    PassMsg.setText("以下の項目が入力されていません\n現在のパスワード\n新パスワード");
                                }else if (oldPass.isEmpty()&&checkPass.isEmpty()){
                                    PassMsg.setText("以下の項目が入力されていません\n現在のパスワード\n新パスワード再入力");
                                } else if (newPass.isEmpty()&&checkPass.isEmpty()){
                                    PassMsg.setText("以下の項目が入力されていません\n新パスワード\n新パスワード再入力");
                                } else if (oldPass.isEmpty()){
                                    PassMsg.setText("現在のパスワードが入力されていません");
                                } else if (newPass.isEmpty()) {
                                    PassMsg.setText("新しいパスワードが入力されていません");
                                }else if (checkPass.isEmpty()){
                                    PassMsg.setText("パスワードの再入力がされていません");
                                }else {
                                    if (newPass.length() < 8||newPass.length() > 32){
                                        PassMsg.setText("設定できるパスワードは8文字以上32文字以下です");
                                    }else {
                                        try {
                                            MessageDigest md = MessageDigest.getInstance("SHA-256");
                                            MessageDigest md1 = MessageDigest.getInstance("SHA-256");
                                            MessageDigest md2 = MessageDigest.getInstance("SHA-256");
                                            md.update(oldPass.getBytes());
                                            md1.update(newPass.getBytes());
                                            md2.update(checkPass.getBytes());
                                            byte[] hashBytes = md.digest();
                                            byte[] hashBytes1 = md1.digest();
                                            byte[] hashBytes2 = md2.digest();
                                            Old_hashPass = Base64.getEncoder().encodeToString(hashBytes);
                                            hashPass = Base64.getEncoder().encodeToString(hashBytes1);
                                            Check_hashPass = Base64.getEncoder().encodeToString(hashBytes2);
                                            Log.v("MY_LOG","DBのハッシュ値:"+DB_hashPass+"\n旧ハッシュ値:"+Old_hashPass+"\n新ハッシュ値:"+hashPass+"\n確認用ハッシュ値:"+Check_hashPass);
                                            if (Objects.equals(DB_hashPass, Old_hashPass)){
                                                if (Objects.equals(DB_hashPass, hashPass)){
                                                    PassMsg.setText("現在のパスワードと同じものが入力されています");
                                                }else {
                                                    if (Objects.equals(hashPass, Check_hashPass)){
                                                        mRealm.executeTransactionAsync(new Realm.Transaction() {
                                                            @Override
                                                            public void execute(Realm realm) {
                                                                Pass pass = realm.where(Pass.class)
                                                                        .equalTo("pass_id",0).findFirst();
                                                                pass.password = hashPass;
                                                            }
                                                        });
                                                        Intent intent = new Intent(PassEdit.this,UserMenu.class);
                                                        startActivity(intent);
                                                    }else {
                                                        PassMsg.setText("新しいパスワードと再入力したパスワードが一致しません");
                                                    }
                                                }
                                            }else {
                                                PassMsg.setText("現在のパスワードが一致していません");
                                            }
                                        } catch (NoSuchAlgorithmException e) {
                                            Intent intent = new Intent(PassEdit.this,MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }else {
                    Log.v("MY_LOG","パスワード登録ナシ");
                    old_text.setVisibility(View.GONE);
                    old_pass.setVisibility(View.GONE);
                    checkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new_pass = findViewById(R.id.import_new_pass);
                            newPass = new_pass.getText().toString();
                            check_pass = findViewById(R.id.import_check_pass);
                            checkPass = check_pass.getText().toString();
                            if (newPass.isEmpty()){
                                PassMsg.setText("新しいパスワードが入力されていません");
                            }else {
                                Log.v("MY_LOG","ハッシュ化する前の値:"+newPass);
                                try {
                                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                                    MessageDigest md1 = MessageDigest.getInstance("SHA-256");
                                    md.update(newPass.getBytes());
                                    md1.update(checkPass.getBytes());
                                    byte[] hashBytes = md.digest();
                                    byte[] hashBytes1 = md1.digest();
                                    hashPass = Base64.getEncoder().encodeToString(hashBytes);
                                    Check_hashPass = Base64.getEncoder().encodeToString(hashBytes1);
                                    Log.v("MY_LOG","ハッシュ値:"+hashPass+"\n確認用ハッシュ値:"+Check_hashPass);
                                    if (Objects.equals(hashPass, Check_hashPass)){
                                        mRealm.executeTransactionAsync(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                Pass pass
                                                        = realm.createObject(Pass.class,0);
                                                pass.password = hashPass;
                                                pass.non_active_flag = 0;
                                                pass.made_date = new Date();
                                            }
                                        });
                                        Intent intent = new Intent(PassEdit.this,UserMenu.class);
                                        startActivity(intent);
                                    }else {
                                        PassMsg.setText("再入力したパスワードと一致していません");
                                    }
                                } catch (NoSuchAlgorithmException e) {
                                    Intent intent = new Intent(PassEdit.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassEdit.this,UserMenu.class);
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