package com.example.devinlozada.chat.biblia;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.devinlozada.chat.R;
import com.example.devinlozada.chat.externalFunctions.externalFunctions;

/**
 * Created by djl on 15/04/17.
 */

public class nuevo_testamento extends android.support.v4.app.Fragment {

    private String [] nuevoTestamento;
    private ListView nuevoTestamentoList;
    private ArrayAdapter<String> adapter;
    private String array[];



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v                  = inflater.inflate(R.layout.activity_nuevo_testamento,container,false);
        nuevoTestamento         = getActivity().getResources().getStringArray(R.array.nuevoTestamento);
        nuevoTestamentoList     = (ListView) v.findViewById(R.id.nuevo_testamento);


        array                   = getActivity().getResources().getStringArray(R.array.nuevoTestamento);
        adapter                 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, array);
        nuevoTestamentoList.setAdapter(adapter);


        nuevoTestamentoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent nuevoTestamentoList = new Intent(getActivity(), libros.class);
                nuevoTestamentoList.putExtra("Libro", ((TextView)view).getText().toString());
                startActivity(nuevoTestamentoList);
            }
        });


        return v;
    }
}
