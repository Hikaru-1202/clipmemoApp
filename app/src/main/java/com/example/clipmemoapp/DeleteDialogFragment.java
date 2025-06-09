package com.example.clipmemoapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import io.realm.Realm;

public class DeleteDialogFragment extends DialogFragment {
    private String title;
    private String text;
    private long ID_NUM = 0;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ID_NUM = requireArguments().getLong("KeyID");
        title = requireArguments().getString("TITLE");
        text = requireArguments().getString("TEXT");
        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle("メモ:"+title+"の削除")
                .setMessage(text+"を削除しますか？")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)requireActivity()).DeleteAccept(ID_NUM);
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }
}
