package com.example.devinlozada.chat.biblia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.devinlozada.chat.R;

public class antiguo_testamento extends Fragment {
    private ListView antiguoTestamentoList;
    private String array[];
    private ArrayAdapter<String> adapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v                  = inflater.inflate(R.layout.activity_antiguo_testamento,container,false);

        antiguoTestamentoList   = (ListView) v.findViewById(R.id.cap_versiculo);
        array                   = getActivity().getResources().getStringArray(R.array.antiguoTestamento);
        adapter                 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, array);
        antiguoTestamentoList.setAdapter(adapter);


        antiguoTestamentoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent antig_testamento = new Intent(getActivity(), libros.class);
                antig_testamento.putExtra("Libro", ((TextView)view).getText().toString());
                startActivity(antig_testamento);
            }
        });

        return v;
    }

}
