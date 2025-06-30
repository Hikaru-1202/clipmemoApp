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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
//    private ImageView icon;
    private String KeyID;
    private String PassID;
//    private String passCheck;
    private final String[] question = {"-","好きな食べ物は？","旅行に行くならどこ？","母親の旧姓は？","好きなスポーツは？","休日は何をする？","願いが叶うなら何をお願いする？","一番落ち着く場所はどこ？","子供の頃憧れていた職業は？"};
    private ListView list;
    private SearchView searchView;
    private ArrayList<Map<String, Object>> memoList;
    private ArrayList<Map<String, Object>> filterList;
    private boolean filterSwitch = true;
    private int flag = 1;
    private int unflag = 0;

    //R.id.category,"CATEGORY",はリストの色で識別

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        filterSwitch = true;

//        icon = findViewById(R.id.passIcon);

        list = findViewById(R.id.List);
        memoList = new ArrayList<>();
        filterList = new ArrayList<>();
        registerForContextMenu(list);
        Log.v("MY_LOG", String.valueOf(list));

//        list.setTextFilterEnabled(true);
        searchView = findViewById(R.id.search);
//        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
//        searchView.setSubmitButtonEnabled(false);

        mRealm = Realm.getDefaultInstance();

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number max = realm.where(User.class).max("user_id");
                if (max == null){
                    User user
                            = realm.createObject(User.class,0);
                    user.question_id = 0;
                    user.keyword = null;
                    user.made_date = new Date();
                    user.updated_date = new Date();
                    Number max1 = realm.where(QuestionList.class).max("question_id");
                    if (max1 == null){
                        for (int i = 0; i < question.length; i++) {
                            QuestionList questionList
                                    = realm.createObject(QuestionList.class,i);
                            questionList.questions = question[i];
                        }
                    }
                }
            }
        });

        importBtn = findViewById(R.id.memo_import_Button);
        userBtn = findViewById(R.id.userButton);

        //月曜日（6/2）にレルム・リストの処理追加

        setMemoList();
        memoAdapt();

//        mRealm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//
////                adapter.notifyDataSetChanged();
//                RealmResults<Memo> memos
//                        = realm.where(Memo.class).findAll();
//                for (Memo memo:
//                        memos) {
//                    memoTitle = memo.title; //.get(0)
//                    memoText = memo.text; //.get(1)
//                    memoPass = memo.password_needed_flag; //.get(2)
//                    memoFavorite = memo.favorite_flag;  //.get(3)
//                    memoBlock = memo.block_flag;  //.get(4)
//                    ID_NUM = memo.memo_id; //.get(5)
//                    memoCategory = memo.category; //.get(6)
//
//                    Log.v("MY_LOG", "memo_id:"+String.valueOf(ID_NUM));
//
//                    Map<String, Object> memoField = new HashMap<>();
//                    memoField.put("title",memoTitle);
//                    memoField.put("text",memoText);
//                    if (memoPass == flag){
//                        memoField.put("passIcon","🔒");
//                    }else {
//                        memoField.put("passIcon"," ");
//                    }
//                    if (memoFavorite == flag){
//                        memoField.put("favoriteIcon","⭐");
//                    }else {
//                        memoField.put("favoriteIcon"," ");
//                    }
//                    if (memoBlock == flag){
//                        memoField.put("blockIcon","🛡");
//                    }else {
//                        memoField.put("blockIcon"," ");
//                    }
//                    memoField.put("pass",String.valueOf(memoPass));
//                    memoField.put("favorite",String.valueOf(memoFavorite));
//                    memoField.put("block",String.valueOf(memoBlock));
//                    memoField.put("memo_id",String.valueOf(ID_NUM));
//                    memoField.put("category",String.valueOf(memoCategory));
//
//                    memoList.add(memoField);
//                }
//            }
//        });



        //遷移方法を変えればDBの内容を変更する度に更新をする必要が無くなるかも…
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view, int position, long id){
                if (filterSwitch){
                    KeyID = String.valueOf(memoList.get(position).get("memo_id"));
                    PassID = String.valueOf(memoList.get(position).get("pass"));
                }else {
                    KeyID = String.valueOf(filterList.get(position).get("memo_id"));
                    PassID = String.valueOf(filterList.get(position).get("pass"));
                }
                mRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Number max = realm.where(Pass.class).max("pass_id");
                        if (Long.parseLong(PassID) != unflag && max != null) {
                            Intent intent = new Intent(MainActivity.this,PassImport.class);
                            intent.putExtra("KeyNUM",Long.parseLong(KeyID));
                            startActivity(intent);
                        } else if (Long.parseLong(PassID) != unflag && max == null) {
                            DialogFragment dialogFragment = new PassDialogFragment();

                            dialogFragment.setCancelable(false);

                            dialogFragment.show(getSupportFragmentManager(),"Pass_dialog");
                        } else {
                            Intent intent = new Intent(MainActivity.this,MemoDisplay.class);
                            intent.putExtra("KeyNUM",Long.parseLong(KeyID));
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    // 入力テキストに変更があったとき
                    @Override
                    public boolean onQueryTextChange(String s) {
                        filterList = new ArrayList<>();
                        if (s.isEmpty()) {
//                            list.clearTextFilter();
                            memoAdapt();
                            filterSwitch = true;
                        } else if (s.equals("お気に入り")) {
                            int i=0;
                            for (i = 0; i < memoList.size(); i++) {
                                if (String.valueOf(memoList.get(i).get("title")).contains(s)||String.valueOf(memoList.get(i).get("text")).contains(s)||memoList.get(i).get("favorite").equals(String.valueOf(1))){
                                    filterList.add(memoList.get(i));
                                }
                            }
                            filterAdapt();
                            filterSwitch = false;
                        } else {
//                            list.setFilterText(s);
//                            filterList = new ArrayList<>();
                            int i=0;
                            for (i = 0; i < memoList.size(); i++) {
                                if (String.valueOf(memoList.get(i).get("title")).contains(s)||String.valueOf(memoList.get(i).get("text")).contains(s)){
                                    filterList.add(memoList.get(i));
                                }
                            }
                            filterAdapt();
                            filterSwitch = false;
                        }
                        return false;
                    }

                    // 検索ボタンを押したとき
                    @Override
                    public boolean onQueryTextSubmit(String s) {
//
                        return false;
                    }
                }
        );

