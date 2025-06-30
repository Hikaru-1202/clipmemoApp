package com.example.clipmemoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;

public class CodeImport extends AppCompatActivity {
    Realm mRealm;
    private EditText code_edit;
    private TextView code_miss_text;
    private Button backBtn;
    private Button checkBtn;
    private String impCode;
    private String DB_keyword;
    private int impQuestion;

    private Spinner questionList;

    private long ID_NUM;
    private int questionId;

    //DBの連携が完了し次第、question配列は削除
    private String[] question;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_code_import);
        ID_NUM = getIntent().getLongExtra("KeyNUM",0);

        question = new String[9];

        questionList = findViewById(R.id.questionList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                question
        );
        questionList.setAdapter(adapter);

        mRealm = Realm.getDefaultInstance();

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number max = realm.where(QuestionList.class).max("question_id");
                for (int i = 0; i < 9; i++) {
                    QuestionList questionList
                            = realm.where(QuestionList.class)
                            .equalTo("question_id",i)
                            .findFirst();
                    question[i] = questionList.questions;
                }
            }
        });



        code_edit = findViewById(R.id.import_code);

        code_miss_text = findViewById(R.id.code_miss_message);

        backBtn = findViewById(R.id.returnButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CodeImport.this,PassImport.class);
                startActivity(intent);
            }
        });
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
//                Number max = realm.where(Code.class).max("code_id");
//                if (max != null){
//
//                }
                Code code = realm.where(Code.class)
                        .equalTo("code_id",0).findFirst();
                questionId = code.question_id;
                DB_keyword = code.keyword;
                checkBtn = findViewById(R.id.checkButton);
                checkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        impCode = code_edit.getText().toString();
                        impQuestion = questionList.getSelectedItemPosition();
                        if (Objects.equals(impCode, DB_keyword)){
                            if (questionId == impQuestion){
                                Intent intent = new Intent(CodeImport.this,MemoDisplay.class);
                                intent.putExtra("KeyNUM",ID_NUM);
                                startActivity(intent);
                            }else {
                                code_miss_text.setText("選択している質問キーが一致していません");
                            }
                        }else {
                            code_miss_text.setText("認証キーが一致していません");
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