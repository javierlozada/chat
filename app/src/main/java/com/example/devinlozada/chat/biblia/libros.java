package com.example.devinlozada.chat.biblia;

import android.app.Activity;
import android.app.Dialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.devinlozada.chat.R;
import com.example.devinlozada.chat.externalFunctions.externalFunctions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class libros extends AppCompatActivity {

    private externalFunctions func = new externalFunctions();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterAnt adapter;
    private  int numberOfTabs;
    private String getLibro;
    private Object[] libro_cap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antigua_libros);

        getLibro =  getIntent().getStringExtra("Libro");


        showToolbar(getLibro,true);


        tabLayout       = (TabLayout) findViewById(R.id.tab_layout2);
        viewPager       = (ViewPager) findViewById(R.id.viewPager);


        viewPager.setCurrentItem(1);

        adapter = new AdapterAnt(getSupportFragmentManager());


        numberOfTabs = func.numberOFTab(getLibro);

        for (int i = 1; i <= numberOfTabs; i ++){

           adapter.addFrag(new capitulo_versiculos(), "Capitulo " + i);

        }//end loop

        viewPager.setAdapter(adapter);


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        /*cambio de color de los tabs*/
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.colorWhite));
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this,R.color.colorWhite));

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                super.onTabSelected(tab);

            }

            private void SelectTab(TabLayout.Tab tab){
                Bundle bundle = new Bundle();
                bundle.putString("libro","Génesis");
                bundle.putString("Capitulo","Capitulo_1");
                capitulo_versiculos ver = new capitulo_versiculos();
                ver.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.content_contacts,ver).commit();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab){
                if(tab.getPosition() == 0)
                    SelectTab(tab);


                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        SelectTab(tab);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

            }
        });




    }//end onCreate


    class AdapterAnt extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList    = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public AdapterAnt(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return  mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFrag(Fragment fragment, String title){

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }


    private void showToolbar( String title, boolean upbutton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//Crea la compatibilidad con versiones anteriores a lolipop
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upbutton);

    }// Fin showToolbar

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar PayPalItem clicks here.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        if(id == R.id.aceptar_action){

            escogerDialogCap(getLibro,numberOfTabs, this);

        }

        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_antiguo_libros, menu);
        return true;
    }


    //Dialogo buscar escoger Capitulo
    private void escogerDialogCap(String title, int numberCap, Activity context){

        final Dialog dialog = new Dialog(libros.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_capitulos_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width    = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height   = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity  = Gravity.CENTER;
        ArrayAdapter<String> adapter;

        dialog.getWindow().setAttributes(lp);

        Button buttonCancelar   = (Button) dialog.findViewById(R.id.cancelBtn);

        final TextView NombreLibro    = (TextView) dialog.findViewById(R.id.NombreLibro);
        final TextView totalCapitulos = (TextView) dialog.findViewById(R.id.totalCapitulos);
        final ListView listCap        = (ListView) dialog.findViewById(R.id.listCap);

        NombreLibro.setText(title);
        totalCapitulos.setText(numberCap + " Capitulos");

        String[] arreglo = createArray(numberCap);

        adapter                 = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, arreglo);
        listCap.setAdapter(adapter);

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        listCap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               viewPager.setCurrentItem(i);
               dialog.dismiss();
            }
        });

        dialog.show();

    }


    private String[] createArray(int numberOfTabs){

      ArrayList<String> mList = new ArrayList<>(numberOfTabs);

        for(int i = 1; i <= numberOfTabs; i++){
          mList.add("Capitulo " + i);
        }

        String[] memArray = mList.toArray(new String[mList.size()]);

        return memArray;

    }


}
