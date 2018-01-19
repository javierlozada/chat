package com.example.devinlozada.chat.biblia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.devinlozada.chat.R;

public class nuevo_testamento_versiculos extends Fragment {
    private ListView antiguoTestamentoList;
    private String array[];
    private ArrayAdapter<String> adapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v                  = inflater.inflate(R.layout.activity_antiguo_testamento,container,false);


        return v;
    }

}
