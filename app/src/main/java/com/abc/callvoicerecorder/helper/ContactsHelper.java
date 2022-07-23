package com.abc.callvoicerecorder.helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;


public class ContactsHelper {

    public static String getContactName(final String phoneNumber, Context context)
    {
        String contactName = "";
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    contactName = cursor.getString(0);
                }
                cursor.close();
            }
            return contactName;
        } catch (Exception e) {
            e.printStackTrace();
            return contactName;
        }
    }

    public static String getContactPhoto(final String phoneNumber, Context context)
    {
        String contactPhoto = "";
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            String[] projection = new String[]{ContactsContract.PhoneLookup.PHOTO_URI};

            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    contactPhoto = cursor.getString(0);
                }
                cursor.close();
            }

            if (contactPhoto == null)
                contactPhoto = "";

            return contactPhoto;
        } catch (Exception e) {
            e.printStackTrace();
            return contactPhoto;
        }
    }

    public static String getContactId(final String phoneNumber, Context context)
    {
        String contactName = "";
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            String[] projection = new String[]{ContactsContract.PhoneLookup._ID};

            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    contactName = cursor.getString(0);
                }
                cursor.close();
            }
            return contactName;
        } catch (Exception e) {
            e.printStackTrace();
            return contactName;
        }
    }
}
