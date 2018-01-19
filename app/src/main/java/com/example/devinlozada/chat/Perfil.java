package com.example.devinlozada.chat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Perfil extends AppCompatActivity implements android.app.DatePickerDialog.OnDateSetListener {

    ImageView cameraButton;

    final CharSequence[] options = {"Camara", "Galeria"};
    String[] permissions= new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,};
    public static final int READ_EXTERNAL_STORAGE = 0,MULTIPLE_PERMISSIONS = 10;
    private String pictureImagePath = "";
    private static final int GALLERY_INTENT = 2;
    private Uri mImageUri = Uri.EMPTY;
    private ProgressDialog mProgressDialog;

    private DatabaseReference myRef;

    private ImageView profileImageView;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private TextView nombre,numero_celular,status,fullDate;

    private CardView changePassword,userNameCardView,view2,statusCardView,birthDate,phoneNumber;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        showToolbar("Perfil",true);

        cameraButton        = (ImageView) findViewById(R.id.cameraButton);
        mProgressDialog     =  new ProgressDialog(this);
        profileImageView    = (ImageView) findViewById(R.id.imageView1);
        changePassword      = (CardView) findViewById(R.id.changePassword);
        nombre              = (TextView) findViewById(R.id.name);
        userNameCardView    = (CardView) findViewById(R.id.usernameCardView);
        appBarLayout        = (AppBarLayout) findViewById(R.id.app_bar);
        view2               = (CardView) findViewById(R.id.view2);
        statusCardView      = (CardView) findViewById(R.id.statusCardView);
        birthDate           = (CardView) findViewById(R.id.birthDate);
        phoneNumber         = (CardView) findViewById(R.id.phoneNumber);
        numero_celular      = (TextView) findViewById(R.id.numero_celular);
        status              = (TextView) findViewById(R.id.status);
        fullDate            = (TextView) findViewById(R.id.cumpleAnos);

        birthDate.setOnClickListener(datePickerDialog);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout.setTitle("Perfil");
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimary));

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if(auth != null){
            user = auth.getCurrentUser();
            String email        = user.getEmail();
            String USER_ID      = email.replace("@","").replace(".","");
            myRef = FirebaseDatabase.getInstance().getReference().child("android").child(USER_ID);
        }

        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Perfil.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_phonenumber_dialog);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width    = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height   = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity  = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);

                Button buttonCancelar      = (Button) dialog.findViewById(R.id.cancel_action);
                Button buttonAceptar       = (Button) dialog.findViewById(R.id.aceptar_action);
                final EditText phoneNumber = (EditText) dialog.findViewById(R.id.phoneNumber);

                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                buttonAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phoneNumberStr = phoneNumber.getText().toString().trim();

                        if (TextUtils.isEmpty(phoneNumberStr)) {
                            phoneNumber.setError("Ingresar numero de celular!");
                            return;
                        }


                        FirebaseUser user           = auth.getCurrentUser();
                        String UserID               = user.getEmail().replace("@","").replace(".","");
                        DatabaseReference mRootRef  = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference ref1      = mRootRef.child("android").child(UserID);
                        ref1.child("Numero_celular").setValue(phoneNumberStr.toString());
                        dialog.dismiss();

                    }
                });



                dialog.show();
            }


        });




        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            }
        });

        statusCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Perfil.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_estado_dialog);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width    = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height   = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity  = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);

                Button buttonCancelar      = (Button) dialog.findViewById(R.id.cancel_action);
                Button buttonAceptar       = (Button) dialog.findViewById(R.id.aceptar_action);
                final EditText estado = (EditText) dialog.findViewById(R.id.estado);

                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                buttonAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String estadoStr = estado.getText().toString().trim();
                        FirebaseUser user           = auth.getCurrentUser();
                        String UserID               = user.getEmail().replace("@","").replace(".","");
                        DatabaseReference mRootRef  = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference ref1      = mRootRef.child("android").child(UserID);

                        if (TextUtils.isEmpty(estadoStr)) {
                            ref1.child("Estado").setValue("Disponible");
                            return;
                        }else {
                            ref1.child("Estado").setValue(estadoStr.toString());

                        }

                        dialog.dismiss();




                    }
                });



                dialog.show();
            }





        });




        userNameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Perfil.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_username_dialog);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width    = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height   = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity  = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);

                Button buttonCancelar      = (Button) dialog.findViewById(R.id.cancel_action);
                Button buttonAceptar       = (Button) dialog.findViewById(R.id.aceptar_action);
                final EditText username    = (EditText) dialog.findViewById(R.id.usuario);

                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                buttonAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String usernameStr = username.getText().toString().trim();

                        if (TextUtils.isEmpty(usernameStr)) {
                            username.setError("Ingresar nuevo nombre usuario actual!");
                            return;
                        }


                        FirebaseUser user           = auth.getCurrentUser();
                        String UserID               = user.getEmail().replace("@","").replace(".","");
                        DatabaseReference mRootRef  = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference ref1      = mRootRef.child("android").child(UserID);
                        ref1.child("Name").setValue(usernameStr.toString());
                        dialog.dismiss();

                    }
                });



                dialog.show();
            }
        });


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Perfil.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_login_dialog);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width    = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height   = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity  = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);

                Button buttonCancelar   = (Button) dialog.findViewById(R.id.cancel_action);
                Button buttonSiguiente  = (Button) dialog.findViewById(R.id.siguiente_action);
                final EditText email    = (EditText) dialog.findViewById(R.id.usuario);
                final EditText contrasena = (EditText) dialog.findViewById(R.id.contrasena);

                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                buttonSiguiente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String emailStr = email.getText().toString().trim();
                        String contrsenaStr = contrasena.getText().toString().trim();


                        if (TextUtils.isEmpty(emailStr)) {

                            email.setError("Ingresar correo actual!");
                            return;
                        }


                        if (TextUtils.isEmpty(contrsenaStr)) {

                            contrasena.setError("Ingresar contraseña actual!");
                            return;
                        }

                        Intent changePassword = new Intent(Perfil.this, cambiarContrasena.class);
                        changePassword.putExtra("correo", emailStr);
                        changePassword.putExtra("contrasena", contrsenaStr);
                        startActivity(changePassword);
                        dialog.dismiss();

                    }
                });



                dialog.show();

            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String photoURL         = (String) dataSnapshot.child("image_Url").getValue(String.class);
                String numeroCell       = (String) dataSnapshot.child("Numero_celular").getValue(String.class);
                String estadoStr        = (String) dataSnapshot.child("Estado").getValue(String.class);
                String nameStr          = (String) dataSnapshot.child("Name").getValue(String.class);
                String cumple           = (String) dataSnapshot.child("cumpleanos").getValue(String.class);

                nombre.setText(nameStr.trim());

                if(cumple == null){
                    fullDate.setText("Fecha cumpleaños");
                }else {
                    fullDate.setText(cumple);
                }

                if(numeroCell != null){
                    numero_celular.setText(numeroCell.trim());
                }else {
                    numero_celular.setText("Numero de celular");
                }

                if(estadoStr==null){
                    status.setText("Disponible");
                }else {
                    status.setText(estadoStr.trim());
                }

                if(!myRef.equals("Null")){
                    Glide.with(Perfil.this)
                            .load(photoURL)
                            .crossFade()
                            .thumbnail(0.5f)
                            .placeholder(R.drawable.loading)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(profileImageView);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Perfil.this);
                builder.setTitle("Elegir Fuente");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Camara")) {
                            if (checkPermissions()) {
                                callCamera();
                            }
                        }
                        if(options[item].equals("Galeria")){
                            if (ContextCompat.checkSelfPermission(Perfil.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(Perfil.this,
                                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                            }
                            else {
                                callgalary();
                            }
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private View.OnClickListener datePickerDialog = new View.OnClickListener(){


        @Override
        public void onClick(View v) {

            DialogFragment datePicker = new DateDialog();
            datePicker.show(getFragmentManager(),"normal");

        }
    };

    private void showToolbar( String title, boolean upbutton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//Crea la compatibilidad con versiones anteriores a lolipop
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upbutton);

    }// Fin showToolbar

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    private void callCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        Log.d("LOGGED", "imageFileName :  "+ imageFileName);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;


        File file = new File(pictureImagePath);

        Uri outputFileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getApplicationContext().getPackageName() + ".provider", file);


        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        cameraIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        Log.d("LOGGED", "pictureImagePath :  "+ pictureImagePath);
        Log.d("LOGGED", "outputFileUri :  "+ outputFileUri);

        startActivityForResult(cameraIntent, 5);
    }

    private void callgalary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d("LOGGED", "InSIDE onActivityResult : ");
        Log.d("LOGGED", " requestCode : " + requestCode+" resultCode : " + resultCode+" DATA "+data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            mImageUri = data.getData();
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Chat_Images").child(mImageUri.getLastPathSegment());
            Log.d("LOGGED", "ImageURI : " +mImageUri);


            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();

                    FirebaseUser user           = auth.getCurrentUser();
                    String UserID               = user.getEmail().replace("@","").replace(".","");
                    DatabaseReference mRootRef  = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference ref1      = mRootRef.child("android").child(UserID);
                    ref1.child("image_Url").setValue(downloadUri.toString());

                    if (!downloadUri.toString().equals("Null")) {
                        Glide.with(Perfil.this)
                                .load(downloadUri.toString())
                                .crossFade()
                                .thumbnail(0.5f)
                                .placeholder(R.drawable.loading)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(profileImageView);
                    }
                    mProgressDialog.dismiss();
                }
            });
        }

        else if (requestCode == 5 && resultCode == RESULT_OK ) {

            File imgFile = new  File(pictureImagePath);
            if(imgFile.exists()) {
                Log.d("LOGGED", "imgFile : " + imgFile);

                Uri fileUri =Uri.fromFile(imgFile);
                Log.d("LOGGED", "fileUri : " + fileUri);

                StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Chat_Images").child(fileUri.getLastPathSegment());

                filePath.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();
                        ArrayMap<String, String> map = new ArrayMap<>();
                        map.put("image_Url", downloadUri.toString());

                        if (!downloadUri.toString().equals("Null")) {
                            Glide.with(Perfil.this)
                                    .load(downloadUri.toString())
                                    .crossFade()
                                    .thumbnail(0.5f)
                                    .placeholder(R.drawable.loading)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(profileImageView);
                        }


                        mProgressDialog.dismiss();
                    }
                });
            }
        }

        else if (requestCode == 5)
        {
            Toast.makeText(this, "resultCode : "+ resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar PayPalItem clicks here.
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent goBack = new Intent(Perfil.this,Chat.class);
            startActivity(goBack);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(Perfil.this).clearMemory();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goBack = new Intent(Perfil.this,Chat.class);
        startActivity(goBack);
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String fullDateStr = setDate(month, dayOfMonth,year);

        FirebaseUser user           = auth.getCurrentUser();
        String UserID               = user.getEmail().replace("@","").replace(".","");
        DatabaseReference mRootRef  = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref1      = mRootRef.child("android").child(UserID);
        ref1.child("cumpleanos").setValue(fullDateStr.toString());

        fullDate.setText(fullDateStr );

    }



    /*function to put date in right format*/
    private String setDate(int mMonth, int mDay, int year) {

        String month, day, fulldate;
        switch (mMonth + 1) {
            case 1:
                month = "Enero";
                break;
            case 2:
                month = "Febrero";
                break;
            case 3:
                month = "Marzo";
                break;
            case 4:
                month = "Abril";
                break;
            case 5:
                month = "Mayo";
                break;
            case 6:
                month = "Junio";
                break;
            case 7:
                month = "Julio";
                break;
            case 8:
                month = "Agosto";
                break;
            case 9:
                month = "Septiembre";
                break;
            case 10:
                month = "Octubre";
                break;
            case 11:
                month = "Noviembre";
                break;
            case 12:
                month = "Diciembre";
                break;
            default:
                month = "";
                break;
        }

        if (mDay < 10) {
            day = "0" + mDay;
        } else {
            day = "" + mDay;
        }

        fulldate = year + "-" + month + "-" + day;


        return fulldate;
    }
}
