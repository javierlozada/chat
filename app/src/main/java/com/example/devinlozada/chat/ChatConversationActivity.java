package com.example.devinlozada.chat;

/**
 * Created by Administrator on 17-05-2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.RunnableFuture;

import javax.net.ssl.HttpsURLConnection;


public class ChatConversationActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef,myRef2,myRef3,myRef4,myRef5;
    private FirebaseRecyclerAdapter<Show_Chat_Conversation_Data_Items, Chat_Conversation_ViewHolder> mFirebaseAdapter;
    public LinearLayoutManager mLinearLayoutManager;
    static String Sender_Name;
    String sender,receiver;
    String messageText;
    ImageView isOnline;
    ImageView profilePhoto;
    LinearLayout actionBar;
    TextView estaEsribiendo;
    Handler handler;
    Runnable runnable;


    ImageView attach_icon,send_icon,no_data_available_image;
    EditText message_area;
    TextView no_chat;
    private FirebaseAuth auth;
    String email;

    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    ProgressBar progressBar;
    public static final int READ_EXTERNAL_STORAGE = 0,MULTIPLE_PERMISSIONS = 10;
    Uri mImageUri = Uri.EMPTY;

    TextView nombre;

    private String pictureImagePath = "";
    final CharSequence[] options = {"Camara", "Galeria"};
    String[] permissions= new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,};


    FirebaseUser user;

    Timer timer;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_chat_conversation_layout);

        showToolbar("",true);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if(auth != null){
            user   = auth.getCurrentUser();
            email = user.getEmail();
        }

        String USER_ID   = email.replace("@","").replace(".","");
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference().child("Chat").child(USER_ID).child(getIntent().getStringExtra("email").replace("@","").replace(".",""));
        myRef.keepSynced(true);
        Log.d("LOGGED", "myRef : " + myRef);

        myRef3 = firebaseDatabase.getReference().child("android").child(USER_ID).child("FirebaseToken");
        myRef4 = firebaseDatabase.getReference().child("android").child(getIntent().getStringExtra("email").replace("@","").replace(".","")).child("FirebaseToken");

        myRef2 = firebaseDatabase.getReference().child("Chat").child(getIntent().getStringExtra("email").replace("@","").replace(".","")).child(USER_ID);
        myRef2.keepSynced(true);
        Log.d("LOGGED", "myRef2 : " + myRef2);

        myRef3                    = firebaseDatabase.getReference("android").child(getIntent().getStringExtra("email").replace("@","").replace(".","")).child("isOnline").child("enLinea");
        myRef3.keepSynced(true);
        Log.d("LOGGED", "myRef3 : " + myRef3);

        myRef5                    = FirebaseDatabase.getInstance().getReference("isOnline").child("enLinea").child(getIntent().getStringExtra("email").replace("@","").replace(".",""));
        myRef5.keepSynced(true);


        Sender_Name             = getIntent().getStringExtra("name");
        recyclerView            = (RecyclerView)findViewById(R.id.fragment_chat_recycler_view);
        attach_icon             = (ImageView)findViewById(R.id.attachButton);
        send_icon               = (ImageView)findViewById(R.id.sendButton);
        no_data_available_image = (ImageView)findViewById(R.id.no_data_available_image);
        message_area            = (EditText)findViewById(R.id.messageArea);
        mProgressDialog         = new ProgressDialog(this);
        progressBar             = (ProgressBar)findViewById(R.id.progressBar3);
        no_chat                 = (TextView)findViewById(R.id.no_chat_text);
        mLinearLayoutManager    = new LinearLayoutManager(ChatConversationActivity.this);
        isOnline                = (ImageView) findViewById(R.id.isOnline);
        nombre                  = (TextView) findViewById(R.id.name);
        profilePhoto            = (ImageView) findViewById(R.id.profilePhoto);
        final String image_URL  = getIntent().getStringExtra("image_id");
        actionBar               = (LinearLayout) findViewById(R.id.actionBar);
        estaEsribiendo          = (TextView) findViewById(R.id.estaEsribiendo);


        nombre.setText(Html.fromHtml("<font color=#FFFFFF>" + Sender_Name + "</font>"));

       if(image_URL != null){
           if(!image_URL.equals("Null")){
               Glide.with(ChatConversationActivity.this)
                       .load(image_URL)
                       .crossFade()
                       .thumbnail(0.5f)
                       .placeholder(R.drawable.loading)
                       .diskCacheStrategy(DiskCacheStrategy.ALL)
                       .into(profilePhoto);
           }
       }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        mLinearLayoutManager.setStackFromEnd(true);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        send_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                 messageText = message_area.getText().toString().trim();

                if(!messageText.equals("")){
                    new NetworkAsyncTask().execute();
                    ArrayMap<String, String> map = new ArrayMap<>();

                    map.put("message", messageText);
                    map.put("sender", email);
                    myRef.push().setValue(map);
                    myRef2.push().setValue(map);
                    message_area.setText("");
                    recyclerView.postDelayed(new Runnable() {
                        @Override public void run()
                        {
                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);

                        }
                    }, 500);
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        message_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String UserID = user.getEmail().replace("@", "").replace(".", "");
                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("isOnline").child("enLinea");
                DatabaseReference ref1 = mRootRef.child(UserID);
                ref1.child("Escribiendo").setValue("true");



            }

            @Override
            public void afterTextChanged(Editable s) {

                 runnable = new Runnable() {
                    @Override
                    public void run() {

                        String UserID = user.getEmail().replace("@", "").replace(".", "");
                        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("isOnline").child("enLinea");
                        DatabaseReference ref1 = mRootRef.child(UserID);
                        ref1.child("Escribiendo").setValue("false");

                    }
                };

                handler = new android.os.Handler();
                handler.postDelayed(runnable, 2000);
            }
        });


        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goProfile = new Intent(ChatConversationActivity.this, profile_sender.class);
                goProfile.putExtra("name",Sender_Name);
                goProfile.putExtra("email_sender", getIntent().getStringExtra("email"));
                goProfile.putExtra("Profile_photo", image_URL);
                startActivity(goProfile);

            }
        });


        myRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 sender = (String) dataSnapshot.getValue();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        myRef4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 receiver = (String) dataSnapshot.getValue();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myRef5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String enLinea           = dataSnapshot.child("isOnline").getValue(String.class);
                String estaEsribiendoStr = dataSnapshot.child("Escribiendo").getValue(String.class);

                    if(enLinea.equals("true")){
                        isOnline.setVisibility(View.VISIBLE);
                    }else{
                        isOnline.setVisibility(View.INVISIBLE);

                    }

                    if(estaEsribiendoStr != null){
                        if(estaEsribiendoStr.equals("true")){
                            estaEsribiendo.setVisibility(View.VISIBLE);
                        }else {
                            estaEsribiendo.setVisibility(View.INVISIBLE);
                        }
                    }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        attach_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatConversationActivity.this);
                builder.setTitle("Elegir Fuente ");
                builder.setItems(options, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Camara"))
                        {
                            if (checkPermissions())
                            {
                                callCamera();
                            }
                        }
                        if(options[item].equals("Galeria"))
                        {
                            if (ContextCompat.checkSelfPermission(ChatConversationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                            {
                                ActivityCompat.requestPermissions(ChatConversationActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                            }
                            else
                            {
                                callgalary();
                            }
                        }
                    }
                });
                builder.show();
            }
        });
    }


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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    callgalary();
                return;

            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    callCamera();
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();


        String UserID               = user.getEmail().replace("@","").replace(".","");
        DatabaseReference mRootRef  = FirebaseDatabase.getInstance().getReference("isOnline").child("enLinea");

        DatabaseReference ref1      = mRootRef.child(UserID);
        ref1.child("isOnline").setValue("true");
        ref1.child("Escribiendo").setValue("false");




        // Log.d("LOGGED", "On Start : " );
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Show_Chat_Conversation_Data_Items, Chat_Conversation_ViewHolder>
                (Show_Chat_Conversation_Data_Items.class,
                        R.layout.show_chat_conversation_single_item,
                        Chat_Conversation_ViewHolder.class,
                        myRef) {

            @Override
            protected void populateViewHolder(Chat_Conversation_ViewHolder viewHolder, Show_Chat_Conversation_Data_Items model, final int position) {

                viewHolder.getSender(model.getSender(), email);
                viewHolder.getMessage(model.getMessage());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {

                        DatabaseReference ref = mFirebaseAdapter.getRef(position);
                        ref.keepSynced(true);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String retrieve_image_url = dataSnapshot.child("message").getValue(String.class);

                                if(retrieve_image_url.startsWith("https"))
                                {
                                    //Toast.makeText(ChatConversationActivity.this, "URL : " + retrieve_image_url, Toast.LENGTH_SHORT).show();
                                    Intent intent = (new Intent(ChatConversationActivity.this,EnlargeImageView.class));
                                    intent.putExtra("url",retrieve_image_url);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        };
        Log.d("LOGGED", "Set Layout : " );
        recyclerView.setAdapter(mFirebaseAdapter);


        myRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    //Log.d("LOGGED", "Data SnapShot : " +dataSnapshot.toString());
                    progressBar.setVisibility(ProgressBar.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    no_data_available_image.setVisibility(View.GONE);
                    no_chat.setVisibility(View.GONE);
                    recyclerView.postDelayed(new Runnable() {
                        @Override public void run()
                        {
                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);

                        }
                    }, 500);

                    recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v,
                                                   int left, int top, int right, int bottom,
                                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            System.out.println("entro" + bottom + " : " + oldBottom);

                            if (bottom < oldBottom) {
                                System.out.println("entro" + bottom + " : " + oldBottom);
                                recyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                                    }
                                }, 100);
                            }
                        }
                    });
                }
                else {
                    //Log.d("LOGGED", "NO Data SnapShot : " +dataSnapshot.toString());
                    progressBar.setVisibility(ProgressBar.GONE);
                    recyclerView.setVisibility(View.GONE);
                    no_data_available_image.setVisibility(View.VISIBLE);
                    no_chat.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goback = new Intent(ChatConversationActivity.this,Chat.class);
        startActivity(goback);
        finish();
    }

    //View Holder For Recycler View
    public static class Chat_Conversation_ViewHolder extends RecyclerView.ViewHolder {
        private final TextView message, sender;
        private final ImageView chat_image_incoming,chat_image_outgoing;
        View mView;
        final LinearLayout.LayoutParams params,text_params;
        LinearLayout layout;


        public Chat_Conversation_ViewHolder(final View itemView) {
            super(itemView);
            //Log.d("LOGGED", "ON Chat_Conversation_ViewHolder : " );
            mView               = itemView;
            message             = (TextView) mView.findViewById(R.id.fetch_chat_messgae);
            sender              = (TextView) mView.findViewById(R.id.fetch_chat_sender);
            chat_image_incoming = (ImageView) mView.findViewById(R.id.chat_uploaded_image_incoming);
            chat_image_outgoing = (ImageView) mView.findViewById(R.id.chat_uploaded_image_outgoing);

            params              = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            text_params         = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout              = (LinearLayout) mView.findViewById(R.id.chat_linear_layout);
        }

        private void getSender(String title,String email) {
            if(title.equals(email))
            {
                //Log.d("LOGGED", "getSender: ");
                params.setMargins((Login.Device_Width/3),5,10,10);
                text_params.setMargins(15,10,0,5);
                sender.setLayoutParams(text_params);
                mView.setLayoutParams(params);
                mView.setBackgroundResource(R.drawable.shape_outcoming_message);
                sender.setText("TU");
                chat_image_outgoing.setVisibility(View.VISIBLE);
                chat_image_incoming.setVisibility(View.GONE);

            }
            else
            {
                params.setMargins(10,0,(Login.Device_Width/3),10);
                sender.setGravity(Gravity.START);
                text_params.setMargins(60,10,0,5);
                sender.setLayoutParams(text_params);
                mView.setLayoutParams(params);
                mView.setBackgroundResource(R.drawable.shape_incoming_message);
                sender.setText(Sender_Name);
                chat_image_outgoing.setVisibility(View.GONE);
                chat_image_incoming.setVisibility(View.VISIBLE);
            }
        }

        private void getMessage(String title) {

            if(!title.startsWith("https"))
            {

                if(!sender.getText().equals(Sender_Name))
                {
                    text_params.setMargins(15,10,22,15);
                }
                else
                {
                    text_params.setMargins(65,10,22,15);
                }

                message.setLayoutParams(text_params);
                message.setText(title);
                message.setTextColor(Color.parseColor("#FFFFFF"));
                message.setVisibility(View.VISIBLE);
                chat_image_incoming.setVisibility(View.GONE);
                chat_image_outgoing.setVisibility(View.GONE);
            }
            else
            {
                if (chat_image_outgoing.getVisibility()==View.VISIBLE && chat_image_incoming.getVisibility()==View.GONE)
                {
                    chat_image_outgoing.setVisibility(View.VISIBLE);
                    message.setVisibility(View.GONE);
                    Glide.with(itemView.getContext())
                            .load(title)
                            .crossFade()
                            .fitCenter()
                            .placeholder(R.drawable.loading)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(chat_image_outgoing);
                }
                else
                {
                    chat_image_incoming.setVisibility(View.VISIBLE);
                    message.setVisibility(View.GONE);
                    Glide.with(itemView.getContext())
                            .load(title)
                            .crossFade()
                            .fitCenter()
                            .placeholder(R.drawable.loading)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(chat_image_incoming);
                }
            }

        }

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


            mProgressDialog.setMessage("Cargando...");
            mProgressDialog.show();

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();

                    ArrayMap<String, String> map = new ArrayMap<>();
                    map.put("message", downloadUri.toString());
                    map.put("sender", email);
                    myRef.push().setValue(map);
                    myRef2.push().setValue(map);
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

                mProgressDialog.setMessage("Uploading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                filePath.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();
                        ArrayMap<String, String> map = new ArrayMap<>();
                        map.put("message", downloadUri.toString());
                        map.put("sender", email);
                        myRef.push().setValue(map);
                        myRef2.push().setValue(map);

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class NetworkAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            String name = getIntent().getStringExtra("name");

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

                /*JSONObject root = new JSONObject();
                JSONObject data = new JSONObject();
                data.put(KEY_FCM_TEXT, messageText);
                data.put(KEY_FCM_SENDER_ID, sender);
                root.put("data", data);
                root.put("to", receiver);
                */

                JSONObject json = new JSONObject();
                json.put("to",receiver.trim());
                JSONObject info = new JSONObject();
                info.put("title", "Causa y Efecto"); // Notification title
                info.put("body", name + " : " + messageText); // Notification body
                json.put("notification", info);


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
    }

    private void showToolbar( String title, boolean upbutton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sharedtoolbar_back);
        setSupportActionBar(toolbar);//Crea la compatibilidad con versiones anteriores a lolipop
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upbutton);

    }// Fin showToolbar

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.get(getApplicationContext()).clearMemory();
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
        String UserID = user.getEmail().replace("@", "").replace(".", "");
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("isOnline").child("enLinea");
        DatabaseReference ref1 = mRootRef.child(UserID);
        ref1.child("isOnline").setValue("true");
    }







}