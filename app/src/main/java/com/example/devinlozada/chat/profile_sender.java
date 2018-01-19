package com.example.devinlozada.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile_sender extends AppCompatActivity {

    private String name;
    private String email;
    private String profile_photo;

    private ImageView profile_photoImage;
    private TextView numero_cell,estado, fechaCumpleanos;

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_sender);

        email           = getIntent().getStringExtra("email_sender");
        name            = getIntent().getStringExtra("name");

        showToolbar(name,true);

        profile_photo   = getIntent().getStringExtra("Profile_photo");

        profile_photoImage  = (ImageView) findViewById(R.id.profile_photo);
        numero_cell         = (TextView) findViewById(R.id.numeroCelular);
        estado              = (TextView) findViewById(R.id.estado);
        fechaCumpleanos     = (TextView) findViewById(R.id.fechaCumpleanos);

        myRef = FirebaseDatabase.getInstance().getReference().child("android").child(email.replace("@","").replace(".",""));


        Glide.with(profile_sender.this)
                .load(profile_photo)
                .crossFade()
                .thumbnail(0.5f)
                .placeholder(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profile_photoImage);



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String numero_celular     = dataSnapshot.child("Numero_celular").getValue(String.class);
                String estadoStr          = dataSnapshot.child("Estado").getValue(String.class);
                String fecha_cumpleanos   = dataSnapshot.child("cumpleanos").getValue(String.class);

               if(fecha_cumpleanos == null){
                   fechaCumpleanos.setText("Fecha cumpleaños");
               }else {
                   fechaCumpleanos.setText(fecha_cumpleanos);
               }

               if(numero_celular == null){
                   numero_cell.setText("Numero celular");
               }else {
                   numero_cell.setText(numero_celular);
               }

                estado.setText(estadoStr);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
