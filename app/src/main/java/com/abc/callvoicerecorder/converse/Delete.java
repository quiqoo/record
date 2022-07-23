package com.abc.callvoicerecorder.converse;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.activity.MainActivity;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.fragment.FragmentMain;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.helper.FragmentsCallHelper;


public class Delete implements Constants{
    private static View view;
    private static TextView textView;
    private static int deleteStatus;
    private static MainActivity mainActivity;

    public static void showDeleteCallConfirmDialog(final Context context, final CallRecord callRecordItem, final int status) {
        deleteStatus = status;
        AlertDialog alert = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setView(getView(context))
                .setPositiveButton(context.getString(R.string.apply_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCall(context, callRecordItem);
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
        if (deleteStatus == DELETE_AUDIO_STATUS_RIGHT)
            textView.setText(context.getString(R.string.dialog_delete_call_confirm));
        else if (deleteStatus == DELETE_AUDIO_STATUS_ERROR)
            textView.setText(context.getString(R.string.dialog_delete_call_confirm_error));
        else if (deleteStatus == DELETE_AUDIO_STATUS_RIGHT_CLOSE)
            textView.setText(context.getString(R.string.dialog_delete_call_confirm));
        return view;
    }

    private static void deleteCall(Context context, CallRecord callRecordItem) {
        try {
            FactoryHelper.getHelper().getCallRecordDAO().deleteItemAndFile(callRecordItem.getId());
            FragmentsCallHelper.updateAllLists(context);
            if (deleteStatus == DELETE_AUDIO_STATUS_RIGHT_CLOSE)
                mainActivity.showFragment(FragmentMain.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
