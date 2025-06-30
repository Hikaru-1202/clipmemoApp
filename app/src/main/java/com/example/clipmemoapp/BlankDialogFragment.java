package com.example.clipmemoapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class BlankDialogFragment extends DialogFragment {
    private String title;
    private String text;
    private long ID_NUM = 0;
    private String NoString;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ID_NUM = requireArguments().getLong("KeyID");
        title = requireArguments().getString("TITLE");
        text = requireArguments().getString("TEXT");
        if (Objects.equals(title, "タイトル")){
            NoString = "\n(タイトル未入力時、タイトルには[No Title]が代入されます。)";
        }else if (Objects.equals(title,"テキスト")){
            NoString = "\n(テキスト未入力時、テキストには[No Text]が代入されます。)";
        }else {
//            NoString = "\n(両方未入力時、タイトルには[No Title]、\nテキストには[No Text]が代入されます。)";
        }
        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(title+"の未入力")
                .setMessage(text+"未入力です。\n"+text+"未入力のまま保存しますか？"+NoString)
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MemoImport)requireActivity()).EditAccept(ID_NUM);
                    }
                })
                .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        ((MemoImport)requireActivity()).unEditAccept(ID_NUM);
                    }
                })
                .create();
    }
}
