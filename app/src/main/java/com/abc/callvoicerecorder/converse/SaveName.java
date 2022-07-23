package com.abc.callvoicerecorder.converse;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.activity.MainActivity;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.db.Record;
import com.abc.callvoicerecorder.fragment.FragmentMain;
import com.abc.callvoicerecorder.constant.Constants;


public class SaveName implements Constants{
    private static View view;
    private static MainActivity mainActivity;

    public static void showSaveDescConfirmDialog(final Context context, final Record callRecordItem, final String descTitle, final String descBody) {
        AlertDialog alert = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setView(getView(context))
                .setPositiveButton(context.getString(R.string.yes_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveDesc(context, callRecordItem, descTitle, descBody);
                    }
                }).setCancelable(true)
                .setNegativeButton(context.getString(R.string.no_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mainBackPressed();
                    }
                }).create();

        alert.show();
    }

    private static View getView(Context context) {
        view = View.inflate(context, R.layout.dialog_delete_call_confirm, null);
        mainActivity = (MainActivity) context;
        return view;
    }

    private static void saveDesc(Context context, Record callRecordItem, String descTitle, String descBody) {
        try {
            callRecordItem.setRecordName(descTitle);
            try {
                FactoryHelper.getHelper().getDictaphoneRecordDAO().update(callRecordItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mainBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mainBackPressed() {
        mainActivity.showFragment(FragmentMain.newInstance());
    }

}
