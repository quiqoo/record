package com.abc.callvoicerecorder.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.callvoicerecorder.converse.ActionClick;
import com.abc.callvoicerecorder.converse.StartRecord;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.kyleduo.switchbutton.SwitchButton;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.util.List;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.helper.PaintIconsHelper;
import com.abc.callvoicerecorder.utils.PermissionHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;


public class FragmentSettings extends FragmentBase implements Constants, View.OnClickListener, ActionClick.OnClickListener, ConstantsColor {
    private View rootView;
    private SwitchButton switchButton;
    private SwitchButton switchButtonVolume;
    private SwitchButton switchButtonWidget;
    private ImageView pinImg;
    private TextView pinSelectTitleTv;
    private TextView pinSelectDescTv;
    private int PICK_REQUEST_CODE = 0;
    private String oldPass = "";
    public LinearLayout scrollLayout;
    private SwitchButton switchButtonn;
    private TextView mainButtonText;
    public static Fragment newInstance() {
        return new FragmentSettings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferencesFile.initSharedReferences(listenerActivity);
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        setHasOptionsMenu(true);
        listenerActivity.visibleBackButton();
        initView();
        setStyle();

        ConnectivityManager conMgr  = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMgr.getActiveNetworkInfo();

        if(info != null && info.isConnected())
        {
            // internet is there.
        }
        else
        {
            AdView mAdMobAdView = (AdView) rootView.findViewById(R.id.admob_adview);
            mAdMobAdView.setVisibility(View.GONE);
            // internet is not there.
        }
        AdView mAdMobAdView = (AdView) rootView.findViewById(R.id.admob_adview);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdMobAdView.loadAd(adRequest);
    /*  final InterstitialAd mInterstitial = new InterstitialAd(getActivity());
        mInterstitial.setAdUnitId(getString(R.string.interstitial_ad_unit));
        mInterstitial.loadAd(new AdRequest.Builder().build());

        mInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // TODO Auto-generated method stub
                super.onAdLoaded();
                if (mInterstitial.isLoaded()) {
                    mInterstitial.show();
                }
            }
        }); */

        return rootView;
    }

