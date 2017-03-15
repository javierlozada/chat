package com.example.devinlozada.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class signUp extends AppCompatActivity {

    private CallbackManager callbackManager;
    private FirebaseAuth auth;
    private Button inscribirse;
    private EditText correo, password;
    private ProgressBar progressBar;
    private LoginButton facebook_login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth            = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        inscribirse     = (Button) findViewById(R.id.Inscribirse);
        correo          = (EditText) findViewById(R.id.input_correo);
        password        = (EditText) findViewById(R.id.input_contra);
        progressBar     = (ProgressBar) findViewById(R.id.progressBar);
        facebook_login  = (LoginButton) findViewById(R.id.facebook_button);


        /*Eventos*/
        facebook_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facbook", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {}
        });



        inscribirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email     = correo.getText().toString().trim();
                String pass     = password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    correo.setError("Ingresar correo!");
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
                auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(signUp.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(signUp.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(signUp.this, ChatFireBase.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }



    private void handleFacebookAccessToken(AccessToken token) {
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(signUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
