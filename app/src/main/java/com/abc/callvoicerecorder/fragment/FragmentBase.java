package com.abc.callvoicerecorder.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.activity.MainActivity;

public class FragmentBase extends Fragment {
    protected MainActivity listenerActivity;
    protected DisplayMetrics metrics;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listenerActivity = (MainActivity) activity;
        metrics = activity.getResources().getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary_theme1));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (listenerActivity.getCurrentFragment() instanceof FragmentMain) {
                    listenerActivity.getDrawerLayoutHelper().openPanel();
                }
                else {
                    listenerActivity.onBackPressed();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
