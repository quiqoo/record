package com.abc.callvoicerecorder.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.abc.callvoicerecorder.dao.CallRecordDAO;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.dao.IgnoreContactDAO;
import com.abc.callvoicerecorder.db.IgnoreContact;
import com.abc.callvoicerecorder.dao.RecordDAO;
import com.abc.callvoicerecorder.db.Record;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.constant.Constants;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper implements Constants {
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    private CallRecordDAO callRecordDao = null;
    private IgnoreContactDAO ignoreContactDao = null;
    private RecordDAO dictaphoneRecordDAO = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, CallRecord.class);
            TableUtils.createTable(connectionSource, IgnoreContact.class);
            TableUtils.createTable(connectionSource, Record.class);
        } catch (SQLException e) {
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer,
                          int newVer) {
        try {
           if (oldVer < 2) {
            }
        } catch (Exception e) {
            Log.e(TAG, "error upgrading db " + DATABASE_NAME + "from ver " + oldVer);
            throw new RuntimeException(e);
        }
    }


    public CallRecordDAO getCallRecordDAO() throws SQLException {
        if (callRecordDao == null) {
            callRecordDao = new CallRecordDAO(getConnectionSource(), CallRecord.class);
        }
        return callRecordDao;
    }

    public IgnoreContactDAO getIgnoreContactDAO() throws SQLException {
        if (ignoreContactDao == null) {
            ignoreContactDao = new IgnoreContactDAO(getConnectionSource(), IgnoreContact.class);
        }
        return ignoreContactDao;
    }

    public RecordDAO getDictaphoneRecordDAO() throws SQLException {
        if (dictaphoneRecordDAO == null) {
            dictaphoneRecordDAO = new RecordDAO(getConnectionSource(), Record.class);
        }
        return dictaphoneRecordDAO;
    }

    @Override
    public void close() {
        super.close();
        callRecordDao = null;
        ignoreContactDao = null;
        dictaphoneRecordDAO = null;
    }

    public void addCallRecordItemDB(String callName, String callNumber, int callType, String path) {
        Calendar timeC = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(timeC.getTimeInMillis());
        timeC.set(Calendar.HOUR_OF_DAY, 0);
        timeC.set(Calendar.MINUTE, 0);
        timeC.set(Calendar.SECOND, 0);
        timeC.set(Calendar.MILLISECOND, 0);
        final long date = timeC.getTimeInMillis();
        try {
            CallRecord callRecordItem = new CallRecord(callName, callNumber, date, time, callType, path, false, "", "");
            FactoryHelper.getHelper().getCallRecordDAO().create(callRecordItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addIgnoreContactItemDB(Context context, String callNumber) {
        try {
            List<IgnoreContact> ignoreContactList = FactoryHelper.getHelper().getIgnoreContactDAO().getListByContainsNumber(callNumber);
            if (ignoreContactList.size() != 0)
                Toast.makeText(context, context.getString(R.string.ignore_contact_exists),
                        Toast.LENGTH_SHORT).show();
            else {
                IgnoreContact ignoreContactItem = new IgnoreContact(callNumber);
                FactoryHelper.getHelper().getIgnoreContactDAO().create(ignoreContactItem);
                Toast.makeText(context, callNumber + " - " + context.getString(R.string.ignore_contact_add),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDictaphoneRecordItemDB(String callName, String path) {
        Calendar timeC = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(timeC.getTimeInMillis());
        timeC.set(Calendar.HOUR_OF_DAY, 0);
        timeC.set(Calendar.MINUTE, 0);
        timeC.set(Calendar.SECOND, 0);
        timeC.set(Calendar.MILLISECOND, 0);
        final long date = timeC.getTimeInMillis();
        try {
            Record callRecordItem = new Record(callName, date, time, path, false, "", "");
            FactoryHelper.getHelper().getDictaphoneRecordDAO().create(callRecordItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}