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
import com.example.devinlozada.chat.externalFunctions.sharedSubMenu;

public class biblia extends AppCompatActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerBiblia adapter;

    private sharedSubMenu shared  = new sharedSubMenu(biblia.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout                       = (TabLayout) findViewById(R.id.tab_layout);
        viewPager                       = (ViewPager) findViewById(R.id.viewPager);
        NavigationView navigationView   = (NavigationView) findViewById(R.id.nav_view);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        adapter         = new ViewPagerBiblia(getSupportFragmentManager(),tabLayout.getTabCount());

        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(1);


        final TabLayout.Tab nuevo_testamento    = tabLayout.newTab().setText("Nuevo");
        final TabLayout.Tab antiguo_testamento  = tabLayout.newTab().setText("Antiguo");

        tabLayout.addTab(nuevo_testamento,  0);
        tabLayout.addTab(antiguo_testamento,1);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


         /*cambio de color de los tabs*/
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.colorWhite));
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this,R.color.colorWhite));

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

        navigationView.setNavigationItemSelectedListener(shared);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contacts, menu);
        return true;
    }

}
