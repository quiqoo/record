package com.abc.callvoicerecorder.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.callvoicerecorder.adapter.MainSliderAdapter;
import com.abc.callvoicerecorder.fragment.FragmentDrawer;
import com.abc.callvoicerecorder.service.PicassoImageLoadingService;
import com.github.martarodriguezm.rateme.OnRatingListener;
import com.github.martarodriguezm.rateme.RateMeDialog;
import com.github.martarodriguezm.rateme.RateMeDialogTimer;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Locale;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.adapter.ListAdapter;
import com.abc.callvoicerecorder.adapter.MainCallAllAdapter;
import com.abc.callvoicerecorder.helper.DatabaseHelper;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.transalation.LocaleTranslation;
import com.abc.callvoicerecorder.fragment.FragmentFavoriteCall;
import com.abc.callvoicerecorder.fragment.FragmentIncomingCall;
import com.abc.callvoicerecorder.fragment.FragmentOutgoingCall;
import com.abc.callvoicerecorder.fragment.FragmentDeleteAll;
import com.abc.callvoicerecorder.fragment.FragmentDeleteContact;
import com.abc.callvoicerecorder.fragment.FragmentDeleteDsAll;
import com.abc.callvoicerecorder.fragment.FragmentPlay;
import com.abc.callvoicerecorder.fragment.FragmentRecord;
import com.abc.callvoicerecorder.fragment.FragmentIgnore;
import com.abc.callvoicerecorder.fragment.FragmentMain;
import com.abc.callvoicerecorder.fragment.FragmentPinProtection;
import com.abc.callvoicerecorder.fragment.FragmentRecordPlay;
import com.abc.callvoicerecorder.fragment.FragmentContactSearch;
import com.abc.callvoicerecorder.fragment.FragmentSettings;
import com.abc.callvoicerecorder.fragment.FragmentOther;
import com.abc.callvoicerecorder.fragment.FragmentSkins;
import com.abc.callvoicerecorder.fragment.FragmentAfterCall;
import com.abc.callvoicerecorder.fragment.FragmentAudioSource;
import com.abc.callvoicerecorder.fragment.FragmentFileExt;
import com.abc.callvoicerecorder.fragment.FragmentTranslation;
import com.abc.callvoicerecorder.fragment.FragmentRecordType;
import com.abc.callvoicerecorder.fragment.FragmentSortType;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.service.BootCallRecordService;
import com.abc.callvoicerecorder.utils.EventsUtils;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;
import com.nononsenseapps.filepicker.FilePickerActivity;

