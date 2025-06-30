package com.example.clipmemoapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class ImportDialogFragment extends DialogFragment {
    private String title;
    private String text;
    private String NoString;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        title = requireArguments().getString("TITLE");
        text = requireArguments().getString("TEXT");
        if (Objects.equals(title, "タイトル")){
            NoString = "No Text";
        }else {
            NoString = "No Title";
        }
        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(title+"の保存")
                .setMessage(text+"が入力されていません。\n"+text+"を入力せずに保存しますか？\n(未入力欄には["+NoString+"]が代入されます。)")
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MemoImport)requireActivity()).ImportAccept();
                    }
                })
                .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        ((MemoImport)requireActivity()).unImportAccept();
                    }
                })
                .create();
    }
}
