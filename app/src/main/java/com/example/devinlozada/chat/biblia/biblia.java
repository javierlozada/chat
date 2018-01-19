package com.example.devinlozada.chat.biblia;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.devinlozada.chat.R;
import com.example.devinlozada.chat.externalFunctions.externalFunctions;
import com.example.devinlozada.chat.externalFunctions.sharedSubMenu;

public class biblia extends AppCompatActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerBiblia adapter;



    private sharedSubMenu shared;
    private externalFunctions externalFunction = new externalFunctions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblia);

        externalFunction.showToolbar(biblia.this, "Biblia", true);

        tabLayout                       = (TabLayout) findViewById(R.id.tab_layout);
        viewPager                       = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setCurrentItem(1);

        final TabLayout.Tab nuevo_testamento    = tabLayout.newTab().setText("Nuevo");
        final TabLayout.Tab antiguo_testamento  = tabLayout.newTab().setText("Antiguo");

        tabLayout.addTab(nuevo_testamento,  0);
        tabLayout.addTab(antiguo_testamento,1);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

         /*cambio de color de los tabs*/
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.colorWhite));
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this,R.color.colorWhite));

        adapter                         = new ViewPagerBiblia(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

         /*Listeners*/
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onBackPressed() {

        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contacts, menu);
        return true;
    }


    /*when click on the message chat icon in the appbar will open a new Navigation view*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar PayPalItem clicks here.
        int id = item.getItemId();

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
