package com.example.devinlozada.chat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class cambiarContrasena extends AppCompatActivity {

    ImageView check;
    String Email;
    FirebaseUser user;
    FirebaseAuth auth;
    ProgressBar progressBarOn;
    String email, password;


    EditText contrasenaNueva, confirmarContr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);
        showToolbar("Cambiar contraseña", true);
        contrasenaNueva = (EditText) findViewById(R.id.contrasenaNueva);
        confirmarContr  = (EditText) findViewById(R.id.confirmarContrasena);
        progressBarOn   = (ProgressBar) findViewById(R.id.progressOn);

        //Get Firebase auth instance
        user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();

        email               =  getIntent().getStringExtra("correo");
        password            =  getIntent().getStringExtra("contrasena");


        if(user != null){
            // user = FirebaseAuth.getInstance().getCurrentUser();
            Email = user.getEmail();
        }


        check = (ImageView) findViewById(R.id.check);
        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBarOn.setVisibility(View.VISIBLE);

                final String contrasenaNuevaStr = contrasenaNueva.getText().toString();
                final String confirmarContrStr = confirmarContr.getText().toString();


                if (TextUtils.isEmpty(contrasenaNuevaStr)) {
                    progressBarOn.setVisibility(View.GONE);
                    contrasenaNueva.setError("Ingresar nueva contraseña!");
                    return;
                }


                if (TextUtils.isEmpty(confirmarContrStr)) {
                    progressBarOn.setVisibility(View.GONE);
                    confirmarContr.setError("Ingresar confirmar contraseña!");
                    return;
                }

                if (!contrasenaNuevaStr.equals(confirmarContrStr)) {
                    progressBarOn.setVisibility(View.GONE);
                    contrasenaNueva.setError("La contraseña nueva y confirmar contraseña son diferentes");
                    return;
                }



                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, password);

// Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(contrasenaNuevaStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBarOn.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {
                                                Toast.makeText(cambiarContrasena.this,"Contraseña se actualizo!",
                                                        Toast.LENGTH_LONG).show();
                                                finish();
                                            } else {
                                                Toast.makeText(cambiarContrasena.this,"Error actualizando contraseña",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    System.out.println("Error auth failed");
                                }
                            }
                        });
            }
        });
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar PayPalItem clicks here.
        int id = item.getItemId();

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected




    private void showToolbar( String title, boolean upbutton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//Crea la compatibilidad con versiones anteriores a lolipop
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upbutton);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

    }// Fin showToolbar
}
