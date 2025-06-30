package com.example.clipmemoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;

public class CodeSetting extends AppCompatActivity {
    Realm mRealm;
    private int oldQuestion;
    private int newQuestion;
    private Button backBtn;
    private Button checkBtn;
    private TextView old_question_text;
    private TextView old_code_text;
//    private TextView new_question_text;
//    private TextView new_code_text;
    private TextView code_miss_text;
    private Spinner old_question_spinner;
    private Spinner new_question_spinner;
    private EditText old_code_edit;
    private EditText new_code_edit;
    private String old_code;
    private String new_code;
    private ArrayAdapter<String> adapter;

    //DBの連携が完了し次第、question配列は削除
//    private String[] question = {"-","好きな食べ物は？","旅行に行くならどこ？","母親の旧姓は？","好きなスポーツは？","休日は何をする？","願いが叶うなら何をお願いする？","一番落ち着く場所はどこ？","子供の頃憧れていた職業は？"};
    private String[] Q_List;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_code_setting);

        Q_List = new String[9];

        old_question_spinner = findViewById(R.id.old_questionList);
        new_question_spinner = findViewById(R.id.new_questionList);
        old_question_text = findViewById(R.id.old_question_message);
        old_code_text = findViewById(R.id.import_code_message);
        old_code_edit = findViewById(R.id.import_code);
        new_code_edit = findViewById(R.id.new_import_code);

        //TextView
        old_question_text.setVisibility(View.GONE);
        //プルダウン
        old_question_spinner.setVisibility(View.GONE);
        old_code_text.setVisibility(View.GONE);
        old_code_edit.setVisibility(View.GONE);


        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
//                question
                Q_List
        );
        old_question_spinner.setAdapter(adapter);
        new_question_spinner.setAdapter(adapter);

        mRealm = Realm.getDefaultInstance();

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
//                Number max = realm.where(QuestionList.class).max("question_id");
                for (int i = 0; i < 9; i++) {
                    QuestionList questionList
                            = realm.where(QuestionList.class)
                            .equalTo("question_id",i)
                            .findFirst();
                    Q_List[i] = questionList.questions;
                }
            }
        });



