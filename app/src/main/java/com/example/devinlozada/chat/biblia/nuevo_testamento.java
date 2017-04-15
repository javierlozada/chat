package com.example.devinlozada.chat.biblia;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devinlozada.chat.R;

/**
 * Created by djl on 15/04/17.
 */

public class nuevo_testamento extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_nuevo_testamento,container,false);
        return v;
    }
}
