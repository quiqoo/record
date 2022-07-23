package com.abc.callvoicerecorder.converse;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.utils.PermissionHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;


public class StartAlertWindow implements Constants {
    private static View view;

    public static void showStartAttentionDialog(final Context context, final String label, final String body) {
        SharedPreferencesFile.initSharedReferences(context);
        AlertDialog alert = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setView(getView(context, label, body))
                .setPositiveButton(context.getString(R.string.apply_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveSettings(context);
                    }
                }).setCancelable(false)
                .setNegativeButton(context.getString(R.string.cancel_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create();

        alert.show();
    }

    private static View getView(Context context, String label, String body) {
        view = View.inflate(context, R.layout.dialog_start_attention, null);
        ((TextView) view.findViewById(R.id.label)).setText(label);
        ((TextView) view.findViewById(R.id.desc)).setText(body);
        return view;
    }

    private static void saveSettings(Context context) {
        if (!PermissionHelper.isSystemAlertPermissionGranted(context)) {
            PermissionHelper.requestSystemAlertPermission(context);
        }
    }

}
