package com.abc.callvoicerecorder.fragment;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.helper.FactoryHelper;
import com.abc.callvoicerecorder.db.CallRecord;
import com.abc.callvoicerecorder.converse.Delete;
import com.abc.callvoicerecorder.converse.SaveDesc;
import com.abc.callvoicerecorder.helper.GoogleDriveHelper;
import com.abc.callvoicerecorder.constant.Constants;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.helper.ContactsHelper;
import com.abc.callvoicerecorder.helper.PaintIconsHelper;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;

public class FragmentRecordPlay extends FragmentBase implements Constants, ConstantsColor, View.OnClickListener {
    private View rootView;
    private CallRecord callRecordItem;
    private int recordId = -1;
    private int lastFragment = 0;
    private ImageView playImage;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;

    private EditText editTextName;
    private EditText editTextDesc;
    private boolean isSaved = false;
    private boolean isStopped = false;

    private static final int RC_SIGN_IN = 9001;
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    private GoogleSignInClient mGoogleSignInClient;

    public static Fragment newInstance(int recordId, int lastFragment) {
        Fragment fragment = new FragmentRecordPlay();
        Bundle bundle = new Bundle();
        bundle.putInt(RECORD_ID, recordId);
        bundle.putInt(RECORD_PLAY_LAST_FRAGMENT, lastFragment);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_record_play, container, false);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();

