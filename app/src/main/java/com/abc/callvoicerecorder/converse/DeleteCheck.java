package com.abc.callvoicerecorder.converse;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import java.util.List;
import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.activity.MainActivity;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.constant.Constants;


public class DeleteCheck implements Constants{
    private static View view;
    private static TextView textView;
    private static MainActivity mainActivity;
    private static OnClickListener mOnClick;

    public interface OnClickListener {
        void onClick(List<CallRecord> callRecordItemList);
    }

    public static void showDeleteCheckCallConfirmDialog(final Context context, final List<CallRecord> callRecordItemList, OnClickListener onClick) {
        mOnClick = onClick;
        AlertDialog alert = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setView(getView(context))
                .setPositiveButton(context.getString(R.string.apply_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mOnClick.onClick(callRecordItemList);
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

        mainActivity = (MainActivity) context;
        textView = (TextView) view.findViewById(R.id.edit_text_pass);
        textView.setText(context.getString(R.string.dialog_delete_all_check));
        return view;
    }

}
