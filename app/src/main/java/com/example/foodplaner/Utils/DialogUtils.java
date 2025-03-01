package com.example.foodplaner.Utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.example.foodplaner.R;

public class DialogUtils {
    public static void showConfirmationDialog(Context context, String message,
                                              DialogInterface.OnClickListener positiveListener) {
        new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle("Confirmation")
                .setMessage(message)
                .setPositiveButton("Yes", positiveListener)
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .create()
                .show();
    }
}