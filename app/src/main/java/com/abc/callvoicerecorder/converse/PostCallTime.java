package com.abc.callvoicerecorder.converse;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;

public class PostCallTime implements Constants {
    private static View view;
    private static EditText fileNameEditText;
    private static OnClickListener mOnClick;

    public interface OnClickListener {
        void onClick(int size);
    }

    public static void showPostCallTimeDialog(final Context context, OnClickListener onClick ) {
        mOnClick = onClick;
        AlertDialog alert = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setView(getView(context))
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

    private static View getView(final Context context) {
        view = View.inflate(context, R.layout.dialog_post_call_time, null);
        ((TextView) view.findViewById(R.id.label)).setText(context.getString(R.string.settings_post_call_time_desc));
        fileNameEditText = (EditText) view.findViewById(R.id.file_name_edit_text);
        fileNameEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        fileNameEditText.setText(String.valueOf(SharedPreferencesFile.getPostCallTime()/1000));
        fileNameEditText.requestFocus();

        return view;
    }

    private static void saveSettings(Context context) {
        if (fileNameEditText.getText() != null && !String.valueOf(fileNameEditText.getText()).equals("")) {
            int value = Integer.parseInt(String.valueOf(fileNameEditText.getText()));
            if (value >= 0 && value <= 20) {
                SharedPreferencesFile.setPostCallTime(value * 1000);
                mOnClick.onClick(value);
            } else
                Toast.makeText(context, context.getString(R.string.dialog_post_call_time_error),
                        Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.dialog_post_call_time_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
