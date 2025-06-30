package com.example.clipmemoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MenuDialog extends DialogFragment {
    private String memoTitle;
    private String memoText;
    private long KeyID;
    final String[] Menu = {"お気に入り","保護","パスワード","削除"};

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        KeyID = requireArguments().getLong("KeyID");
        memoTitle = requireArguments().getString("TITLE");
        memoText = requireArguments().getString("TEXT");
        return new AlertDialog.Builder(requireActivity())
            .setItems(Menu, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0){

                    } else if (which == 1) {

                    } else if (which == 2) {

                    } else if (which == 3) {
                        DialogFragment dialogFragment = new DeleteDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("TITLE",memoTitle);
                        args.putString("TEXT",memoText);
                        args.putLong("KeyID",KeyID);
                        dialogFragment.setArguments(args);

                        dialogFragment.setCancelable(true);

                        dialogFragment.show(getFragmentManager(),"Delete_dialog");
                    }else {

                    }
                }
            })
            .show();
    }
}
