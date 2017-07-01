package com.example.devinlozada.chat;

import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by devinlozada on 28/02/17.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;



    public ViewPagerAdapter(FragmentManager fm,int NumOfTabs) {
        super(fm);
        this.numOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                tabFragmentGroup tab1 = new tabFragmentGroup();
                return tab1 ;
            case 1:
                tabFragmentEventos tab2 = new tabFragmentEventos();
                return tab2;
            default:
                return null;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        //this is where you set the titles
        switch(position) {
            case 0:
                return "Chat";
            case 1:
                return "Eventos";
        }
        return null;
    }


    @Override
    public int getCount() {
        return numOfTabs;
    }
}