//        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Log.v("MY_LOG", "id:"+String.valueOf(id));
//                KeyID = String.valueOf(memoList.get(position).get("memo_id"));
//                memoTitle = String.valueOf(memoList.get(position).get("title"));
//                memoText = String.valueOf(memoList.get(position).get("text"));
//
//                DialogFragment dialogFragment = new MenuDialog();
//                Bundle args = new Bundle();
//                args.putString("TITLE",memoTitle);
//                args.putString("TEXT",memoText);
//                args.putLong("KeyID",Long.parseLong(KeyID));
//                dialogFragment.setArguments(args);
//
//                dialogFragment.setCancelable(false);
//
//                dialogFragment.show(getSupportFragmentManager(),"Delete_dialog");
//                return true;
//            }
//        });



        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MemoImport.class);
                startActivity(intent);
            }
        });

        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UserMenu.class);
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

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long itemId = item.getItemId();
        Log.v("MY_LOG", "itemId:"+String.valueOf(itemId));
//        mRealm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                Memo memo = realm.where(Memo.class)
//                        .equalTo("memo_id",ID_NUM)
//                        .findFirst();
//                memoBlock = memo.block_flag;
//            }
//        });
        if(itemId == R.id.context_favorite) {
            if (filterSwitch){
                FavoriteAccept(Long.parseLong(String.valueOf(memoList.get(info.position).get("memo_id"))));
//                memoList = new ArrayList<>();
//                setMemoList();
//                memoAdapt();
//                filterSwitch = true;
            }else {
                FavoriteAccept(Long.parseLong(String.valueOf(filterList.get(info.position).get("memo_id"))));
            }
        } else if (itemId == R.id.context_protect) {
            if (filterSwitch){
                BlockAccept(Long.parseLong(String.valueOf(memoList.get(info.position).get("memo_id"))));
            }else {
                BlockAccept(Long.parseLong(String.valueOf(filterList.get(info.position).get("memo_id"))));
            }
        } else if (itemId == R.id.context_password) {
            if (filterSwitch){
                PassAccept(Long.parseLong(String.valueOf(memoList.get(info.position).get("memo_id"))));
            }else {
                PassAccept(Long.parseLong(String.valueOf(filterList.get(info.position).get("memo_id"))));
            }
        } else if (itemId == R.id.context_delete) {
            if (filterSwitch){
                if (Integer.parseInt(String.valueOf(memoList.get(info.position).get("block"))) == unflag){
                    DialogFragment dialogFragment = new DeleteDialogFragment();
                    Bundle args = new Bundle();
//                        memoList.get();
                    args.putString("TITLE",String.valueOf(memoList.get(info.position).get("title")));
                    args.putString("TEXT",String.valueOf(memoList.get(info.position).get("text")));
                    args.putLong("KeyID", Long.parseLong(String.valueOf(memoList.get(info.position).get("memo_id"))));
                    dialogFragment.setArguments(args);

                    dialogFragment.setCancelable(false);

                    dialogFragment.show(getSupportFragmentManager(),"Menu_dialog");
                }else {
                    Toast.makeText(MainActivity.this,R.string.Block_memo,Toast.LENGTH_LONG).show();
                }
            }else {
                if (Integer.parseInt(String.valueOf(filterList.get(info.position).get("block"))) == 0){
                    DialogFragment dialogFragment = new DeleteDialogFragment();
                    Bundle args = new Bundle();
//                        memoList.get();
                    args.putString("TITLE",String.valueOf(filterList.get(info.position).get("title")));
                    args.putString("TEXT",String.valueOf(filterList.get(info.position).get("text")));
                    args.putLong("KeyID", Long.parseLong(String.valueOf(filterList.get(info.position).get("memo_id"))));
                    dialogFragment.setArguments(args);

                    dialogFragment.setCancelable(false);

                    dialogFragment.show(getSupportFragmentManager(),"Menu_dialog");
                }else {
                    Toast.makeText(MainActivity.this,R.string.Block_memo,Toast.LENGTH_LONG).show();
                }
            }
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

    public void FavoriteAccept(long ID_NUM) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = realm.where(Memo.class)
                        .equalTo("memo_id",ID_NUM)
                        .findFirst();
                if (memo.favorite_flag == unflag){
                    memo.favorite_flag = flag;
                }else {
                    memo.favorite_flag = unflag;
                }
            }
        });
