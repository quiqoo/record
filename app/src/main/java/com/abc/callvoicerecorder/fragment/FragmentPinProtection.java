package com.abc.callvoicerecorder.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.helper.DatabaseHelper;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;

public class FragmentPinProtection extends FragmentBase implements View.OnClickListener, Constants, ConstantsColor {
    private View rootView;
    private TextView title;
    private ImageButton btnBackspace;
    private ImageButton btnCancel;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private int count = 0;

    public static final int PIN_SET = 0;
    public static final int PIN_CONFIRM = 1;
    private int status = PIN_SET;
    private String password = "";
    private String confirm = "";
    private int typePin = 0;
    private int recordId = -1;
    private boolean verify = false;
    private boolean cancel = false;
    private boolean accept = false;

    public static Fragment newInstance(int typePin, int recordId) {
        Fragment fragment = new FragmentPinProtection();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_PIN, typePin);
        bundle.putInt(RECORD_ID, recordId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferencesFile.initSharedReferences(listenerActivity);
        rootView = inflater.inflate(R.layout.fragment_pin, container, false);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(TYPE_PIN) && bundle.containsKey(RECORD_ID)) {
            typePin = bundle.getInt(TYPE_PIN);
            recordId = bundle.getInt(RECORD_ID);
        }

        if (typePin == TYPE_PIN_CONFIRM) {
            status = PIN_CONFIRM;
            password = SharedPreferencesFile.getAppPassword();
            verify = true;
        } else {
            password = "";
            status = PIN_SET;
        }

        initView();
        setStyle();
        return rootView;
    }

    private void initView() {
        final int[] idBtns = {R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5,
                R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_0};
        final int[] valueBtns = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        for (int i = 0; i < idBtns.length; i++) {
            final ImageButton btn = (ImageButton) rootView.findViewById(idBtns[i]);
            if (btn != null) {
                final int finalI = i;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count++;
                        Log.d("QQQ", String.valueOf(valueBtns[finalI]));
                        enterPassword(valueBtns[finalI]);
                    }
                });
            }
        }


        img1 = (ImageView) rootView.findViewById(R.id.img_1);
        img2 = (ImageView) rootView.findViewById(R.id.img_2);
        img3 = (ImageView) rootView.findViewById(R.id.img_3);
        img4 = (ImageView) rootView.findViewById(R.id.img_4);

        btnBackspace = (ImageButton) rootView.findViewById(R.id.btn_backspace);
        btnBackspace.setOnClickListener(this);
        btnCancel = (ImageButton) rootView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        // deprecated
        title = (TextView) rootView.findViewById(R.id.title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void enterPassword(int number) {
        if (status == PIN_SET) {
            if (password.length() < 4) {
                password += String.valueOf(number);
                changeCheck();
            }
        } else if (status == PIN_CONFIRM) {
            if (confirm.length() < 4) {
                confirm += String.valueOf(number);
                changeCheck();
            }
        }
    }

    private void changeCheck() {
        switch (count) {
            case 0:
                img1.setImageResource(R.drawable.pin_unactive_ic);
                img2.setImageResource(R.drawable.pin_unactive_ic);
                img3.setImageResource(R.drawable.pin_unactive_ic);
                img4.setImageResource(R.drawable.pin_unactive_ic);
                break;
            case 1:
                img1.setImageResource(R.drawable.pin_active_ic);
                img2.setImageResource(R.drawable.pin_unactive_ic);
                img3.setImageResource(R.drawable.pin_unactive_ic);
                img4.setImageResource(R.drawable.pin_unactive_ic);
                break;
            case 2:
                img2.setImageResource(R.drawable.pin_active_ic);
                img3.setImageResource(R.drawable.pin_unactive_ic);
                img4.setImageResource(R.drawable.pin_unactive_ic);
                break;
            case 3:
                img3.setImageResource(R.drawable.pin_active_ic);
                img4.setImageResource(R.drawable.pin_unactive_ic);
                break;
            case 4:
                img4.setImageResource(R.drawable.pin_active_ic);
                nextAction();
                break;
        }
    }

    private void savePassword() {
        SharedPreferencesFile.setAppPassword(password);
        backClick();
        Toast.makeText(listenerActivity, getString(R.string.pin_password_confirm) + " " + SharedPreferencesFile.getAppPassword(), Toast.LENGTH_SHORT)
                .show();
    }

    public void backClick() {
        if (typePin == TYPE_PIN_SET) {
            listenerActivity.showFragment(FragmentSettings.newInstance(), ANIM_BACKWARD);
        } else {
            if (accept) {
                if (recordId == -1)
                    listenerActivity.showFragment(FragmentMain.newInstance());
                else {
                    try {
                        CallRecord callRecordItem = null;
                        DatabaseHelper databaseHelper = FactoryHelper.getHelper();
                        if (databaseHelper != null && databaseHelper.getCallRecordDAO() != null &&
                                databaseHelper.getCallRecordDAO().getAllItems() != null &&
                                databaseHelper.getCallRecordDAO().getAllItems().size() != 0) {
                            callRecordItem = FactoryHelper.getHelper().getCallRecordDAO().getItem(recordId);
                            if (callRecordItem != null) {
                                listenerActivity.showFragment(FragmentRecordPlay.newInstance(callRecordItem.getId(), RECORD_PLAY_LAST_FRAGMENT_MAIN));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else
                listenerActivity.finish();
        }
    }

    private void backSpace() {
        if (status == PIN_SET) {
            password = deleteLastChar(password);
        } else if (status == PIN_CONFIRM) {
            confirm =  deleteLastChar(confirm);
        }
        count--;
        changeCheck();
    }

    private String deleteLastChar(String pass) {
        if (pass != null && pass.length() > 0) {
            pass = pass.substring(0, pass.length() - 1);
        }
        return pass;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                backClick();
                break;
            case R.id.btn_backspace:
                cancel = true;
                backSpace();
                break;
        }
    }

    private void nextAction() {
        if (status == PIN_SET) {
            if (password.length() == 4) {
                status = PIN_CONFIRM;
                title.setText(getString(R.string.pin_title_confirm));
                count = 0;
                changeCheck();
            } else {
                Toast.makeText(listenerActivity, getString(R.string.pin_empty_password), Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (status == PIN_CONFIRM) {
            if (confirm.length() == 4) {
                if (confirm.equals(password)) {
                    accept = true;
                    if (!verify) {
                        savePassword();
                    } else {
                        backClick();
                    }
                } else {
                    confirm = "";
                    count = 0;
                    changeCheck();
                    Toast.makeText(listenerActivity, getString(R.string.pin_password_not_confirm), Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                Toast.makeText(listenerActivity, getString(R.string.pin_empty_password), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void setStyle() {
        (rootView.findViewById(R.id.root)).setBackground(getResources().getDrawable(COLOR_BACKGROUND[SharedPreferencesFile.getThemeNumber()]));
    }
}