        final InterstitialAd mInterstitial = new InterstitialAd(getActivity());
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
        });

        if (bundle != null && bundle.containsKey(RECORD_ID) && bundle.containsKey(RECORD_PLAY_LAST_FRAGMENT)) {
            recordId = bundle.getInt(RECORD_ID);
            lastFragment = bundle.getInt(RECORD_PLAY_LAST_FRAGMENT);

            try {
                callRecordItem = FactoryHelper.getHelper().getCallRecordDAO().getItem(recordId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        listenerActivity.visibleBackButton();
        initView();
        setStyle();
        return rootView;
    }

    private void initView() {
        if (callRecordItem != null) {
            if (listenerActivity != null) {
                String contactName = ContactsHelper.getContactName(callRecordItem.getCallNumber(), listenerActivity);
                if (contactName.equals(""))
                    contactName = callRecordItem.getCallNumber();
                listenerActivity.setTitle(contactName);
            }

            initMediaPlayer();

            playImage = (ImageView) rootView.findViewById(R.id.play_imageView);
            seekBar = (SeekBar) rootView.findViewById(R.id.seek_bar);

            playImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playRecord();
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(mediaPlayer != null && fromUser){
                        mediaPlayer.seekTo(progress * mediaPlayer.getDuration() / 100);
                    }
                }
            });

            editTextName = (EditText) rootView.findViewById(R.id.edit_text_name);
            editTextDesc = (EditText) rootView.findViewById(R.id.edit_text_desc);

            editTextName.setVisibility(View.GONE);

            if (callRecordItem.getDescTitle() != null && !callRecordItem.getDescTitle().equals("")) {
                editTextName.setText(callRecordItem.getDescTitle());
                editTextName.setVisibility(View.VISIBLE);
            }
            if (callRecordItem.getDesc() != null && !callRecordItem.getDesc().equals("")) {
                editTextDesc.setText(callRecordItem.getDesc());
            }


            (rootView.findViewById(R.id.play_default_layout)).setOnClickListener(this);
            (rootView.findViewById(R.id.share_layout)).setOnClickListener(this);
            (rootView.findViewById(R.id.share_google_drive_layout)).setOnClickListener(this);
            (rootView.findViewById(R.id.add_ignore_layout)).setOnClickListener(this);
            (rootView.findViewById(R.id.delete_contact_layout)).setOnClickListener(this);



        }

    }

    private void playRecord() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            playImage.setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.ic_pause, COLOR_ICONS[SharedPreferencesFile.getThemeNumber()]));
            if (isStopped)
                initMediaPlayer();
            mediaPlayer.start();
            isStopped = false;
            startListener();

        } else {
            playImage.setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.ic_play, COLOR_ICONS[SharedPreferencesFile.getThemeNumber()]));
            mediaPlayer.pause();
        }
    }

    private void startListener() {
        final Handler mHandler = new Handler();
        listenerActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (seekBar != null && mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition();
                    if (mediaPlayer.getDuration() != 0)
                        seekBar.setProgress((mCurrentPosition * 100) / mediaPlayer.getDuration());
                    else
                        seekBar.setProgress((mCurrentPosition * 100) / 1);
                    mHandler.postDelayed(this, 1000);
                }
            }
        });
    }

    public void saveDialog() {
        if (editTextName.getVisibility() == View.VISIBLE) {
            if ((editTextName.getText() != null && !editTextName.getText().toString().equals(callRecordItem.getDescTitle()))
                    || (editTextDesc.getText() != null && !editTextDesc.getText().toString().equals(callRecordItem.getDesc())))
                SaveDesc.showSaveDescConfirmDialog(listenerActivity, callRecordItem, editTextName.getText().toString(), editTextDesc.getText().toString());
            else
                listenerActivity.showFragment(FragmentMain.newInstance());
        } else {
            if (editTextDesc.getText() != null && !editTextDesc.getText().toString().equals(callRecordItem.getDesc()))
                SaveDesc.showSaveDescConfirmDialog(listenerActivity, callRecordItem, "", editTextDesc.getText().toString());
            else
                listenerActivity.showFragment(FragmentMain.newInstance());
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            isStopped = true;
            mediaPlayer.stop();
            playImage.setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.ic_play, COLOR_ICONS[SharedPreferencesFile.getThemeNumber()]));
            seekBar.setProgress(0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            isStopped = true;
            mediaPlayer.stop();
            playImage.setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.ic_play, COLOR_ICONS[SharedPreferencesFile.getThemeNumber()]));
            seekBar.setProgress(0);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.record_play_menu, menu);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_default_layout:
                Intent intentPlay = new Intent();
                intentPlay.setAction(android.content.Intent.ACTION_VIEW);
                File filePlay = new File(callRecordItem.getPath());
                if (filePlay.exists()) {
                    try {
                        intentPlay.setDataAndType(Uri.fromFile(filePlay), "audio/*");
                        startActivity(intentPlay);
                    } catch (Exception e) {
                        try {
                            Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                            startActivity(intent);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.add_ignore_layout:
                FactoryHelper.getHelper().addIgnoreContactItemDB(listenerActivity, callRecordItem.getCallNumber());
                break;
            case R.id.share_layout:
                File file = new File(callRecordItem.getPath());
                Uri uri = Uri.fromFile(file);
                Log.d("QQQ", callRecordItem.getPath() + " " + file.exists());
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Share Sound File"));
                break;
            case R.id.share_google_drive_layout:
                signIn();
                break;
            case R.id.delete_contact_layout:
                Delete.showDeleteCallConfirmDialog(listenerActivity, callRecordItem, DELETE_AUDIO_STATUS_RIGHT_CLOSE);
                break;
            default:
                break;
        }
    }

    public boolean isSaved() {
        return isSaved;
    }

    public int getLastFragment() {
        return lastFragment;
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playImage.setImageDrawable(PaintIconsHelper.recolorIcon(listenerActivity, R.drawable.ic_play, COLOR_ICONS[SharedPreferencesFile.getThemeNumber()]));
                    seekBar.setProgress(0);
                }
            });
            mediaPlayer.setDataSource(callRecordItem.getPath());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                String contactName = ContactsHelper.getContactName(callRecordItem.getCallNumber(), listenerActivity);
                if (!contactName.equals("")) {
                    Intent contactInfoIntent = new Intent(Intent.ACTION_VIEW);
                    contactInfoIntent.setData(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(ContactsHelper.getContactId(callRecordItem.getCallNumber(), listenerActivity))));
                    startActivity(contactInfoIntent);
                } else {
                    try {
                        Intent intentAdd = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, Uri.parse("tel:" + callRecordItem.getCallNumber()));
                        intentAdd.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true);
                        startActivity(intentAdd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            case R.id.action_call:
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + callRecordItem.getCallNumber()));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setStyle() {
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.edit_record_play_default, R.drawable.edit_record_play_default);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.edit_record_filter, R.drawable.edit_record_filter);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.edit_record_share, R.drawable.edit_record_share);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.edit_record_share_google_drive, R.drawable.edit_record_share_google_drive);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.edit_record_delete, R.drawable.edit_record_delete);
        PaintIconsHelper.setIconsColors(listenerActivity, rootView, R.id.play_imageView, R.drawable.ic_play);
    }

    public void signIn() {
        Set<Scope> requiredScopes = new HashSet<>(2);
        requiredScopes.add(Drive.SCOPE_FILE);
        requiredScopes.add(Drive.SCOPE_APPFOLDER);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (signInAccount != null && signInAccount.getGrantedScopes().containsAll(requiredScopes)) {
        } else {
            GoogleSignInOptions signInOptions =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestScopes(Drive.SCOPE_FILE)
                            .requestScopes(Drive.SCOPE_APPFOLDER)
                            .build();
            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), signInOptions);
            startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> getAccountTask =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == getActivity().RESULT_OK) {
            listenerActivity.getGoogleApiClient().connect();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        try {
            Log.d("Google_sign", "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                File file = new File(callRecordItem.getPath());
                if (file.exists())
                    createGoogleDriveFile(file, acct);
            } else {

            }
        } catch (Exception e) {
            Log.d("exception", String.valueOf(e));
        }
    }

    public void createGoogleDriveFile(File audioFile, GoogleSignInAccount acct) {
        GoogleDriveHelper googleDriveHelper = new GoogleDriveHelper(listenerActivity.getGoogleApiClientDrive(), listenerActivity, acct, mGoogleSignInClient);

        try {
            googleDriveHelper.createAudio(audioFile.getName(), audioFile.getAbsolutePath());
        } catch (Exception e) {
            Log.d("qwerty1", String.valueOf(e));
        }
    }
}
