package com.abc.callvoicerecorder.converse;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.db.IgnoreContact;
import com.abc.callvoicerecorder.constant.Constants;


public class DeleteIgnoreContact implements Constants{
    private static View view;
    private static TextView textView;
    private static OnClickListener mOnClick;

    public interface OnClickListener {
        void onClick(IgnoreContact ignoreContactItem);
    }

    public static void showDeleteIgnoreContactConfirmDialog(final Context context, final IgnoreContact ignoreContactItem, OnClickListener onClick) {
        mOnClick = onClick;
        AlertDialog alert = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setView(getView(context))
                .setPositiveButton(context.getString(R.string.apply_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mOnClick.onClick(ignoreContactItem);
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

    private static View getView(Context context) {
        view = View.inflate(context, R.layout.dialog_delete_call_confirm, null);

        textView = (TextView) view.findViewById(R.id.edit_text_pass);
        textView.setText(context.getString(R.string.ignore_list_delete_confirm));
        return view;
    }


}
