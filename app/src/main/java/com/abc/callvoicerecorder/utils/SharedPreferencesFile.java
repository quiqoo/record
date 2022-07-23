package com.abc.callvoicerecorder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesFile {

    private static final String APP_SHARED_PREFS = SharedPreferencesFile.class.getSimpleName();
    private static final String IS_ENCODER_TYPE_SEND = "IS_ENCODER_TYPE_SEND";
    private static final String IS_ENCODER_TYPE_CHECK = "IS_ENCODER_TYPE_CHECK";
    private static final String IS_MAIN_NOTIFICATION_ACTIVE = "IS_MAIN_NOTIFICATION_ACTIVE";
    private static final String BUTTON_GRAVITY = "BUTTON_GRAVITY";
    private static final String LAST_POS_X = "LAST_POS_X";
    private static final String LAST_POS_Y = "LAST_POS_Y";
    private static final String PREF_SAVE_FILE = "PREF_SAVE_FILE";
    private static final String PREF_FILE_NAME = "PREF_FILE_NAME";
    private static final String PREF_DIR_NAME = "PREF_DIR_NAME";
    private static final String PREF_DIR_PATH = "PREF_DIR_PATH";
    private static final String PREF_DIR_PATH_ABS = "PREF_DIR_PATH_ABS";
    private static final String PREF_SHOW_SEED = "PREF_SHOW_SEED";
    private static final String PREF_SHOW_PHONE_NUMBER = "PREF_SHOW_PHONE_NUMBER";
    private static final String PREF_AUDIO_SOURCE = "PREF_AUDIO_SOURCE";
    private static final String PREF_AUDIO_ENCODER = "PREF_AUDIO_ENCODER";
    private static final String PREF_OUTPUT_FORMAT = "PREF_OUTPUT_FORMAT";

    private static final String IS_ENCODER_TYPE_CRASH = "IS_ENCODER_TYPE_CRASH";

    private static final String LAST_SEARCH_NAME = "LAST_SEARCH_NAME";
    private static final String SORT_BY = "SORT_BY";

    private static final String LAST_RECORD_ID = "LAST_RECORD_ID";
    private static final String CALL_TYPE_RECORD = "CALL_TYPE_RECORD";
    private static final String APP_PASSWORD = "APP_PASSWORD";

    private static final String ACTION_AFTER_CALL = "ACTION_AFTER_CALL";
    private static final String IS_START_ATTENTION_SHOWED = "IS_START_ATTENTION_SHOWED";

    private static final String LANG_TYPE = "LANG_TYPE";
    private static final String IS_SETTINGS_MAX_CALL_VOLUME = "IS_SETTINGS_MAX_CALL_VOLUME";

    private static final String IS_AUDIO_SOURCE_AUTO = "IS_AUDIO_SOURCE_AUTO";
    private static final String IS_WIDGET_ACTIVE = "IS_WIDGET_ACTIVE";
    private static final String IS_IS_WIDGET_ATTENTION_SHOWED = "IS_IS_WIDGET_ATTENTION_SHOWED";
    private static final String IS_SYSTEM_ALERT_WINDOW_SHOWED = "IS_SYSTEM_ALERT_WINDOW_SHOWED";

    private static final String POST_CALL_TIME = "POST_CALL_TIME";

    private static final String THEME_NUMBER = "THEME_NUMBER";



    private static SharedPreferences sPref;

    public static void initSharedReferences(Context context) {
        if (sPref == null) {
            sPref = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        }
    }


    public static boolean isEncoderTypeSend() {
        return sPref.getBoolean(IS_ENCODER_TYPE_SEND, false);
    }

    public static void setIsEncoderTypeSend(boolean number) {
        Editor ed = sPref.edit();
        ed.putBoolean(IS_ENCODER_TYPE_SEND, number);
        ed.commit();
    }

    public static boolean isEncoderTypeCheck() {
        return sPref.getBoolean(IS_ENCODER_TYPE_CHECK, false);
    }

    public static void setIsEncoderTypeCheck(boolean number) {
        Editor ed = sPref.edit();
        ed.putBoolean(IS_ENCODER_TYPE_CHECK, number);
        ed.commit();
    }

    public static boolean isMainNotificationActive() {
        return sPref.getBoolean(IS_MAIN_NOTIFICATION_ACTIVE, true);
    }

    public static void setIsMainNotificationActive(boolean number) {
        Editor ed = sPref.edit();
        ed.putBoolean(IS_MAIN_NOTIFICATION_ACTIVE, number);
        ed.commit();
    }

    public static boolean isFileSave() {
        return sPref.getBoolean(PREF_SAVE_FILE, true);
    }

    public static void setIsFileSave(boolean number) {
        Editor ed = sPref.edit();
        ed.putBoolean(PREF_SAVE_FILE, number);
        ed.commit();
    }

    public static String getFileName() {
        return sPref.getString(PREF_FILE_NAME, "");
    }

    public static void setFileName(String number) {
        Editor ed = sPref.edit();
        ed.putString(PREF_FILE_NAME, number);
        ed.commit();
    }

    public static String getDirName() {
        return sPref.getString(PREF_DIR_NAME, "");
    }

    public static void setDirName(String number) {
        Editor ed = sPref.edit();
        ed.putString(PREF_DIR_NAME, number);
        ed.commit();
    }

    public static String getDirPath() {
        return sPref.getString(PREF_DIR_PATH, "");
    }

    public static void setDirPath(String number) {
        Editor ed = sPref.edit();
        ed.putString(PREF_DIR_PATH, number);
        ed.commit();
    }

    public static String getDirPathAbs() {
        return sPref.getString(PREF_DIR_PATH_ABS, "");
    }

    public static void setDirPathAbs(String number) {
        Editor ed = sPref.edit();
        ed.putString(PREF_DIR_PATH_ABS, number);
        ed.commit();
    }

    public static boolean isShowSeed() {
        return sPref.getBoolean(PREF_SHOW_SEED, true);
    }

    public static void setIsShowSeed(boolean number) {
        Editor ed = sPref.edit();
        ed.putBoolean(PREF_SHOW_SEED, number);
        ed.commit();
    }

    public static boolean isShowNumber() {
        return sPref.getBoolean(PREF_SHOW_PHONE_NUMBER, true);
    }

    public static void setIsShowNumber(boolean number) {
        Editor ed = sPref.edit();
        ed.putBoolean(PREF_SHOW_PHONE_NUMBER, number);
        ed.commit();
    }

    public static int getAudioSource() {
        return sPref.getInt(PREF_AUDIO_SOURCE, 7);
    }

    public static void setAudioSource(int number) {
        Editor ed = sPref.edit();
        ed.putInt(PREF_AUDIO_SOURCE, number);
        ed.commit();
    }

    public static int getAudioEncoder() {
        return sPref.getInt(PREF_AUDIO_ENCODER, 1);
    }

    public static void setAudioEncoder(int number) {
        Editor ed = sPref.edit();
        ed.putInt(PREF_AUDIO_ENCODER, number);
        ed.commit();
    }

    public static int getOutputFormat() {
        return sPref.getInt(PREF_OUTPUT_FORMAT, 4);
    }

    public static void setOutputFormat(int number) {
        Editor ed = sPref.edit();
        ed.putInt(PREF_OUTPUT_FORMAT, number);
        ed.commit();
    }

    public static boolean isEncoderTypeCrash() {
        return sPref.getBoolean(IS_ENCODER_TYPE_CRASH, false);
    }

    public static void setIsEncoderTypeCrash(boolean number) {
        Editor ed = sPref.edit();
        ed.putBoolean(IS_ENCODER_TYPE_CRASH, number);
        ed.commit();
    }

    public static String getLastSearchName() {
        return sPref.getString(LAST_SEARCH_NAME, "");
    }

    public static void setLastSearchName(String number) {
        Editor ed = sPref.edit();
        ed.putString(LAST_SEARCH_NAME, number);
        ed.commit();
    }

    public static int getSortBy() {
        return sPref.getInt(SORT_BY, 0);
    }

    public static void setSortBy(int number) {
        Editor ed = sPref.edit();
        ed.putInt(SORT_BY, number);
        ed.commit();
    }

    public static int getCallTypeRecord() {
        return sPref.getInt(CALL_TYPE_RECORD, 0);
    }

    public static void setCallTypeRecord(int number) {
        Editor ed = sPref.edit();
        ed.putInt(CALL_TYPE_RECORD, number);
        ed.commit();
    }

    public static String getAppPassword() {
        return sPref.getString(APP_PASSWORD, "");
    }

    public static void setAppPassword(String number) {
        Editor ed = sPref.edit();
        ed.putString(APP_PASSWORD, number);
        ed.commit();
    }

    public static int getActionAfterCall() {
        return sPref.getInt(ACTION_AFTER_CALL, 0);
    }

    public static void setActionAfterCall(int number) {
        Editor ed = sPref.edit();
        ed.putInt(ACTION_AFTER_CALL, number);
        ed.commit();
    }

    public static boolean isStartAttentionShowed() {
        return sPref.getBoolean(IS_START_ATTENTION_SHOWED, false);
    }

    public static void setIsStartAttentionShowed(boolean number) {
        Editor ed = sPref.edit();
        ed.putBoolean(IS_START_ATTENTION_SHOWED, number);
        ed.commit();
    }

    public static String getLangType() {
        return sPref.getString(LANG_TYPE, "default");
    }

    public static void setLangType(String number) {
        Editor ed = sPref.edit();
        ed.putString(LANG_TYPE, number);
        ed.commit();
    }

    public static boolean isSettingsMaxCallVolume() {
        return sPref.getBoolean(IS_SETTINGS_MAX_CALL_VOLUME, false);
    }

    public static void setIsSettingsMaxCallVolume(boolean number) {
        Editor ed = sPref.edit();
        ed.putBoolean(IS_SETTINGS_MAX_CALL_VOLUME, number);
        ed.commit();
    }

    public static boolean isAudioSourceAuto() {
        return sPref.getBoolean(IS_AUDIO_SOURCE_AUTO, true);
    }

    public static void setIsAudioSourceAuto(boolean number) {
        Editor ed = sPref.edit();
        ed.putBoolean(IS_AUDIO_SOURCE_AUTO, number);
        ed.commit();
    }

    public static int getButtonGravity() {
        return sPref.getInt(BUTTON_GRAVITY, 0);
    }

    public static void setButtonGravity(int number) {
        Editor ed = sPref.edit();
        ed.putInt(BUTTON_GRAVITY, number);
        ed.commit();
    }

    public static int getLastButtonPositionX() {
        return sPref.getInt(LAST_POS_X, 0);
    }

    public static void setLastButtonPositionX(int number) {
        Editor ed = sPref.edit();
        ed.putInt(LAST_POS_X, number);
        ed.commit();
    }

    public static int getLastButtonPositionY() {
        return sPref.getInt(LAST_POS_Y, 0);
    }

    public static void setLastButtonPositionY(int number) {
        Editor ed = sPref.edit();
        ed.putInt(LAST_POS_Y, number);
        ed.commit();
    }

    public static boolean isWidgetActive() {
        return sPref.getBoolean(IS_WIDGET_ACTIVE, true);
    }

    public static void setIsWidgetActive(boolean number) {
        Editor ed = sPref.edit();
        ed.putBoolean(IS_WIDGET_ACTIVE, number);
        ed.commit();
    }

    public static boolean isWidgetAttentionShowed() {
        return sPref.getBoolean(IS_IS_WIDGET_ATTENTION_SHOWED, false);
    }

    public static void setIsWidgetAttentionShowed(boolean number) {
        Editor ed = sPref.edit();
        ed.putBoolean(IS_IS_WIDGET_ATTENTION_SHOWED, number);
        ed.commit();
    }

    public static int getThemeNumber() {
        return sPref.getInt(THEME_NUMBER, 0);
    }

    public static void setThemeNumber(int number) {
        Editor ed = sPref.edit();
        ed.putInt(THEME_NUMBER, number);
        ed.commit();
    }

    public static boolean isSystemAlertWindowShowed() {
        return sPref.getBoolean(IS_SYSTEM_ALERT_WINDOW_SHOWED, false);
    }

    public static void setIsSystemAlertWindowShowed(boolean number) {
        Editor ed = sPref.edit();
        ed.putBoolean(IS_SYSTEM_ALERT_WINDOW_SHOWED, number);
        ed.commit();
    }

    public static int getPostCallTime() {
        return sPref.getInt(POST_CALL_TIME, 0);
    }

    public static void setPostCallTime(int number) {
        Editor ed = sPref.edit();
        ed.putInt(POST_CALL_TIME, number);
        ed.commit();
    }


}
