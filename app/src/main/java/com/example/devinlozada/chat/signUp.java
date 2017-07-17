package com.example.devinlozada.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.devinlozada.chat.firebase.MyFirebaseInstanceIDService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class signUp extends AppCompatActivity {

    private CallbackManager callbackManager;
    private FirebaseAuth auth;
    private Button inscribirse;
    private EditText correo, password, nombreyapellido;
    private ProgressBar progressBar;
    private LoginButton facebook_login;

    private String LoggedIn_User_Email;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    String getFireBase, fireBaseToken;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

         /* iniciating getSharedPreferences to put Strings and to Edit*/
        prefs  = getSharedPreferences("Prefs", MODE_PRIVATE);
        editor = prefs.edit();


        auth            = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        inscribirse     = (Button) findViewById(R.id.Inscribirse);
        correo          = (EditText) findViewById(R.id.input_correo);
        password        = (EditText) findViewById(R.id.input_contra);
        progressBar     = (ProgressBar) findViewById(R.id.progressBar);
        facebook_login  = (LoginButton) findViewById(R.id.facebook_button);
        nombreyapellido = (EditText) findViewById(R.id.input_nombreyapellido);

        new firebaseinstanceIdTask().execute();

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

                final String email     = correo.getText().toString().trim();
                String pass      = password.getText().toString().trim();

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
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(signUp.this, "Authentication failed." + task.getException(), Toast.LENGTH_LONG).show();
                                } else {

                                    FirebaseUser user   = auth.getCurrentUser();
                                    LoggedIn_User_Email = user.getEmail();


                                    editor.putString("Email", LoggedIn_User_Email);
                                    editor.commit();

                                    FirebaseMessaging.getInstance().subscribeToTopic("news");

                                    String UserID               = user.getEmail().replace("@","").replace(".","");
                                    DatabaseReference mRootRef  = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference ref1      = mRootRef.child("android").child(UserID);
                                    ref1.child("Name").setValue(nombreyapellido.getText().toString().trim());
                                    ref1.child("image_Url").setValue("Null");
                                    ref1.child("Email").setValue(user.getEmail());
                                    ref1.child("FirebaseToken").setValue(fireBaseToken);

                                    DatabaseReference mRootRef2  = FirebaseDatabase.getInstance().getReference("isOnline").child("enLinea");

                                    DatabaseReference ref2     = mRootRef2.child(UserID);
                                    ref2.child("isOnline").setValue("true");
                                    ref2.child("Escribiendo").setValue("false");


                                    Intent signUp = new Intent(signUp.this,Login.class);
                                    signUp.putExtra("Email",LoggedIn_User_Email);
                                    startActivity(signUp);
                                    finish();


                                    //startActivity(new Intent(signUp.this, Login.class));

                                   //finish();

                                    Toast.makeText(signUp.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }


    /*obtengo el firebase token*/
    private class firebaseinstanceIdTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            MyFirebaseInstanceIDService firebaseinstanceID = new MyFirebaseInstanceIDService();
            firebaseinstanceID.onTokenRefresh();
            getFireBase = firebaseinstanceID.getFireBaseToken();
            return getFireBase;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if(result != null){

                    fireBaseToken = getFireBase;

                }
            }catch (Exception e){
                e.printStackTrace();
            }//end try/catch

        }//end onPostExecute
    }//end firebaseinstanceIdTask

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
