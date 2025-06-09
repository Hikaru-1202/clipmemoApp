package com.example.clipmemoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    Realm mRealm;
    private Button importBtn;
//    private Button viewBtn;
    private Button userBtn;
    private long ID_NUM = 0;
    private long codeId;
    private String memoTitle;
    private String memoText;
    private int memoCategory = 0;
    private int memoFavorite = 0;
    private int memoBlock = 0;
    private int memoPass = 0;
    private String KeyID;
    private String PassID;

//    private static final String[] MEMO = {"ID","TITLE","TEXT","FAVORITE","BLOCK","PASSWORD"};
//    private static final int[] TO = {R.id.idNumber,R.id.title,R.id.text,R.id.favoriteIcon,R.id.blockIcon,R.id.passIcon};
    //R.id.category,"CATEGORY",はリストの色で識別

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();

        importBtn = findViewById(R.id.memo_import_Button);
//        viewBtn = findViewById(R.id.memo_view_Button);
        userBtn = findViewById(R.id.userButton);

        //月曜日（6/2）にレルム・リストの処理追加

        ListView list = findViewById(R.id.List);

//        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this,memoField,R.layout,);


//        直接
        ArrayList<ArrayList<String>> memoList = new ArrayList<ArrayList<String>>();
        ArrayAdapter<ArrayList<String>> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                memoList
        );

//        間接
//        ArrayList<ArrayList<String>> memoList = new ArrayList<ArrayList<String>>();
//        ArrayAdapter<ArrayList<String>> adapter = new ArrayAdapter<>(
//                this,
//                R.layout.list_item,
//                memoList
//        );

        list.setAdapter(adapter);

        registerForContextMenu(list);

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                adapter.notifyDataSetChanged();
                RealmResults<Memo> memos
                        = realm.where(Memo.class).findAll();
                for (Memo memo:
                     memos) {
                    ID_NUM = memo.memo_id; //.get(0)
                    memoTitle = memo.title; //.get(1)
                    memoText = memo.text; //.get(2)
                    memoCategory = memo.category; //.get(3)
                    memoFavorite = memo.favorite_flag;  //.get(4)
                    memoBlock = memo.block_flag;  //.get(5)
                    memoPass = memo.password_needed_flag; //.get(6)



                    Log.v("MY_LOG", "memo_id:"+String.valueOf(ID_NUM));


                    ArrayList<String> memoField = new ArrayList<String>();
                    memoField.add(String.valueOf(ID_NUM));
                    memoField.add(memoTitle);
                    memoField.add(memoText);
                    memoField.add(String.valueOf(memoCategory));
                    memoField.add(String.valueOf(memoFavorite));
                    memoField.add(String.valueOf(memoBlock));
                    memoField.add(String.valueOf(memoPass));

                    memoList.add(memoField);
//                    Log.v("MY_LOG", "memoList."+ID_NUM+":"+memoList.get((int) ID_NUM));
//                    Log.v("MY_LOG", "memoListID."+ID_NUM+":"+memoList.get((int) ID_NUM).get(0));
                }

                list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,View view, int position, long id){
                        KeyID = memoList.get(position).get(0);
                        PassID = memoList.get(position).get(6);
                        Log.v("MY_LOG","ブレイクポイント");
                        mRealm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Number max = realm.where(Pass.class).max("pass_id");
                                if (Long.parseLong(PassID) != 0 && max != null) {
                                    Intent intent = new Intent(MainActivity.this,PassImport.class);
                                    intent.putExtra("KeyNUM",Long.parseLong(KeyID));
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(MainActivity.this,MemoDisplay.class);
                                    intent.putExtra("KeyNUM",Long.parseLong(KeyID));
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                });

//                非同期の弊害？長押しで削除しようとするとアプリが落ちる
//                エラー：java.lang.IllegalStateException: This Realm instance has already been closed, making it unusable.

                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Log.v("MY_LOG", "id:"+String.valueOf(id));
                        KeyID = memoList.get(position).get(0);
                        memoTitle = memoList.get(position).get(1);
                        memoText = memoList.get(position).get(2);
                        DialogFragment dialogFragment = new DeleteDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("TITLE",memoTitle);
                        args.putString("TEXT",memoText);
                        args.putLong("KeyID",Long.parseLong(KeyID));
                        dialogFragment.setArguments(args);

                        dialogFragment.setCancelable(false);

                        dialogFragment.show(getSupportFragmentManager(),"Delete_dialog");
                        return true;
                    }
                });
            }
        });

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MemoImport.class);
                startActivity(intent);
            }
        });
        //ListViewが実装完了したため廃止
//        viewBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,MemoDisplay.class);
//                intent.putExtra("KeyNUM", ID_NUM);
//                startActivity(intent);
//            }
//        });
        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UserMenu.class);
                startActivity(intent);
            }
        });
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
//        super.onCreateContextMenu(menu, view, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.context_menu, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item){
//        long itemId = item.getItemId();
//        Log.v("MY_LOG", "itemId:"+String.valueOf(itemId));
//        if(itemId == R.id.context_favorite) {
//
//        } else if (itemId == R.id.context_category) {
//
//        } else if (itemId == R.id.context_protect) {
//
//        } else if (itemId == R.id.context_password) {
//
//        } else if (itemId == R.id.context_delete) {
//            Log.v("MY_LOG", "ID_NUM:"+String.valueOf(ID_NUM));
//            DialogFragment dialogFragment = new DeleteDialogFragment();
//            Bundle args = new Bundle();
////            memoList.get();
//            args.putString("TITLE",memoTitle);
//            args.putString("TEXT",memoText);
//            args.putLong("KeyID",ID_NUM);
//            dialogFragment.setArguments(args);
//
//            dialogFragment.setCancelable(false);
//
//            dialogFragment.show(getSupportFragmentManager(),"Delete_dialog");
//        } else{
//            return super.onContextItemSelected(item);
//        }
//        return true;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }

    public void DeleteAccept(long ID_NUM) {
        Intent intent = new Intent(MainActivity.this,DeleteAccept.class);
        intent.putExtra("KeyNUM", ID_NUM);
        startActivity(intent);
    }
}