import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.OnClickPathListener, Constants, ConstantsColor,
        GoogleApiClient.OnConnectionFailedListener,  GoogleApiClient.ConnectionCallbacks {




    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;

    final int launchTimes = 2; // Set the Time here
    final int installDate = 1; // Set the day here


    private Toolbar toolbar;
    private static Fragment currentFragment;

    public static MainCallAllAdapter mainFragmentAllAdapter;
    public static MainCallAllAdapter mainFragmentIncomingAdapter;
    public static MainCallAllAdapter mainFragmentOutgoingAdapter;
    public static MainCallAllAdapter mainFragmentFavoriteAdapter;
    public static MainCallAllAdapter searchFragmentAdapter;
    public static ListAdapter dictaphoneListFragmentAdapter;

    public BootCallRecordService myService;
    private ServiceConnection sConn;
    private boolean bound = false;
    private Intent intent;
    private int notificationId = MAIN_ACTIVITY_ID;

    private Locale locale;
    private String lang;
    private DrawerLayout drawerLayout;
    private int btnPosX = 0;
    private int btnPosY = 0;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClientDrive;
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    private boolean isGoogleDriveConnected = false;
    private int PICK_REQUEST_CODE = 0;
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public GoogleApiClient getGoogleApiClientDrive() {
        return mGoogleApiClientDrive;
    }
    private Slider slider;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private FragmentDrawer mDrawerLayoutHelper;
    public Fragment getCurrentFragment() {
        return currentFragment;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        locale = new Locale(lang);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);
    }

    public FragmentDrawer getDrawerLayoutHelper() {
        return mDrawerLayoutHelper;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesFile.initSharedReferences(MainActivity.this);
        lang = SharedPreferencesFile.getLangType();
        LocaleTranslation localeEnum = LocaleTranslation.LOCALE_DEFAULT;
        for (LocaleTranslation enam : LocaleTranslation.values()) {
            if (enam.getMessage().equals(lang))
                localeEnum = enam;
        }

        switch (localeEnum) {
            case LOCALE_DEFAULT:
                locale = getResources().getConfiguration().locale.getDefault();
                break;
            default:
                locale = new Locale(localeEnum.getMessage());
                break;
        }




        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);

        super.onCreate(savedInstanceState);
        setTheme(THEME[SharedPreferencesFile.getThemeNumber()]);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(COLOR_TOOLBAR[SharedPreferencesFile.getThemeNumber()]));
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_theme1));
        }


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);

        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3};


        addBottomDots(0);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(SECOND_NOTIFICATION_ID);
        nMgr.cancel(ERROR_NOTIFICATION_ID);

        if (SharedPreferencesFile.getOutputFormat() == MediaRecorder.OutputFormat.AMR_NB)
            SharedPreferencesFile.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);

        intent = new Intent(this, BootCallRecordService.class);
        sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d("WTF_S", "onServiceConnected");
                myService = ((BootCallRecordService.MyBinder) binder).getService();
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.d("WTF_S", "onServiceDisconnected");
                bound = false;
            }
        };

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(PUSH_ID)) {
            notificationId = (int) bundle.getInt(PUSH_ID);
            Log.d("WTF_Activity", "Start Activity with pushId = " + notificationId);
        }
        Slider.init(new PicassoImageLoadingService(this));
        initView();
    }



    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);


            if (position == layouts.length - 1) {

                //   btnNext.setText(">>");
                //   btnSkip.setVisibility(View.GONE);
            } else {

                //   btnNext.setText(">>");
                //   btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


    private void initView() {

        if (checkPermission()) {


        } else {
            requestPermission();

        }

        sendConversion();


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (!SharedPreferencesFile.isEncoderTypeSend() && SharedPreferencesFile.isEncoderTypeCheck() && internetEnabled()) {
            int encoderType = 0;
            if (SharedPreferencesFile.getAudioSource() == MediaRecorder.AudioSource.VOICE_CALL)
                encoderType = EVENT_TYPE_VOICE_CALL;
            else if (SharedPreferencesFile.getAudioSource() == MediaRecorder.AudioSource.DEFAULT)
                encoderType = EVENT_TYPE_DEFAULT;
            else
                encoderType = EVENT_TYPE_VOICE_COMMUNICATION;
            EventsUtils.sendEncoderTypeEvent(this, encoderType);

            SharedPreferencesFile.setIsEncoderTypeSend(true);

            Log.d("EVENT_SEND", "encoder type send = " + encoderType);
        }

            Handler changeFragmentHandler = new Handler();
            Runnable changeFragmentRunnable = new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (SharedPreferencesFile.getAppPassword().equals("")) {
                                if (notificationId == -1) {
                                    showFragment(FragmentMain.newInstance());
                                } else {
                                    try {
                                        CallRecord callRecordItem = null;
                                        DatabaseHelper databaseHelper = FactoryHelper.getHelper();
                                        if (databaseHelper != null && databaseHelper.getCallRecordDAO() != null &&
                                                databaseHelper.getCallRecordDAO().getAllItems() != null &&
                                                databaseHelper.getCallRecordDAO().getAllItems().size() != 0) {
                                            callRecordItem = FactoryHelper.getHelper().getCallRecordDAO().getItem(notificationId);
                                            if (callRecordItem != null) {
                                                showFragment(FragmentRecordPlay.newInstance(callRecordItem.getId(), RECORD_PLAY_LAST_FRAGMENT_MAIN));
                                            } else {
                                                showFragment(FragmentMain.newInstance());
                                            }
                                        } else {
                                            showFragment(FragmentMain.newInstance());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        showFragment(FragmentMain.newInstance());
                                    }
                                }
                            } else {
                                showFragment(FragmentPinProtection.newInstance(TYPE_PIN_CONFIRM, notificationId));
                            }
                        }
                    });

                }
            };
            changeFragmentHandler.postDelayed(changeFragmentRunnable, 1500);

        RateMeDialogTimer.onStart(this);
        if (RateMeDialogTimer.shouldShowRateDialog(this, installDate, launchTimes)) {
            showCustomRateMeDialogg();
        }
    }


    public void showFragment(Fragment nextFragment) {
        try {
            currentFragment = nextFragment;

            if ((currentFragment instanceof FragmentPinProtection) && getSupportActionBar() != null) {
                getSupportActionBar().hide();
                RelativeLayout adsop = (RelativeLayout) findViewById(R.id.ads);
                adsop.setVisibility(View.GONE);
            } else if (getSupportActionBar() != null) {
                getSupportActionBar().show();
                RelativeLayout adsop = (RelativeLayout) findViewById(R.id.ads);
                adsop.setVisibility(View.VISIBLE);
            }

            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.content_frame, nextFragment, "");
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showFragment(Fragment nextFragment, int anim) {
        try {
            try {
                currentFragment = nextFragment;

                if ((currentFragment instanceof FragmentPinProtection) && getSupportActionBar() != null) {
                    getSupportActionBar().hide();
                    RelativeLayout adsop = (RelativeLayout) findViewById(R.id.ads);
                    adsop.setVisibility(View.GONE);
                } else if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                    RelativeLayout adsop = (RelativeLayout) findViewById(R.id.ads);
                    adsop.setVisibility(View.VISIBLE);
                }

                if (getSupportFragmentManager() != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    switch (anim) {
                        case ANIM_FORWARD:
                            fragmentTransaction.setCustomAnimations(R.anim.slide_forward_in_left, R.anim.slide_forward_in_right);
                            break;
                        case ANIM_BACKWARD:
                            fragmentTransaction.setCustomAnimations(R.anim.slide_backward_in_left, R.anim.slide_backward_in_right);
                            break;
                    }
                    fragmentTransaction.replace(R.id.content_frame, nextFragment, "");
                    fragmentTransaction.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDrawerLayoutHelper = new FragmentDrawer(this, drawerLayout, MainActivity.this);
        bindService(intent, sConn, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDrawerLayoutHelper.closePanel();
        notificationId = MAIN_ACTIVITY_ID;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!bound) return;
        unbindService(sConn);
        bound = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(SECOND_NOTIFICATION_ID);
        Log.d("ON_METHOD", "new intent");
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                notificationId = (int) bundle.getInt(PUSH_ID);
            }
            if (notificationId != -1) {
                if (SharedPreferencesFile.getAppPassword().equals("")) {
                    try {
                        CallRecord callRecordItem = null;
                        DatabaseHelper databaseHelper = FactoryHelper.getHelper();
                        if (databaseHelper != null && databaseHelper.getCallRecordDAO() != null &&
                                databaseHelper.getCallRecordDAO().getAllItems() != null &&
                                databaseHelper.getCallRecordDAO().getAllItems().size() != 0) {
                            callRecordItem = FactoryHelper.getHelper().getCallRecordDAO().getItem(notificationId);
                            if (callRecordItem != null) {
                                showFragment(FragmentRecordPlay.newInstance(callRecordItem.getId(), RECORD_PLAY_LAST_FRAGMENT_MAIN));
                            } else {
                                showFragment(FragmentMain.newInstance());
                            }
                        } else {
                            showFragment(FragmentMain.newInstance());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showFragment(FragmentMain.newInstance());
                    }
                } else {
                    showFragment(FragmentPinProtection.newInstance(TYPE_PIN_CONFIRM, notificationId));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        procheck();

    }

    private void sendConversion() {

    }

    private boolean internetEnabled() {
        NetworkInfo i = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    public void visibleBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Drawable homeButton = getResources().getDrawable(R.drawable.back_button_ic);
            if (homeButton != null) {
                homeButton.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
            }
            getSupportActionBar().setHomeAsUpIndicator(homeButton);
        }
    }

    public void disableBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }


    public void setBtnPosX(int btnPosX) {
        this.btnPosX = btnPosX;
    }

    public void setBtnPosY(int btnPosY) {
        this.btnPosY = btnPosY;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("qwerty", "GoogleApiClient connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("qwerty", "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        try {
            Log.d("Google_sign", "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {

                GoogleSignInAccount acct = result.getSignInAccount();
                isGoogleDriveConnected = true;
            } else {

            }
        } catch (Exception e) {
            Log.d("exception", String.valueOf(e));
        }
    }
    private void showCustomRateMeDialogg() {
        new RateMeDialog.Builder(getPackageName(), getString(R.string.app_name))
                .setHeaderBackgroundColor(getResources().getColor(R.color.colorPrimary_theme1))
                .setBodyBackgroundColor(getResources().getColor(R.color.colorPrimaryDark_theme1))
                .setBodyTextColor(getResources().getColor(R.color.colorAccent_theme1))
                .showAppIcon(R.mipmap.ic_launcher)
                .setDefaultNumberOfStars(5)
                .setShowShareButton(true)
                .setLineDividerColor(getResources().getColor(R.color.color_list_text_desc_color))
                .setRateButtonBackgroundColor(getResources().getColor(R.color.colorPrimary_theme1))
                .setRateButtonPressedBackgroundColor(getResources().getColor(R.color.btn_gradient_color_start))
                .setOnRatingListener(new OnRatingListener() {
                    @Override
                    public void onRating(RatingAction action, float rating) {

                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {

                    }
                })
                .build()
                .show(getFragmentManager(), "custom-dialog");
    }
    public void procheck() {
        boolean strPref = false;
        SharedPreferences shf = this.getSharedPreferences("config", MODE_PRIVATE);
        strPref = shf.getBoolean("showyes", false);

        if (strPref) {

            if (currentFragment instanceof FragmentMain) {
                if (mDrawerLayoutHelper.isPanelOpen())
                    mDrawerLayoutHelper.closePanel();
                else
                    super.onBackPressed();
            } else if (currentFragment instanceof FragmentContactSearch) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentRecordPlay) {
                if (((FragmentRecordPlay) currentFragment).getLastFragment() == RECORD_PLAY_LAST_FRAGMENT_MAIN) {
                    if (!((FragmentRecordPlay) currentFragment).isSaved())
                        ((FragmentRecordPlay) currentFragment).saveDialog();
                    else
                        showFragment(FragmentMain.newInstance());
                } else if (((FragmentRecordPlay) currentFragment).getLastFragment() == Constants.RECORD_PLAY_LAST_FRAGMENT_DELETE) {
                    showFragment(FragmentDeleteAll.newInstance());
                }
            } else if (currentFragment instanceof FragmentSettings) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentIgnore) {
                showFragment(FragmentSettings.newInstance());
            } else if (currentFragment instanceof FragmentDeleteAll) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentDeleteContact) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentRecord) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentPlay) {
                if (((FragmentPlay) currentFragment).getLastFragment() == RECORD_PLAY_LAST_FRAGMENT_MAIN) {
                    if (!((FragmentPlay) currentFragment).isSaved())
                        ((FragmentPlay) currentFragment).saveDialog();
                    else
                        showFragment(FragmentMain.newInstance());
                } else if (((FragmentPlay) currentFragment).getLastFragment() == Constants.RECORD_PLAY_LAST_FRAGMENT_DELETE) {
                    showFragment(FragmentDeleteDsAll.newInstance());
                }
            } else if (currentFragment instanceof FragmentDeleteDsAll) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentOther) {
                showFragment(FragmentSettings.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentPinProtection) {
                ((FragmentPinProtection) currentFragment).backClick();
            } else if (currentFragment instanceof FragmentRecordType) {
                showFragment(FragmentSettings.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentAudioSource) {
                showFragment(FragmentOther.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentAfterCall) {
                showFragment(FragmentOther.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentFileExt) {
                showFragment(FragmentOther.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentSortType) {
                showFragment(FragmentOther.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentTranslation) {
                showFragment(FragmentSettings.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentSkins) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentIncomingCall) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentOutgoingCall) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentFavoriteCall) {
                showFragment(FragmentMain.newInstance());
            }

        } else {


            if (currentFragment instanceof FragmentMain) {
                if (mDrawerLayoutHelper.isPanelOpen())
                    mDrawerLayoutHelper.closePanel();
                else
                    super.onBackPressed();
            } else if (currentFragment instanceof FragmentContactSearch) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentRecordPlay) {
                if (((FragmentRecordPlay) currentFragment).getLastFragment() == RECORD_PLAY_LAST_FRAGMENT_MAIN) {
                    if (!((FragmentRecordPlay) currentFragment).isSaved())
                        ((FragmentRecordPlay) currentFragment).saveDialog();
                    else
                        showFragment(FragmentMain.newInstance());
                } else if (((FragmentRecordPlay) currentFragment).getLastFragment() == Constants.RECORD_PLAY_LAST_FRAGMENT_DELETE) {
                    showFragment(FragmentDeleteAll.newInstance());
                }
            } else if (currentFragment instanceof FragmentSettings) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentIgnore) {
                showFragment(FragmentSettings.newInstance());
            } else if (currentFragment instanceof FragmentDeleteAll) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentDeleteContact) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentRecord) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentPlay) {
                if (((FragmentPlay) currentFragment).getLastFragment() == RECORD_PLAY_LAST_FRAGMENT_MAIN) {
                    if (!((FragmentPlay) currentFragment).isSaved())
                        ((FragmentPlay) currentFragment).saveDialog();
                    else
                        showFragment(FragmentMain.newInstance());
                } else if (((FragmentPlay) currentFragment).getLastFragment() == Constants.RECORD_PLAY_LAST_FRAGMENT_DELETE) {
                    showFragment(FragmentDeleteDsAll.newInstance());
                }
            } else if (currentFragment instanceof FragmentDeleteDsAll) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentOther) {
                showFragment(FragmentSettings.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentPinProtection) {
                ((FragmentPinProtection) currentFragment).backClick();
            } else if (currentFragment instanceof FragmentRecordType) {
                showFragment(FragmentSettings.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentAudioSource) {
                showFragment(FragmentOther.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentAfterCall) {
                showFragment(FragmentOther.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentFileExt) {
                showFragment(FragmentOther.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentSortType) {
                showFragment(FragmentOther.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentTranslation) {
                showFragment(FragmentSettings.newInstance(), ANIM_BACKWARD);
            } else if (currentFragment instanceof FragmentSkins) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentIncomingCall) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentOutgoingCall) {
                showFragment(FragmentMain.newInstance());
            } else if (currentFragment instanceof FragmentFavoriteCall) {
                showFragment(FragmentMain.newInstance());
            }

        }
    }




    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_CONTACTS, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean contactAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean phoneAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted && cameraAccepted && contactAccepted && phoneAccepted )
                        Toast.makeText(this, "Permission Granted, Now you can access this app.", Toast.LENGTH_LONG).show();

                    else {
                        Toast.makeText(this, "Permission Denied, You cannot use this app.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_CONTACTS, READ_PHONE_STATE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void onClickPath() {
        Intent i = new Intent(MainActivity.this, FilePickerActivity.class);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        startActivityForResult(i, PICK_REQUEST_CODE);
    }
}
