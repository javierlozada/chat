package com.example.devinlozada.chat.biblia;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.devinlozada.chat.tabFragment;

/**
 * Created by devinlozada on 28/02/17.
 */

public class ViewPagerBiblia extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ViewPagerBiblia(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                antiguo_testamento tab1 = new antiguo_testamento();
                return tab1;

            case 1:
                nuevo_testamento tab2 = new nuevo_testamento();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
