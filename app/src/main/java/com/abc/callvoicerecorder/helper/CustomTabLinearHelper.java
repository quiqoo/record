package com.abc.callvoicerecorder.helper;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.activity.MainActivity;
import com.abc.callvoicerecorder.fragment.FragmentRecord;
import com.abc.callvoicerecorder.constant.ConstantsColor;
import com.abc.callvoicerecorder.utils.SharedPreferencesFile;



public class CustomTabLinearHelper implements ConstantsColor {
    private static Context mContext;
    private static View mView;
    private static int mPosition;
    private static ViewPager mCallPager;
    private static ImageView mImageView;
    private static LinearLayout mImageLayout;
    private static TextView mCallLabel;
    private static TextView mVoiceLabel;
    private static ImageView mCallLabelImg;
    private static ImageView mVoiceLabelImg;

    public static void initCustomTabLinearHelper(Context context, View view, int position, ViewPager callPager) {
        mContext = context;
        mView = view;
        mPosition = position;
        mCallPager = callPager;

        mImageView = (ImageView) view.findViewById(R.id.voice_click_ic);
        mCallLabel = (TextView) view.findViewById(R.id.call_title_text);
        mVoiceLabel = (TextView) view.findViewById(R.id.voice_title_text);
        mCallLabelImg = (ImageView) view.findViewById(R.id.call_title_img);
        mVoiceLabelImg = (ImageView) view.findViewById(R.id.voice_title_img);
        mImageLayout = (LinearLayout) view.findViewById(R.id.voice_click_layout);

        changePage();
        setStyle();

        (mView.findViewById(R.id.call_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallPager.setCurrentItem(0);
            }
        });
        (mView.findViewById(R.id.voice_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallPager.setCurrentItem(1);
            }
        });
        mImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mContext).showFragment(FragmentRecord.newInstance());
            }
        });

        PaintIconsHelper.setIconsColorsNew(mContext, mImageView, R.drawable.custom_ic_dictophone, R.color.btn_icon_white_color);
    }

    private static void changePage() {
        switch (mPosition) {
            case -1:
                setCheckedColor(mCallLabel, mCallLabelImg, R.drawable.menu_icon_main_screen);
                setUnCheckedColor(mVoiceLabel, mVoiceLabelImg, R.drawable.menu_icon_dictaphone);
                mImageLayout.setVisibility(View.GONE);
            case 0:
                setCheckedColor(mCallLabel, mCallLabelImg, R.drawable.menu_icon_main_screen);
                setUnCheckedColor(mVoiceLabel, mVoiceLabelImg, R.drawable.menu_icon_dictaphone);
                changeIcon(false);
                break;
            case 1:
                setUnCheckedColor(mCallLabel, mCallLabelImg, R.drawable.menu_icon_main_screen);
                setCheckedColor(mVoiceLabel, mVoiceLabelImg, R.drawable.menu_icon_dictaphone);
                changeIcon(true);
                break;
        }
    }

    private static void setCheckedColor(TextView textView, ImageView imageView, int imgId) {
        textView.setTextColor(mContext.getResources().getColor(R.color.custom_linear_text_color_checked));
        PaintIconsHelper.setIconsColorsNew(mContext, imageView, imgId, R.color.custom_linear_text_color_checked);
    }

    private static void setUnCheckedColor(TextView textView, ImageView imageView, int imgId) {
        textView.setTextColor(mContext.getResources().getColor(R.color.custom_linear_text_color_unchecked));
        PaintIconsHelper.setIconsColorsNew(mContext, imageView, imgId, R.color.custom_linear_text_color_unchecked);
    }

    private static void changeIcon(boolean isVisible) {
        Animation scale = null;
        if (isVisible) {
            mImageLayout.setVisibility(View.VISIBLE);
            scale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scale.setDuration(200);

        } else {
            scale = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scale.setDuration(200);
            scale.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mImageLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        AnimationSet animSet = new AnimationSet(true);
        animSet.setFillEnabled(true);
        animSet.addAnimation(scale);
        mImageLayout.startAnimation(animSet);
    }

    private static void setStyle() {
        (mView.findViewById(R.id.bottom_custom_layout)).setBackgroundColor(mContext.getResources().getColor(COLOR_TABBAR[SharedPreferencesFile.getThemeNumber()]));
    }
}
