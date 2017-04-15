package com.example.devinlozada.chat.biblia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.devinlozada.chat.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import static android.R.attr.fragment;

public class antiguo_testamento extends Fragment {
    ListView antiguoTestamentoList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_antiguo_testamento,container,false);
        antiguoTestamentoList = (ListView)v.findViewById(R.id.antiguo_testamento);
        return v;
    }

}
