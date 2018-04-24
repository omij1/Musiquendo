package com.example.mimo.musiquendo.Dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.mimo.musiquendo.R;

public class SimpleDialog extends DialogFragment {

    public interface DialogListener {
        void crearDialog();
    }

    private DialogListener listener;
    private static final String TITLE = "Title";
    private static final String TEXT = "Text";

    public SimpleDialog() {
        super();
    }

    public static SimpleDialog newInstance(String title, String text){
        SimpleDialog dialog = new SimpleDialog();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putString(TEXT, text);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();

        return new AlertDialog.Builder(getActivity(), R.style.SimpleDialog)
                .setIcon(R.drawable.ic_signal_wifi_off)
                .setTitle(arguments.getString(TITLE))
                .setMessage(arguments.getString(TEXT))
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                })
                .create();
    }
}
