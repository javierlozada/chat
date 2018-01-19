package com.example.devinlozada.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private Button login;
    private EditText correo, password;
    private ProgressBar progressBar;
    private TextView signUpLabel, forgotLabel;
    private FirebaseAuth auth;
    private RelativeLayout loginLayout;

    static String LoggedIn_User_Email;
    public static int Device_Width;
    String getEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle extras = getIntent().getExtras();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

      if (auth.getCurrentUser() != null) {

          if(extras != null){
              getEmail = extras.getString("Email");
              LoggedIn_User_Email = getEmail;
          }

            startActivity(new Intent(Login.this, Chat.class));
            finish();
        }



        login           = (Button) findViewById(R.id.loginButton);
        correo          = (EditText) findViewById(R.id.input_correo);
        password        = (EditText) findViewById(R.id.input_contra);
        progressBar     = (ProgressBar) findViewById(R.id.progressBar);
        signUpLabel     = (TextView) findViewById(R.id.signup);
        forgotLabel     = (TextView) findViewById(R.id.olvidecontra);
        loginLayout     = (RelativeLayout) findViewById(R.id.activity_chat_fire_base);

        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        Device_Width = metrics.widthPixels;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user     = correo.getText().toString().trim();
                String pass     = password.getText().toString().trim();

                if (TextUtils.isEmpty(user)) {
                    correo.setError("Ingresar usuario!");
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    password.setError("Ingresar contraseña!");
                    return;
                }

                if (password.length() < 6) {
                    password.setError("Contraseña muy corta, ingrese minimo 6 caracteres!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(user, pass)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        password.setError("Contraseña muy corta, ingrese minimo 6 caracteres!");
                                    } else {
                                        Snackbar.make(loginLayout, "Fallo en la autenticacion!", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                } else {
                                    Intent intent = new Intent(Login.this, Chat.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });




        signUpLabel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(Login.this, signUp.class);
                startActivity(signUp);
                finish();
            }
        });

        forgotLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPass = new Intent(Login.this,forgotPassword.class);
                startActivity(forgotPass);
                finish();
            }
        });
        /*end Eventos*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