    private void initView() {
        if (listenerActivity != null) {
            listenerActivity.setTitle(listenerActivity.getString(R.string.settings));
        }


        SharedPreferencesFile.setIsWidgetActive(PermissionHelper.isSystemAlertPermissionGranted(listenerActivity));

        switchButton = (SwitchButton) rootView.findViewById(R.id.sb_settings);
        switchButtonVolume = (SwitchButton) rootView.findViewById(R.id.sb_max_call_volume);
        switchButtonWidget = (SwitchButton) rootView.findViewById(R.id.sb_widget);
        PointF p = new PointF(100, 100);
        float size = 18 * getResources().getDisplayMetrics().density;
        switchButton.setThumbSize(new PointF(size, size));
        switchButton.setThumbColorRes(R.color.color_switch_button);
        switchButton.setBackColorRes(SWITCH_BTN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
        switchButton.setThumbRadius(100);
        switchButton.setBackRadius(100);
        switchButtonVolume.setThumbSize(new PointF(size, size));
        switchButtonVolume.setThumbColorRes(R.color.color_switch_button);
        switchButtonVolume.setBackColorRes(SWITCH_BTN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
        switchButtonVolume.setThumbRadius(100);
        switchButtonVolume.setBackRadius(100);
        switchButtonWidget.setThumbSize(new PointF(size, size));
        switchButtonWidget.setThumbColorRes(R.color.color_switch_button);
        switchButtonWidget.setBackColorRes(SWITCH_BTN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
        switchButtonWidget.setThumbRadius(100);
        switchButtonWidget.setBackRadius(100);

        pinImg = (ImageView) rootView.findViewById(R.id.settings_pin_select_img);
        pinSelectTitleTv = (TextView) rootView.findViewById(R.id.settings_pin_select_title_tv);
        pinSelectDescTv = (TextView) rootView.findViewById(R.id.settings_pin_select_desc_tv);

        pinImg = (ImageView) rootView.findViewById(R.id.settings_pin_select_img);
        pinSelectTitleTv = (TextView) rootView.findViewById(R.id.settings_pin_select_title_tv);
        pinSelectDescTv = (TextView) rootView.findViewById(R.id.settings_pin_select_desc_tv);

        oldPass = SharedPreferencesFile.getAppPassword();

        if (SharedPreferencesFile.getAppPassword().equals("")) {
            switchButton.setChecked(false);
            switchButton.setBackColorRes(SWITCH_BTN_DISABLE[SharedPreferencesFile.getThemeNumber()]);
            disablePin();
        } else {
            switchButton.setChecked(true);
            switchButton.setBackColorRes(SWITCH_BTN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
            activePin();
        }

        if (!SharedPreferencesFile.isSettingsMaxCallVolume()) {
            switchButtonVolume.setChecked(false);
            switchButtonVolume.setBackColorRes(SWITCH_BTN_DISABLE[SharedPreferencesFile.getThemeNumber()]);
            disableMaxVolume();
        } else {
            switchButtonVolume.setChecked(true);
            switchButtonVolume.setBackColorRes(SWITCH_BTN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
            activeMaxVolume();
        }


        if (!SharedPreferencesFile.isWidgetActive()) {
            switchButtonWidget.setChecked(false);
            switchButtonWidget.setBackColorRes(SWITCH_BTN_DISABLE[SharedPreferencesFile.getThemeNumber()]);
            disableWidget();
        } else {
            switchButtonWidget.setChecked(true);
            switchButtonWidget.setBackColorRes(SWITCH_BTN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
            activeWidget();;
        }

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    activePin();
                } else {
                    disablePin();
                }

            }
        });

        switchButtonVolume.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    activeMaxVolume();
                } else {
                    disableMaxVolume();
                }

            }
        });

        switchButtonWidget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    activeWidget();
                } else {
                    disableWidget();
                }

            }
        });

        (rootView.findViewById(R.id.settings_other)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_record_type_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_ignore_list_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_pin_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_pin_select_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_path_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_lang_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_max_call_volume_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_widget_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_share_layout)).setOnClickListener(this);
        (rootView.findViewById(R.id.settings_rate_us_layout)).setOnClickListener(this);

        setDescText();




        scrollLayout = (LinearLayout) rootView.findViewById(R.id.scroll_layout);
        mainButtonText = (TextView) rootView.findViewById(R.id.main_button_text);
        switchButtonn = (SwitchButton) rootView.findViewById(R.id.sb_main);
        PointF pp = new PointF(100, 100);
        float sizee = 24 * getResources().getDisplayMetrics().density;
        switchButtonn.setThumbSize(new PointF(sizee, sizee));
        switchButtonn.setThumbColorRes(R.color.color_switch_button)


        ;
        switchButtonn.setBackColorRes(SWITCH_BTN_MAIN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
        switchButtonn.setThumbRadius(100);
        switchButtonn.setBackRadius(100);


        switchButtonn.setChecked(SharedPreferencesFile.isMainNotificationActive());
        if (SharedPreferencesFile.isMainNotificationActive()) {
            mainButtonText.setText(getString(R.string.record_enable));
            switchButtonn.setBackColorRes(SWITCH_BTN_MAIN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
        }
        else {
            mainButtonText.setText(getString(R.string.record_disable));
            switchButtonn.setBackColorRes(SWITCH_BTN_MAIN_DISABLE[SharedPreferencesFile.getThemeNumber()]);
        }
        if (!SharedPreferencesFile.isEncoderTypeCrash())
            SharedPreferencesFile.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        switchButtonn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    activeRecorder();
                } else {
                    deactivateRecorder();
                }

            }
        });

        scrollLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchButtonn.setChecked(!switchButtonn.isChecked());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_record_type_layout:
                listenerActivity.showFragment(FragmentRecordType.newInstance(), ANIM_FORWARD);
                break;
            case R.id.settings_other:
                listenerActivity.showFragment(FragmentOther.newInstance(), ANIM_FORWARD);
                break;
            case R.id.settings_ignore_list_layout:
                listenerActivity.showFragment(FragmentIgnore.newInstance());
                break;
            case R.id.settings_pin_layout:
                switchButton.setChecked(!switchButton.isChecked());
                break;
            case R.id.settings_pin_select_layout:
                if (switchButton.isChecked())
                    listenerActivity.showFragment(FragmentPinProtection.newInstance(TYPE_PIN_SET, -1), ANIM_FORWARD);
                break;
            case R.id.settings_path_layout:
                Intent i = new Intent(listenerActivity, FilePickerActivity.class);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                startActivityForResult(i, PICK_REQUEST_CODE);
                break;
            case R.id.settings_max_call_volume_layout:
                switchButtonVolume.setChecked(!switchButtonVolume.isChecked());
                break;
            case R.id.settings_lang_layout:
                listenerActivity.showFragment(FragmentTranslation.newInstance(), ANIM_FORWARD);
                break;
            case R.id.settings_widget_layout:
                switchButtonWidget.setChecked(!switchButtonWidget.isChecked());
                break;
            case R.id.settings_share_layout:
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.putExtra(Intent.EXTRA_TEXT, listenerActivity.getString(R.string.share_text) + "market://details?id=" + listenerActivity.getPackageName());
                intentShare.setType("text/plain");
                listenerActivity.startActivity(Intent.createChooser(intentShare, listenerActivity.getString(R.string.settings_share)));
                break;
            case R.id.settings_rate_us_layout:
                Intent intentRateUs = new Intent(Intent.ACTION_VIEW);
                intentRateUs.setData(Uri.parse("market://details?id=" + listenerActivity.getPackageName()));
                listenerActivity.startActivity(intentRateUs);
                break;
            default:
                break;
        }
    }

    private void activePin() {
        switchButton.setBackColorRes(SWITCH_BTN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
        pinImg.setImageResource(R.drawable.settings_pin_ic);
        pinImg.setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.settings_pin_ic, COLOR_ICONS[SharedPreferencesFile.getThemeNumber()]));
        pinSelectTitleTv.setTextColor(listenerActivity.getResources().getColor(R.color.color_edit_record_text));
        pinSelectDescTv.setTextColor(listenerActivity.getResources().getColor(R.color.color_settings_pin_desc));
        SharedPreferencesFile.setAppPassword(oldPass);
    }

    private void disablePin() {
        switchButton.setBackColorRes(SWITCH_BTN_DISABLE[SharedPreferencesFile.getThemeNumber()]);
        pinImg.setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.settings_pin_ic, R.color.color_settings_pin_unselected));
        pinSelectTitleTv.setTextColor(listenerActivity.getResources().getColor(R.color.color_settings_pin_unselected));
        pinSelectDescTv.setTextColor(listenerActivity.getResources().getColor(R.color.color_settings_pin_unselected));
        SharedPreferencesFile.setAppPassword("");
    }

    private void activeMaxVolume() {
        switchButtonVolume.setBackColorRes(SWITCH_BTN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
        SharedPreferencesFile.setIsSettingsMaxCallVolume(true);
    }

    private void disableMaxVolume() {
        switchButtonVolume.setBackColorRes(SWITCH_BTN_DISABLE[SharedPreferencesFile.getThemeNumber()]);
        SharedPreferencesFile.setIsSettingsMaxCallVolume(false);
    }

    private void activeWidget() {
        if (!PermissionHelper.isSystemAlertPermissionGranted(listenerActivity)) {
            PermissionHelper.requestSystemAlertPermission(listenerActivity);
        }
        switchButtonWidget.setBackColorRes(SWITCH_BTN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
        SharedPreferencesFile.setIsWidgetActive(true);
    }

    private void disableWidget() {
        switchButtonWidget.setBackColorRes(SWITCH_BTN_DISABLE[SharedPreferencesFile.getThemeNumber()]);
        SharedPreferencesFile.setIsWidgetActive(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            List<Uri> files = Utils.getSelectedFilesFromResult(data);
            for (Uri uri: files) {
                File file = Utils.getFileForUri(uri);
                SharedPreferencesFile.setDirPathAbs(file.getAbsolutePath());
                Toast.makeText(listenerActivity, getString(R.string.change_save_folder_to) + " " + file.getAbsolutePath(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setStyle() {
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_record_type_ic, R.drawable.settings_record_type_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_filter_list_ic, R.drawable.settings_filter_list_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_folder_ic, R.drawable.settings_folder_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_max_volume_ic, R.drawable.settings_max_volume_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_pin_title_ic, R.drawable.settings_pin_title_ic);

        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_widget_ic, R.drawable.settings_widget_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_lang_ic, R.drawable.settings_lang_ic);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_other_ic, R.drawable.settings_other_ic);

        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_share_ic, R.drawable.edit_record_share);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.settings_rate_us_ic, R.drawable.menu_item_rate_us);
    }

    private void setDescText() {
        TextView settingsRecordTypeDescTv = (TextView) rootView.findViewById(R.id.settings_record_type_desc_tv);
        switch (SharedPreferencesFile.getCallTypeRecord()) {
            case CALL_TYPE_RECORD_ALL:
                settingsRecordTypeDescTv.setText(getString(R.string.dialog_call_type_record_all));
                break;
            case CALL_TYPE_RECORD_INCOMING:
                settingsRecordTypeDescTv.setText(getString(R.string.dialog_call_type_record_incoming));
                break;
            case CALL_TYPE_RECORD_OUTGOING:
                settingsRecordTypeDescTv.setText(getString(R.string.dialog_call_type_record_outgoing));
                break;
            case CALL_TYPE_RECORD_UNKNOWN:
                settingsRecordTypeDescTv.setText(getString(R.string.dialog_call_type_record_unknown));
                break;
        }
    }

    private void activeRecorder() {
        try {
            mainButtonText.setText(getString(R.string.record_enable));
            switchButtonn.setBackColorRes(SWITCH_BTN_MAIN_ENABLE[SharedPreferencesFile.getThemeNumber()]);
            listenerActivity.myService.startCallRecordClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deactivateRecorder() {
        try {
            if (!SharedPreferencesFile.isWidgetAttentionShowed()) {
                StartRecord.showStartAttentionDialog(listenerActivity, getString(R.string.dialog_attention_label), getString(R.string.dialog_widget_body));
                SharedPreferencesFile.setIsWidgetAttentionShowed(true);
            }

            mainButtonText.setText(getString(R.string.record_disable));
            switchButtonn.setBackColorRes(SWITCH_BTN_MAIN_DISABLE[SharedPreferencesFile.getThemeNumber()]);
            listenerActivity.myService.stopAutoCallRecordClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick() {
        setDescText();
    }
}
