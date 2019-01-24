package com.mocom.com.mdancingproject.Adapter;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.mocom.com.mdancingproject.Fragments.StudentCoinPackageFragment;
import com.mocom.com.mdancingproject.Fragments.StudentCoinStoreFragment;

public class StudentCoinAdapter extends FragmentStatePagerAdapter {

    private static int NUM_ITEMS = 2;
    private SparseArray<Fragment> fragments;
    String title;

    public StudentCoinAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return StudentCoinPackageFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return StudentCoinStoreFragment.newInstance(1, "Page # 2");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            title = "Package";
        }else if(position == 1){
            title = "Coin Store";
        }
        return title;
    }
}
