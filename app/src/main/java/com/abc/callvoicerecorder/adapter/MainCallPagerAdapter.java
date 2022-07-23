package com.abc.callvoicerecorder.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import android.view.ViewGroup;

import com.abc.callvoicerecorder.R;
import com.abc.callvoicerecorder.fragment.FragmentList;
import com.abc.callvoicerecorder.fragment.FragmentCall;


public class MainCallPagerAdapter extends FragmentStatePagerAdapter {
    private static final int PAGE_COUNT = 2;
    private static String[] TITLE_FRAGMENTS;

    public MainCallPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        TITLE_FRAGMENTS = new String[]{
                context.getString(R.string.main_screen_call_all_fragment),
                context.getString(R.string.main_screen_call_incoming_fragment),
                context.getString(R.string.main_screen_call_outgoing_fragment),
                context.getString(R.string.main_screen_call_favorite_fragment)
        };

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FragmentCall();
                break;
            case 1:
                fragment = new FragmentList();
                break;
//            case 1:
//                fragment = new CallIncomingFragment();
//                break;
//            case 2:
//                fragment = new CallOutgoingFragment();
//                break;
//            case 3:
//                fragment = new CallFavoriteFragment();
//                break;
        }
        return fragment;
    }

    public CharSequence getPageTitle(int position) {
        return TITLE_FRAGMENTS[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        trans.commit();

        super.destroyItem(container, position, object);
    }


}
