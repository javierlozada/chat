package com.example.devinlozada.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.devinlozada.chat.externalFunctions.sharedSubMenu;
import com.example.devinlozada.chat.tabsHome.ViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Chat extends AppCompatActivity {

    /*Initializar componentes*/
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseUser user;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private NavigationView navigationView;
    private DrawerLayout submenu;
    private ImageView profilePhoto;
    private DatabaseReference myRef;
    private TextView name, email;
    private Toolbar toolbar;


    private sharedSubMenu shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        showToolbar("Causa y Efecto",false);

        auth = FirebaseAuth.getInstance();

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(Chat.this, Login.class));
                    finish();
                }
            }
        };


        submenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        shared  = new sharedSubMenu(Chat.this, submenu);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, submenu, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        submenu.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(shared);
        View header     = navigationView.getHeaderView(0);


        profilePhoto    = (ImageView) header.findViewById(R.id.profilePhoto);
        email           = (TextView)  header.findViewById(R.id.email);
        name            = (TextView) header.findViewById(R.id.name);

        tabLayout       = (TabLayout) findViewById(R.id.tab_layout);
        viewPager       = (ViewPager) findViewById(R.id.viewPager);

        TabLayout.Tab chat    = tabLayout.newTab();
        TabLayout.Tab groups  = tabLayout.newTab();
        TabLayout.Tab eventos = tabLayout.newTab();

        tabLayout.addTab(chat, 0);
        tabLayout.addTab(groups, 1);
        tabLayout.addTab(eventos, 2);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);

        /*cambio de color de los tabs*/
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.colorWhite));
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this,R.color.colorWhite));


        adapter = new ViewPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        FirebaseUser user           = auth.getCurrentUser();
        String UserID               = user.getEmail().replace("@","").replace(".","");


        myRef = FirebaseDatabase.getInstance().getReference().child("android").child(UserID);

        email.setText(user.getEmail());


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String photoURL     = dataSnapshot.child("image_Url").getValue(String.class);
                String nameStr      = dataSnapshot.child("Name").getValue(String.class);


                if(!myRef.equals("Null")){

                    name.setText(nameStr.trim());

                    Glide.with(Chat.this)
                            .load(photoURL)
                            .crossFade()
                            .thumbnail(0.5f)
                            .placeholder(R.drawable.loading)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(profilePhoto);
                }else {
                    //
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //viewPager.setCurrentItem(tab.getPosition());
                selectPage(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        //listener para tener el efecto de swipe entre tabs
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

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





        /*end listeners*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(Chat.this,SettingsActivity.class);
            startActivity(settings);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();
        String UserID               = user.getEmail().replace("@","").replace(".","");
        DatabaseReference mRootRef  = FirebaseDatabase.getInstance().getReference("isOnline").child("enLinea");
        DatabaseReference ref1      = mRootRef.child(UserID);
        ref1.child("isOnline").setValue("true");

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        String UserID               = user.getEmail().replace("@","").replace(".","");
        DatabaseReference mRootRef  = FirebaseDatabase.getInstance().getReference("isOnline").child("enLinea");
        DatabaseReference ref1      = mRootRef.child(UserID);
        ref1.child("isOnline").setValue("false");
    }

    @Override
    public void onPause() {
        super.onPause();
        String UserID               = user.getEmail().replace("@","").replace(".","");
        DatabaseReference mRootRef  = FirebaseDatabase.getInstance().getReference("isOnline").child("enLinea");
        DatabaseReference ref1      = mRootRef.child(UserID);
        ref1.child("isOnline").setValue("false");


    }

    @Override
    public void onResume() {
        super.onResume();
        String UserID               = user.getEmail().replace("@","").replace(".","");
        DatabaseReference mRootRef  = FirebaseDatabase.getInstance().getReference("isOnline").child("enLinea");
        DatabaseReference ref1      = mRootRef.child(UserID);
        ref1.child("isOnline").setValue("true");


        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    void selectPage(int pageIndex){
        tabLayout.setScrollPosition(pageIndex,0f,true);
        viewPager.setCurrentItem(pageIndex);
    }


    private void showToolbar( String title, boolean upbutton) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//Crea la compatibilidad con versiones anteriores a lolipop
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upbutton);

    }// Fin showToolbar


}
