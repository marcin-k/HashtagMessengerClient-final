package com.marcin.hashtagmessengerclient.child;
/**************************************************************
 * Used to show the alert like prompts (when deleting sos alerts
 * and reviewing the childs messages)
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class AlertDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Error title")
                .setMessage("Error msg")
                .setPositiveButton("ok", null);

        AlertDialog dialogue = builder.create();
        return dialogue;
    }
}
