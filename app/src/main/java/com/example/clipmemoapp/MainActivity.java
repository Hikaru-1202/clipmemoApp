package com.example.clipmemoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    Realm mRealm;
    private Button importBtn;
//    private Button viewBtn;
    private Button userBtn;
    private long memoId = 0;
    private long ID_NUM = 0;
    private String memoTitle;
    private String memoText;

    ArrayList<String> memoList = new ArrayList<>();

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


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                memoList
        );
        list.setAdapter(adapter);
        registerForContextMenu(list);

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Memo> memos
                        = realm.where(Memo.class).findAll();
                for (Memo memo:
                     memos) {
                    memoTitle = memo.title;
                    memoText = memo.text;
                    ID_NUM = memo.memo_id;
                    memoList.add(memoId,memoTitle,memoText);
                    memoId++;
                }
                list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,View view, int position, long id){
                        ID_NUM = id;
                        Intent intent = new Intent(MainActivity.this,MemoDisplay.class);
                        intent.putExtra("KeyNUM", ID_NUM);
                        Log.v("MY_LOG","Main:"+String.valueOf(ID_NUM));
                        startActivity(intent);
                    }
                });

//                非同期の弊害？長押しで削除しようとするとアプリが落ちる
//                エラー：java.lang.IllegalStateException: This Realm instance has already been closed, making it unusable.

//                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                    @Override
//                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
//                        Log.v("MY_LOG", "id:"+String.valueOf(id));
//                        DialogFragment dialogFragment = new DeleteDialogFragment();
//                        Bundle args = new Bundle();
//                        args.putString("TITLE",memoTitle);
//                        args.putString("TEXT",memoText);
//                        args.putLong("KeyID",ID_NUM);
//                        dialogFragment.setArguments(args);
//
//                        dialogFragment.setCancelable(false);
//
//                        dialogFragment.show(getSupportFragmentManager(),"Delete_dialog");
//                        return false;
//                    }
//                });
            }
        });

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MemoImport.class);
                startActivity(intent);
            }
        });
        //ListView実装完了したため廃止
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        long itemId = item.getItemId();
        Log.v("MY_LOG", "itemId:"+String.valueOf(itemId));
        if(itemId == R.id.context_favorite) {

        } else if (itemId == R.id.context_category) {

        } else if (itemId == R.id.context_protect) {

        } else if (itemId == R.id.context_password) {

        } else if (itemId == R.id.context_delete) {
            Log.v("MY_LOG", "ID_NUM:"+String.valueOf(ID_NUM));
            DialogFragment dialogFragment = new DeleteDialogFragment();
            Bundle args = new Bundle();
//            memoList.get();
            args.putString("TITLE",memoTitle);
            args.putString("TEXT",memoText);
            args.putLong("KeyID",ID_NUM);
            dialogFragment.setArguments(args);

            dialogFragment.setCancelable(false);

            dialogFragment.show(getSupportFragmentManager(),"Delete_dialog");
        } else{
            return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }
}
