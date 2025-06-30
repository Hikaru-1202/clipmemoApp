package com.example.clipmemoapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PassDeleteDialogFragment extends DialogFragment {
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
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((UserMenu)requireActivity()).PassDelete(ID_NUM);
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
