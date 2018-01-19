package com.example.devinlozada.chat;

import android.*;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.devinlozada.chat.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class publicar extends AppCompatActivity {

    private String image_URL,name;
    private ImageView photoURL;
    private TextView nombre;
    private CardView chooseFotoVideo;
    final CharSequence[] options = {"Photo", "Video"};
    private String pictureImagePath = "";
    private static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 101;

    String[] permissions= new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int READ_EXTERNAL_STORAGE = 0,MULTIPLE_PERMISSIONS = 10;
    private ImageView showImage;
    private Uri mImageUri = Uri.EMPTY;
    private EditText comentario;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;
    private VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar);
        showToolbar("Publicar", true);

        image_URL       =  getIntent().getStringExtra("Image_url");
        name            =  getIntent().getStringExtra("nombre");
        photoURL        = (ImageView) findViewById(R.id.photoURL);
        nombre          = (TextView) findViewById(R.id.nombre);
        chooseFotoVideo = (CardView) findViewById(R.id.AgregarFoto);
        showImage       = (ImageView) findViewById(R.id.showImageView);
        comentario      = (EditText) findViewById(R.id.comentario);
        videoView       = (VideoView) findViewById(R.id.videoView);

        mProgress       = new ProgressDialog(this);

        mStorage        = FirebaseStorage.getInstance().getReference();
        mDatabase       = FirebaseDatabase.getInstance().getReference().child("Blog");

        nombre.setText(name);

        Glide.with(publicar.this)
                .load(image_URL)
                .crossFade()
                .thumbnail(0.5f)
                .placeholder(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoURL);


        chooseFotoVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(publicar.this);
                builder.setTitle("Elegir Fuente");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Photo")) {
                            if (checkPermissions()) {
                                callgalaryFoto();
                            }
                        }
                        if(options[item].equals("Video")){
                            if (ContextCompat.checkSelfPermission(publicar.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(publicar.this,
                                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                            }
                            else {
                                callgalaryVideo();
                            }
                        }
                    }
                });
                builder.show();

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            Log.d("LOGGED", "imgFile : " + mImageUri);
            showImage.setImageURI(mImageUri);
            videoView.setVisibility(View.INVISIBLE);


        }else if (requestCode == 5 && resultCode == RESULT_OK ) {

        File imgFile = new  File(pictureImagePath);

        if(imgFile.exists()) {
            Log.d("LOGGED", "imgFile : " + imgFile);

            Uri fileUri =Uri.fromFile(imgFile);
            Log.d("LOGGED", "fileUri : " + fileUri);

            showImage.setImageURI(fileUri);
            videoView.setVisibility(View.INVISIBLE);

        }
    }


    if(requestCode == REQUEST_TAKE_GALLERY_VIDEO && resultCode == RESULT_OK){
        videoView.setVisibility(View.VISIBLE);

        mImageUri = data.getData();

        String path = data.getData().toString();
        videoView.setVideoPath(path);
        videoView.start();

        showImage.setVisibility(View.INVISIBLE);

    }

}//end onActivityResult


    private void showToolbar( String title, boolean upbutton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sharedtoolbar_back);
        setSupportActionBar(toolbar);//Crea la compatibilidad con versiones anteriores a lolipop
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upbutton);

    }// Fin showToolbar


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.check,menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar PayPalItem clicks here.
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent goBack = new Intent(publicar.this,Chat.class);
            startActivity(goBack);
            finish();
            return true;
        }

        if(id == R.id.publish){
            startPosting();
        }


        return super.onOptionsItemSelected(item);
    }//end onOptionsItemSelected

    private void startPosting() {

        SimpleDateFormat sdf        = new SimpleDateFormat("dd-MMM-yyyy H:mm");
        final String currentDateandTime   = sdf.format(new Date());


        mProgress.setMessage("Publicando...");
        mProgress.show();

        final String comentarioStr = comentario.getText().toString().trim();

        if(!TextUtils.isEmpty(comentarioStr)){

            StorageReference filePath  = mStorage.child("Blog_Images").child(mImageUri.getLastPathSegment());

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")

                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabase.push();
                    newPost.child("comentario").setValue(comentarioStr);
                    newPost.child("image").setValue(downloadUri.toString());
                    newPost.child("name").setValue(name);

                    if(image_URL == null){
                        newPost.child("profileNamePhoto").setValue("Null");
                    }else {
                        newPost.child("profileNamePhoto").setValue(image_URL);
                    }

                    newPost.child("horaPublicacion").setValue(currentDateandTime);
                    mProgress.dismiss();
                    new NetworkAsyncTask().execute();
                    startActivity(new Intent(publicar.this,Chat.class));
                    finish();
                }
            });

        }

    }//end startPosting

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


    private void callgalaryFoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST);
    }//end callgalaryFoto

    private void callgalaryVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccionar Video"),REQUEST_TAKE_GALLERY_VIDEO);
    }//end callgalaryVideo

    private class NetworkAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            //send Push Notification
            HttpsURLConnection connection = null;
            try {


                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                connection = (HttpsURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                //Put below you FCM API Key instead
                connection.setRequestProperty("Authorization", "key=" + "AAAAC46AZt0:APA91bGK0Dl40lADv53wcVDEzWrmTyQIUTC91US5WkqHH-10NkH--gsGcpqoe_cXrSCjaIwGc1gr5dp9H-eLfTVYLT17GCYb59GYLHoH9ufOUokVYbt795pVcNeXAnhEXqgYCtI16sqp");

                JSONObject json = new JSONObject();
                json.put("to","/topics/news");
                JSONObject info = new JSONObject();
                info.put("message", name + " acabo de publicar algo!"); // Notification body
                json.put("data", info);


                OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
                os.write(json.toString());
                os.flush();
                os.close();
                connection.getInputStream(); //do not remove this line. request will not work without it gg

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) connection.disconnect();
            }


            return null;
        }
    }//end NetworkAsyncTask



}