//        memoList = new ArrayList<>();
//        setMemoList();
//        memoAdapt();
//        filterSwitch = true;
        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void DeleteAccept(long ID_NUM) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = realm.where(Memo.class)
                        .equalTo("memo_id",ID_NUM)
                        .findFirst();

                memo.deleteFromRealm();
            }
        });
//        memoList = new ArrayList<>();
//        setMemoList();
//        memoAdapt();
//        filterSwitch = true;
        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void BlockAccept(long ID_NUM) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = realm.where(Memo.class)
                        .equalTo("memo_id",ID_NUM)
                        .findFirst();
                if (memo.block_flag == unflag){
                    memo.block_flag = flag;
                }else {
                    memo.block_flag = unflag;
                }
            }
        });
//        memoList = new ArrayList<>();
//        setMemoList();
//        memoAdapt();
//        filterSwitch = true;
        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void PassAccept(long ID_NUM) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = realm.where(Memo.class)
                        .equalTo("memo_id",ID_NUM)
                        .findFirst();
                if (memo.password_needed_flag == unflag){
                    memo.password_needed_flag = flag;
                }else {
                    memo.password_needed_flag = unflag;
                }
            }
        });
//        memoList = new ArrayList<>();
//        setMemoList();
//        memoAdapt();
//        filterSwitch = true;
        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void setMemoList(){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

//                adapter.notifyDataSetChanged();
                RealmResults<Memo> memos
                        = realm.where(Memo.class).findAll();
                for (Memo memo:
                        memos) {
                    memoTitle = memo.title; //.get(0)
                    memoText = memo.text; //.get(1)
                    memoPass = memo.password_needed_flag; //.get(2)
                    memoFavorite = memo.favorite_flag;  //.get(3)
                    memoBlock = memo.block_flag;  //.get(4)
                    ID_NUM = memo.memo_id; //.get(5)
                    memoCategory = memo.category; //.get(6)

                    Log.v("MY_LOG", "memo_id:"+String.valueOf(ID_NUM));

                    Map<String, Object> memoField = new HashMap<>();
                    memoField.put("title",memoTitle);
                    if (memoPass == flag){
                        memoField.put("passIcon","🔒");
                        memoField.put("hiddentext","********************");
                    }else {
                        memoField.put("text",memoText);
                    }
                    if (memoFavorite == flag){
                        memoField.put("favoriteIcon","⭐");
                    }
                    if (memoBlock == flag){
                        memoField.put("blockIcon","🛡");
                    }
                    memoField.put("pass",String.valueOf(memoPass));
                    memoField.put("favorite",String.valueOf(memoFavorite));
                    memoField.put("block",String.valueOf(memoBlock));
                    memoField.put("memo_id",String.valueOf(ID_NUM));
                    memoField.put("category",String.valueOf(memoCategory));

                    memoList.add(memoField);
                }
            }
        });
    }

    public void memoAdapt(){
        list.setAdapter(new SimpleAdapter(
                this,
                memoList,
                R.layout.list_item,
//                new String[] {"title","text","pass","favorite","block","memo_id","category"},
                new String[] {"title","text","hiddentext","passIcon","favoriteIcon","blockIcon","pass","favorite","block","memo_id","category"},
                new int[] {R.id.title,R.id.text,R.id.hiddentext,R.id.passIcon,R.id.favoriteIcon,R.id.blockIcon}
        ));
    }
    public void filterAdapt(){
        list.setAdapter(new SimpleAdapter(
                this,
                filterList,
                R.layout.list_item,
                new String[] {"title","text","passIcon","favoriteIcon","blockIcon","pass","favorite","block","memo_id","category"},
                new int[] {R.id.title,R.id.text,R.id.passIcon,R.id.favoriteIcon,R.id.blockIcon}
        ));
    }

    public void passIntent(){
        Intent intent = new Intent(MainActivity.this, PassEdit.class);
        startActivity(intent);
    }
}
