package com.example.devinlozada.chat.externalFunctions;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.devinlozada.chat.R;

/**
 * Created by djl on 15/11/17.
 */

public class externalFunctions {

    public void showToolbar(Activity activity, String title, boolean upbutton) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        ((AppCompatActivity) (activity)).setSupportActionBar(toolbar);//Crea la compatibilidad con versiones anteriores a lolipop
        ((AppCompatActivity) (activity)).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) (activity)).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) (activity)).getSupportActionBar().setDisplayHomeAsUpEnabled(upbutton);

    }// Fin showToolbar

    public  Integer numberOFTab(String libro){
        Integer numTabs = 0;

        switch (libro){
            case "Génesis":
                numTabs = 50;
                break;
            case "Exodo":
                numTabs = 40;
                break;
            case "Levítico":
                numTabs = 27;
                break;
            case "Números":
                numTabs = 36;
                break;
            case "Deuteronomio":
                numTabs = 34;
                break;
            case "Josué":
                numTabs = 24;
                break;
            case "Jueces":
                numTabs = 21;
                break;
            case "Rut":
                numTabs = 4;
                break;
            case "1 Samuel":
                 numTabs = 31;
                break;
            case "2 Samuel":
                numTabs = 24;
                break;
            case "1 Reyes":
                numTabs = 22;
                break;
            case "2 Reyes":
                numTabs = 25;
                break;
            case "1 Crónicas":
                numTabs = 29;
                break;
            case "2 Crónicas":
                numTabs = 36;
                break;
            case "Esdras":
                numTabs = 10;
                break;
            case "Nehemías":
                numTabs = 13;
                break;
            case "Ester":
                numTabs = 10;
                break;
            case "Job":
                numTabs = 42;
                break;
            case "Salmos":
                numTabs = 150;
                break;
            case "Proverbios":
                numTabs = 31;
                break;
            case "Eclesiastés":
                numTabs = 12;
                break;
            case "Cantares":
                numTabs = 8;
                break;
            case "Isaías":
                numTabs = 66;
                break;
            case "Jeremías":
                numTabs = 52;
                break;
            case "Lamentaciones":
                numTabs = 5;
                break;
            case "Ezequiel":
                numTabs = 48;
                break;
            case "Daniel":
                numTabs = 12;
                break;
            case "Oseas":
                numTabs = 14;
                break;
            case "Joel":
                numTabs = 3;
                break;
            case "Amos":
                numTabs = 9;
                break;
            case "Abdías":
                numTabs = 1;
                break;
            case "Jonás":
                numTabs = 4;
                break;
            case "Miqueas":
                numTabs = 7;
                break;
            case "Nahúm":
                numTabs = 3;
                break;
            case "Habacuc":
                numTabs = 3;
                break;
            case "Sofonías":
                numTabs = 3;
                break;
            case "Hageo":
                numTabs = 2;
                break;
            case "Zacarías":
                numTabs = 14;
                break;
            case "Malaquías":
                numTabs = 4;
                break;
            case "Mateo":
                numTabs = 28;
                break;
            case "Marcos":
                numTabs = 16;
                break;
            case "Lucas":
                numTabs = 24;
                break;
            case "Juan":
                numTabs = 21;
                break;
            case "Hechos":
                numTabs = 28;
                break;
            case "Romanos":
                numTabs = 16;
                break;
            case "1 Corintios":
                numTabs =  16;
                break;
            case "2 Corintios":
                numTabs = 13;
                break;
            case "Gálatas":
                numTabs = 6;
                break;
            case "Efesios":
                numTabs = 6;
                break;
            case "Filipenses":
                numTabs = 4;
                break;
            case "Colosenses":
                numTabs = 4;
                break;
            case "1 Tesalonicenses":
                numTabs = 5;
                break;
            case "2 Tesalonicenses":
                numTabs = 3;
                break;
            case "1 Timoteo":
                numTabs = 6;
                break;
            case "2 Timoteo":
                numTabs = 4;
                break;
            case "Tito":
                numTabs = 3;
                break;
            case "Filemón":
                numTabs = 1;
                break;
            case "Hebreos":
                numTabs = 13;
                break;
            case "Santiago":
                numTabs = 5;
                break;
            case "1 Pedro":
                numTabs = 5;
                break;
            case "2 Pedro":
                numTabs = 3;
                break;
            case "1 Juan":
                numTabs = 5;
                break;
            case "2 Juan":
                numTabs = 1;
                break;
            case "3 Juan":
                numTabs = 1;
                break;
            case "Judas":
                numTabs = 1;
                break;
            case "Apocalipsis":
                numTabs = 22;
                break;


        }//end switch/case

        return numTabs;
    }//end numberOFTab


}
