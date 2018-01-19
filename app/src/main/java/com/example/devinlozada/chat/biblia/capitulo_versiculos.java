package com.example.devinlozada.chat.biblia;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
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
import android.widget.Toast;

import com.example.devinlozada.chat.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class capitulo_versiculos extends Fragment {
    private ListView cap_Ver_List;
    private TypedArray array;
    private ArrayAdapter<String> adapter;
    private List listaCap;
    private ClipData myClip;
    private String libro,cap;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v          = inflater.inflate(R.layout.activity_antiguo_testamento,container,false);
        final ClipboardManager clipboard =(ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        cap_Ver_List    = (ListView) v.findViewById(R.id.cap_versiculo);
        array           = getActivity().getResources().obtainTypedArray(R.array.Génesis_Capitulos);

        if(getArguments() != null){

            libro = getArguments().getString("libro");
            cap   = getArguments().getString("Capitulo");
            int index = getIndex(cap,libro,array);
            System.out.println(index);
        }


        CharSequence[] capitulosChar = array.getTextArray(0);
        listaCap = new ArrayList<CharSequence>(Arrays.asList(capitulosChar));

        adapter         = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, listaCap);
        cap_Ver_List.setAdapter(adapter);

        cap_Ver_List.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                myClip = ClipData.newPlainText("text", ((TextView)view).getText().toString());
                clipboard.setPrimaryClip(myClip);

                Toast.makeText(getContext(),"Copiado: " + ((TextView)view).getText().toString(),Toast.LENGTH_LONG).show();

                return true;
            }
        });


        return v;
    }

        private int getIndex(String cap, String libros, TypedArray array){

        int index = 0;

        for(int i= 0; i <= array.length(); i++ ){

                String concatCap_titulo = cap.concat("_").concat(libros).concat("_" + i);

                if(concatCap_titulo.equals("Génesis_Capitulo_2")){
                    index = i;
                }

            }

            return index;
        }


}
