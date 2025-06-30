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
import androidx.fragment.app.DialogFragment;

import java.util.Date;

import io.realm.Realm;

public class MemoImport extends AppCompatActivity {
    Realm mRealm;
    private Button backBtn;
    private Button saveBtn;
    private EditText I_Title;
    private EditText I_Text;
    private String G_Title;
    private String G_Text;
    private String C_Title;
    private String C_Text;
    private String D_Title = "タイトル";
    private String D_Text = "テキスト";
    private String D_Memo = "タイトルとテキスト";
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
                    C_Title = memo.title;
                    C_Text = memo.text;

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

                if (G_Title.isBlank()&&G_Text.isBlank()){
                    if (ID_NUM < 0){
                        Intent intent = new Intent(MemoImport.this,MainActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(MemoImport.this,MemoDisplay.class);
                        intent.putExtra("KeyNUM", ID_NUM);
                        startActivity(intent);
                    }
                }else {
                    if (ID_NUM < 0){
                        if (!G_Title.isBlank()&&!G_Text.isBlank()){
                            CancelDialog(D_Memo);
                        } else if (!G_Title.isBlank()||!G_Text.isBlank()){
                            if (!G_Title.isBlank()){
                                CancelDialog(D_Title);
                            }else{
                                CancelDialog(D_Text);
                            }
                        }
//                        ImportAccept();
                    }else {
//                        if (G_Title.isBlank()||G_Text.isBlank()){
//                            unEditAccept(ID_NUM);
//                        }else {
//                            if (!G_Title.equals(C_Title)||!G_Text.equals(C_Text)){
//                                if (!G_Title.equals(C_Title)&&!G_Text.equals(C_Text)){
//                                    EditDialog(D_Memo,ID_NUM);
//                                } else if (!G_Title.equals(C_Title)){
//                                    EditDialog(D_Title,ID_NUM);
//                                }else{
//                                    EditDialog(D_Text,ID_NUM);
//                                }
//                            }else {
//                                unEditAccept(ID_NUM);
//                            }
//                        }
                        if (!G_Title.equals(C_Title)||!G_Text.equals(C_Text)){
                            if (G_Title.isBlank()||G_Text.isBlank()){
                                if (G_Title.isBlank()&&G_Text.isBlank()){
                                    BackDialog(D_Memo,ID_NUM);
                                } else if (G_Title.isBlank()){
                                    BackDialog(D_Title,ID_NUM);
                                }else{
                                    BackDialog(D_Text,ID_NUM);
                                }
                            }else {
                                if (!G_Title.equals(C_Title)&&!G_Text.equals(C_Text)){
                                    EditDialog(D_Memo,ID_NUM);
                                } else if (!G_Title.equals(C_Title)){
                                    EditDialog(D_Title,ID_NUM);
                                }else{
                                    EditDialog(D_Text,ID_NUM);
                                }
                            }
                        }else {
                            IntentAccept(ID_NUM);
                        }
                    }
                }
            }
        });

        saveBtn = findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                G_Title = I_Title.getText().toString();
                G_Text = I_Text.getText().toString();
                if (ID_NUM < 0){
                    if (!G_Title.isBlank()||!G_Text.isBlank()){
                        if (!G_Title.isBlank()&&!G_Text.isBlank()){
                            ImportAccept();
                        } else if (!G_Title.isBlank()) {
                            ImportDialog(D_Title,D_Text);
                        }else {
                            ImportDialog(D_Text,D_Title);
                        }
                    }else {
                        unImportAccept();
                    }
                }else {
                    if (!G_Title.isBlank()&&!G_Text.isBlank()){
                        EditAccept(ID_NUM);
                    } else if (!G_Title.isBlank()||!G_Text.isBlank()){
                        if (!G_Title.isBlank()){
                            BlankDialog(D_Text,ID_NUM);
                        }else{
                            BlankDialog(D_Title,ID_NUM);
                        }
                    }else {
                        IntentAccept(ID_NUM);
                    }
                }
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }

    public void CancelDialog(String ImpString){
        DialogFragment dialogFragment = new CancelDialogFragment();
        Bundle args = new Bundle();
        args.putString("TITLE",ImpString);
        args.putString("TEXT",ImpString);
        dialogFragment.setArguments(args);

//        dialogFragment.setCancelable(false);

        dialogFragment.show(getSupportFragmentManager(),"Cancel_dialog");
    }

    public void ImportDialog(String title,String text){
        DialogFragment dialogFragment = new ImportDialogFragment();
        Bundle args = new Bundle();
        args.putString("TITLE",title);
        args.putString("TEXT",text);
        dialogFragment.setArguments(args);

        dialogFragment.setCancelable(false);

        dialogFragment.show(getSupportFragmentManager(),"Import_dialog");
    }

    public void EditDialog(String EditString,long ID_NUM){
        DialogFragment dialogFragment = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putString("TITLE",EditString);
        args.putString("TEXT",EditString);
        args.putLong("KeyID",ID_NUM);
        dialogFragment.setArguments(args);

//        dialogFragment.setCancelable(false);

        dialogFragment.show(getSupportFragmentManager(),"Edit_dialog");
    }

    public void BlankDialog(String EditString,long ID_NUM){
        DialogFragment dialogFragment = new BlankDialogFragment();
        Bundle args = new Bundle();
        args.putString("TITLE",EditString);
        args.putString("TEXT",EditString);
        args.putLong("KeyID",ID_NUM);
        dialogFragment.setArguments(args);

//        dialogFragment.setCancelable(false);

        dialogFragment.show(getSupportFragmentManager(),"Blank_dialog");
    }

    public void BackDialog(String EditString,long ID_NUM){
        DialogFragment dialogFragment = new BackDialogFragment();
        Bundle args = new Bundle();
        args.putString("TITLE",EditString);
        args.putString("TEXT",EditString);
        args.putLong("KeyID",ID_NUM);
        dialogFragment.setArguments(args);

//        dialogFragment.setCancelable(false);

        dialogFragment.show(getSupportFragmentManager(),"Back_dialog");
    }

    public void ImportAccept() {
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
                if (G_Title.isBlank()){
                    memo.title = "No Title";
                }else {
                    memo.title = G_Title;
                }
                if (G_Text.isBlank()){
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
            }
        });
    }
    public void EditAccept(long ID_NUM) {
        G_Title = I_Title.getText().toString();
        G_Text = I_Text.getText().toString();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = realm.where(Memo.class)
                        .equalTo("memo_id",ID_NUM)
                        .findFirst();
                if (G_Title.isBlank()){
                    memo.title = "No Title";
                }else {
                    memo.title = G_Title;
                }
                if (G_Text.isBlank()){
                    memo.text = "No Text";
                }else {
                    memo.text = G_Text;
                }
            }
        });
        IntentAccept(ID_NUM);
    }

    public void IntentAccept(long ID_NUM) {
        Intent intent = new Intent(MemoImport.this,MemoDisplay.class);
        intent.putExtra("KeyNUM", ID_NUM);
        startActivity(intent);
    }
    public void unImportAccept(){
        Intent intent = new Intent(MemoImport.this,MainActivity.class);
        startActivity(intent);
    }
}