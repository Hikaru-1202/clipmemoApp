package com.example.clipmemoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import io.realm.Realm;

public class UserMenu extends AppCompatActivity {
    Realm mRealm;
    private Button backBtn;
    private Button passBtn;
    private Button codeBtn;
    private Button deleteBtn;
    private Number pass_max;
    private Number code_max;
    private boolean Pass_Check = true;
    private boolean Code_Check = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_menu);
        backBtn = findViewById(R.id.returnButton);
        passBtn = findViewById(R.id.passButton);
        codeBtn = findViewById(R.id.codeButton);
        deleteBtn = findViewById(R.id.deleteButton);

        mRealm = Realm.getDefaultInstance();

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                pass_max = realm.where(Pass.class).max("pass_id");
                code_max = realm.where(Code.class).max("code_id");
                if (pass_max == null&&code_max == null){
                    Pass_Check = true;
                    Code_Check = true;
                }else if (pass_max == null){
                    Pass_Check = true;
                    Code_Check = false;
                }else if (code_max == null){
                    Pass_Check = false;
                    Code_Check = true;
                }else {
                    Pass_Check = false;
                    Code_Check = false;
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMenu.this,MainActivity.class);
                startActivity(intent);
            }
        });
        passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMenu.this, PassEdit.class);
                startActivity(intent);
            }
        });
        codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMenu.this,CodeSetting.class);
                startActivity(intent);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Pass_Check&&Code_Check){
                    Toast.makeText(UserMenu.this,R.string.Pass_Code_nothing_message,Toast.LENGTH_LONG).show();
                }else if (Pass_Check){
                    DialogFragment dialogFragment = new PassDeleteDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("TITLE","認証・質問キーの削除");
                    args.putString("TEXT","認証・質問キーの削除を行います。\n削除しますか？");
                    args.putLong("KeyID",1);
                    dialogFragment.setArguments(args);
                    dialogFragment.setCancelable(false);
                    dialogFragment.show(getSupportFragmentManager(),"Pass_dialog");
                }else if (Code_Check){
                    DialogFragment dialogFragment = new PassDeleteDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("TITLE","パスワードの削除");
                    args.putString("TEXT","パスワードの削除を行います。\nこの操作を行うと、パスワードを再登録しない限りパスワードを設定したメモを表示させることができなくなります。\n削除しますか？");
                    args.putLong("KeyID",2);
                    dialogFragment.setArguments(args);
                    dialogFragment.setCancelable(false);
                    dialogFragment.show(getSupportFragmentManager(),"Pass_dialog");
                }else {
                    DialogFragment dialogFragment = new PassDeleteDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("TITLE","パスワードと認証・質問キーの削除");
                    args.putString("TEXT","パスワードと認証・質問キーの削除を行います。\nこの操作を行うと、パスワードを再登録しない限りパスワードを設定したメモを表示させることができなくなります。\n削除しますか？");
                    args.putLong("KeyID",3);
                    dialogFragment.setArguments(args);
                    dialogFragment.setCancelable(false);
                    dialogFragment.show(getSupportFragmentManager(),"Pass_dialog");
                }
            }
        });
    }
    public void PassDelete(long i){
        if (i == 1){
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Code code = realm.where(Code.class)
                            .equalTo("code_id",0)
                            .findFirst();

                    code.deleteFromRealm();
                }
            });
            Code_Check = true;
            Toast.makeText(UserMenu.this,R.string.Code_delete_message,Toast.LENGTH_LONG).show();
        }else if (i == 2){
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Pass pass = realm.where(Pass.class)
                            .equalTo("pass_id",0)
                            .findFirst();

                    pass.deleteFromRealm();
                }
            });
            Pass_Check = true;
            Toast.makeText(UserMenu.this,R.string.Pass_delete_message,Toast.LENGTH_LONG).show();
        }else {
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Code code = realm.where(Code.class)
                            .equalTo("code_id",0)
                            .findFirst();

                    code.deleteFromRealm();
                }
            });
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Pass pass = realm.where(Pass.class)
                            .equalTo("pass_id",0)
                            .findFirst();

                    pass.deleteFromRealm();
                }
            });
            Code_Check = true;
            Pass_Check = true;
            Toast.makeText(UserMenu.this,R.string.Pass_Code_delete_message,Toast.LENGTH_LONG).show();
        }
    }
}