//        new_question_text = findViewById(R.id.new_question_message);
//        new_question_spinner = findViewById(R.id.new_questionList);
//        old_question_spinner = findViewById(R.id.old_questionList);
//        new_code_text = findViewById(R.id.new_code_message);

        code_miss_text = findViewById(R.id.code_miss_message);

        checkBtn = findViewById(R.id.checkButton);

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number max = realm.where(Code.class).max("code_id");
                if (max != null){
                    //TextView
                    old_question_text.setVisibility(View.VISIBLE);
                    //プルダウン
                    old_question_spinner.setVisibility(View.VISIBLE);
                    old_code_text.setVisibility(View.VISIBLE);
                    old_code_edit.setVisibility(View.VISIBLE);
//                    old_question_spinner = findViewById(R.id.old_questionList);
                    old_question_spinner.setAdapter(adapter);
//                    new_question_spinner = findViewById(R.id.new_questionList);
                    new_question_spinner.setAdapter(adapter);
                    Log.v("MY_LOG","キー登録アリ");
                    Code code = realm.where(Code.class)
                            .equalTo("code_id",0).findFirst();
                    int questionId = code.question_id;
                    String DB_keyword = code.keyword;

                    checkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                            old_question_spinner = findViewById(R.id.old_questionList);
//                            new_question_spinner = findViewById(R.id.new_questionList);
//                            old_question_spinner.setAdapter(adapter);
//                            new_question_spinner.setAdapter(adapter);

                            old_code = old_code_edit.getText().toString();
                            new_code = new_code_edit.getText().toString();
                            oldQuestion = old_question_spinner.getSelectedItemPosition();
                            newQuestion = new_question_spinner.getSelectedItemPosition();
                            if (old_code.isEmpty()||new_code.isEmpty()){
                                code_miss_text.setText("認証キーが未入力です");
                            } else if (oldQuestion == 0||newQuestion == 0){
                                code_miss_text.setText("質問が選択されていません");
                            }else {//以下より認証・質問キーの整合性チェック
                                if (questionId == oldQuestion){
                                    if (Objects.equals(DB_keyword, new_code)){
                                        code_miss_text.setText("現在の認証・質問キーと同じものが入力されています");
                                    }else {
                                        if (Objects.equals(DB_keyword, old_code)){
                                            if (!forbiddenCheck(new_code)){
                                                code_miss_text.setText("新しく設定する認証キーに禁則文字が含まれています\n入力できる文字:ひらがな,全角カタカナ,漢字,半角英数字");
                                            }else {
                                                mRealm.executeTransactionAsync(new Realm.Transaction() {
                                                    @Override
                                                    public void execute(Realm realm) {
                                                        Code code = realm.where(Code.class)
                                                                .equalTo("code_id",0).findFirst();
                                                        code.keyword = new_code;
                                                        code.question_id = newQuestion;
                                                    }
                                                });
                                                Intent intent = new Intent(CodeSetting.this,UserMenu.class);
                                                startActivity(intent);
                                            }
                                        }else {
                                            code_miss_text.setText("現在の認証キーが一致しません");
                                        }
                                    }
                                }else {
                                    code_miss_text.setText("現在の質問キーが一致していません");
                                }
                            }
                        }
                    });
                }else {
                    Log.v("MY_LOG","キー登録ナシ");
                    for (int i = 0; i < Q_List.length; i++) {
                        Log.v("MY_LOG","Q_List["+i+"]:"+Q_List[i]);
                    }
//                    mRealm.beginTransaction();
//                    QuestionList questionList=realm.createObject(QuestionList.class);
//                    questionList.question_id = 1;
//                    questionList.questions = question[0];
//                    realm.commitTransaction();
//                    old_question_spinner = findViewById(R.id.old_questionList);
//                    new_question_spinner = findViewById(R.id.new_questionList);
//                    old_question_spinner.setAdapter(adapter);
//                    new_question_spinner.setAdapter(adapter);

//                    //TextView
//                    old_question_text.setVisibility(View.GONE);
//                    //プルダウン
//                    old_question_spinner.setVisibility(View.GONE);
//                    old_code_text.setVisibility(View.GONE);
//                    old_code_edit.setVisibility(View.GONE);

                    checkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new_code = new_code_edit.getText().toString();
                            newQuestion = new_question_spinner.getSelectedItemPosition();
                            if (new_code.isEmpty()){
                                code_miss_text.setText("認証キーが入力されていません");
                            }else {
                                if (!forbiddenCheck(new_code)){
                                    code_miss_text.setText("認証キーに禁則文字が含まれています\n入力できる文字:ひらがな,全角カタカナ,漢字,半角英数字");
                                }else {
                                    if (newQuestion != 0){
                                        mRealm.executeTransactionAsync(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                Code code
                                                        = realm.createObject(Code.class,0);
                                                code.question_id = newQuestion;
                                                code.keyword = new_code;
                                                Log.v("MY_LOG", "質問キー:"+String.valueOf(newQuestion));
                                                Log.v("MY_LOG", "認証キー:"+new_code);
                                            }
                                        });
                                        Intent intent = new Intent(CodeSetting.this,UserMenu.class);
                                        startActivity(intent);
                                    }else {
                                        code_miss_text.setText("質問が選択されていません");
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
        backBtn = findViewById(R.id.returnButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CodeSetting.this,UserMenu.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }
    private static boolean forbiddenCheck(String forbiddenCode){
        Pattern pattern = Pattern.compile("^[ぁ-んァ-ヶー\\u30a0-\\u30ff\\u3040-\\u309f\\u3005-\\u3006\\u30e0-\\u9fcf\0-9a-zA-Z]+$");
        Matcher m = pattern.matcher(forbiddenCode);
        return m.find();
    }
}