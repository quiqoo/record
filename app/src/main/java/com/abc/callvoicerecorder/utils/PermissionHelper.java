package com.abc.callvoicerecorder.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

public class PermissionHelper {

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isSystemAlertPermissionGranted(Context context) {
        final boolean result = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context);
        return result;
    }

    public static void requestSystemAlertPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;
        if (context != null && context.getPackageName() != null) {
            final String packageName = context == null ? context.getPackageName() : context.getPackageName();
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName));
            context.startActivity(intent);
        }
    }
}
