package com.abc.callvoicerecorder.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.converse.Delete;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;

public class NotificationsHelper implements Constants {


    public static Notification getNotification(Context context, String title, String message, int pushId, Class aClass, boolean isAutoCancel) {
        String NOTIFICATION_CHANNEL_ID = context.getPackageName();
        String channelName = context.getResources().getString(R.string.app_name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }

        int iconSmallId = 0;
        if (SharedPreferencesFile.isMainNotificationActive())
            iconSmallId = R.mipmap.ic_status_bar;
        else
            iconSmallId = R.mipmap.ic_status_bar_disable;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(iconSmallId);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(isAutoCancel);
        builder.setOngoing(true);
        Intent appIntent = new Intent(context, aClass);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, MAIN_ACTIVITY_ID, appIntent, 0);
        builder.setContentIntent(pendingIntent);
        Notification result = builder.build();
        return result;
    }

    public static Notification getNotificationSecondary(Context context, String title, String message, int pushId, Class aClass, boolean isAutoCancel) {
        String NOTIFICATION_CHANNEL_ID = context.getPackageName();
        String channelName = context.getResources().getString(R.string.app_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(R.mipmap.ic_status_bar_done);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        Intent appIntent = new Intent(context, aClass);
        appIntent.putExtra(PUSH_ID, pushId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, pushId, appIntent, 0);
        builder.setContentIntent(pendingIntent);
        Notification result = builder.build();
        return result;
    }

    public static Notification getNotificationError(Context context, String title, String message, Class aClass) {
        String NOTIFICATION_CHANNEL_ID = context.getPackageName();
        String channelName = context.getResources().getString(R.string.app_name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(R.mipmap.ic_status_bar_error);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        Intent appIntent = new Intent(context, aClass);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, MAIN_ACTIVITY_ID, appIntent, 0);
        builder.setContentIntent(pendingIntent);
        Notification result = builder.build();
        return result;
    }


    public static void showDotsPopupMenu(View v, final Context context, final CallRecord callRecordItem, final int status, final boolean isFavorite) {
        final PopupMenu popupMenu = new PopupMenu(context, v);
        if (!isFavorite)
            popupMenu.inflate(R.menu.call_record_popup_add_menu);
        else
            popupMenu.inflate(R.menu.call_record_popup_remove_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        Delete.showDeleteCallConfirmDialog(context, callRecordItem, status);
                        return true;
                    case R.id.action_favorite:
                        callRecordItem.setFavorite(!isFavorite);
                        try {
                            FactoryHelper.getHelper().getCallRecordDAO().update(callRecordItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        FragmentsCallHelper.updateAllLists(context);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
    }

}
