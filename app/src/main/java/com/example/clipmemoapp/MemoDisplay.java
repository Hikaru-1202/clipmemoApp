package com.example.clipmemoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.DialogFragment;

import io.realm.Realm;

public class MemoDisplay extends AppCompatActivity {
    Realm mRealm;
    private Button backBtn;
    private Button editBtn;
    private TextView TitleView;
    private TextView TextView;
    private long ID_NUM = 0;
    private int memoBlock = 0;
    private String title;
    private String text;
    private int pass_flag;
    private int block_flag;
    private int favorite_flag;

    private final int flag = 1;
    private final int unflag = 0;


//    GestureDetector mDetector;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo_display);

        registerForContextMenu(findViewById(R.id.displaytextView));
//        mDetector = new GestureDetector(this, (GestureDetector.OnGestureListener) this);

        ID_NUM = getIntent().getLongExtra("KeyNUM",0);
        Log.v("MY_LOG","Display:"+String.valueOf(ID_NUM));

        ((TextView) findViewById(R.id.displaytextView)).setMovementMethod(new ScrollingMovementMethod());

        mRealm = Realm.getDefaultInstance();

        TitleView = findViewById(R.id.titleView);
        TextView = findViewById(R.id.displaytextView);

        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = realm.where(Memo.class)
                        .equalTo("memo_id",ID_NUM).findFirst();

                title = memo.title;
                text = memo.text;
                block_flag = memo.block_flag;
                pass_flag = memo.password_needed_flag;
                favorite_flag = memo.favorite_flag;

                TitleView.setText(title);
                TextView.setText(text);
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
        editBtn = findViewById(R.id.editButton);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoDisplay.this,MemoImport.class);
                intent.putExtra("KeyNUM", ID_NUM);
                startActivity(intent);
            }
        });
    }
//    public boolean onDoubleTap(MotionEvent e) {
//        Toast.makeText(this, "called onDoubleTap()", Toast.LENGTH_LONG).show();
//        return false;
//    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_context, menu);
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
            FavoriteAccept(ID_NUM);
//        } else if (itemId == R.id.context_category) {
        } else if (itemId == R.id.context_protect) {
            BlockAccept(ID_NUM);
        } else if (itemId == R.id.context_password) {
            PassAccept(ID_NUM);
        } else if (itemId == R.id.context_delete) {
            if (block_flag == unflag){
                DialogFragment dialogFragment = new DisplayDialogFragment();
                Bundle args = new Bundle();
//                        memoList.get();
                args.putString("TITLE",title);
                args.putString("TEXT",text);
                args.putLong("KeyID",ID_NUM);
                dialogFragment.setArguments(args);

                dialogFragment.setCancelable(false);

                dialogFragment.show(getSupportFragmentManager(),"Display_dialog");
            }else {
                Toast.makeText(MemoDisplay.this,R.string.Block_memo,Toast.LENGTH_LONG).show();
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
//                favorite_flag = memo.favorite_flag;
                if (favorite_flag == unflag){
                    memo.favorite_flag = flag;
                }else {
                    memo.favorite_flag = unflag;
                }
                favorite_flag = memo.favorite_flag;
            }
        });
        if (favorite_flag == unflag){
            Toast.makeText(MemoDisplay.this,R.string.Favorite_message,Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(MemoDisplay.this,R.string.Unfavorite_message,Toast.LENGTH_LONG).show();
        }
//        Intent intent = new Intent(MemoDisplay.this,MainActivity.class);
//        startActivity(intent);
    }
    public void BlockAccept(long ID_NUM) {
        Log.v("MY_LOG","test:"+title);
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = realm.where(Memo.class)
                        .equalTo("memo_id",ID_NUM)
                        .findFirst();
//                block_flag = memo.block_flag;
                if (block_flag == unflag){
                    memo.block_flag = flag;
//                    Toast.makeText(MemoDisplay.this,R.string.Block_message,Toast.LENGTH_LONG).show();
                }else {
                    memo.block_flag = unflag;
//                    Toast.makeText(MemoDisplay.this,R.string.Unblock_message,Toast.LENGTH_LONG).show();
                }
                block_flag = memo.block_flag;
            }
        });
        if (block_flag == 0){
            Toast.makeText(MemoDisplay.this,R.string.Block_message,Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(MemoDisplay.this,R.string.Unblock_message,Toast.LENGTH_LONG).show();
        }
//        Intent intent = new Intent(MemoDisplay.this,MainActivity.class);
//        startActivity(intent);
    }

    public void PassAccept(long ID_NUM) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = realm.where(Memo.class)
                        .equalTo("memo_id",ID_NUM)
                        .findFirst();
//                pass_flag = memo.password_needed_flag;
                if (pass_flag == unflag){
                    memo.password_needed_flag = flag;
//                    Toast.makeText(MemoDisplay.this,R.string.Pass_needed_message,Toast.LENGTH_LONG).show();
                }else {
                    memo.password_needed_flag = unflag;
//                    Toast.makeText(MemoDisplay.this,R.string.Pass_unneeded_message,Toast.LENGTH_LONG).show();
                }
                pass_flag = memo.password_needed_flag;
            }
        });
        if (pass_flag == 0){
            Toast.makeText(MemoDisplay.this,R.string.Pass_needed_message,Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(MemoDisplay.this,R.string.Pass_unneeded_message,Toast.LENGTH_LONG).show();
        }
//        Intent intent = new Intent(MemoDisplay.this,MainActivity.class);
//        startActivity(intent);
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
        Intent intent = new Intent(MemoDisplay.this,MainActivity.class);
        startActivity(intent);
    }
}

