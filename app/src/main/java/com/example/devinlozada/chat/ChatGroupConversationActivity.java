package com.example.devinlozada.chat;

/**
 * Created by Administrator on 17-05-2017.
 */

import android.Manifest;
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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
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


public class ChatGroupConversationActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    DatabaseReference myRef,myRef2;
    private FirebaseRecyclerAdapter<Show_Chat_Conversation_Data_Items, Chat_Conversation_ViewHolder> mFirebaseAdapter;
    public LinearLayoutManager mLinearLayoutManager;
    static String Group_Name;
    String receiver;
    String messageText;
    ImageView isOnline;
    ImageView profilePhoto;
    LinearLayout actionBar;
    static String retrieve_sender;
    static String name;
    static String messsageTextStr;


    ImageView attach_icon,send_icon,no_data_available_image;
    EditText message_area;
    TextView no_chat;
    private FirebaseAuth auth;
    static String email;

    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    private ProgressBar progressBar;
    public static final int READ_EXTERNAL_STORAGE = 0,MULTIPLE_PERMISSIONS = 10;
    private Uri mImageUri = Uri.EMPTY;

    private TextView nombre;

    private String pictureImagePath = "";
    final CharSequence[] options = {"Camara", "Galeria"};
    private String[] permissions= new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,};


    private FirebaseUser user;

    private static int Device_Width;

    String image_URL;




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


        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        Device_Width = metrics.widthPixels;

        Group_Name              = getIntent().getStringExtra("GroupName");

        FirebaseMessaging.getInstance().subscribeToTopic(Group_Name.replace("@","").replace(".","").replaceAll("\\s+","").trim());


        myRef                   = FirebaseDatabase.getInstance().getReference().child("Groups").child(getIntent().getStringExtra("GroupName")).child("GroupChat");
        myRef.keepSynced(true);

        myRef2 = FirebaseDatabase.getInstance().getReference().child("android").child(email.replace("@","").replace(".","")).child("Name");
        myRef2.keepSynced(true);

        recyclerView            = (RecyclerView)findViewById(R.id.fragment_chat_recycler_view);
        attach_icon             = (ImageView)findViewById(R.id.attachButton);
        send_icon               = (ImageView)findViewById(R.id.sendButton);
        no_data_available_image = (ImageView)findViewById(R.id.no_data_available_image);
        message_area            = (EditText)findViewById(R.id.messageArea);
        mProgressDialog         = new ProgressDialog(this);
        progressBar             = (ProgressBar)findViewById(R.id.progressBar3);
        no_chat                 = (TextView)findViewById(R.id.no_chat_text);
        mLinearLayoutManager    = new LinearLayoutManager(ChatGroupConversationActivity.this);
        isOnline                = (ImageView) findViewById(R.id.isOnline);
        nombre                  = (TextView) findViewById(R.id.name);
        profilePhoto            = (ImageView) findViewById(R.id.profilePhoto);
        image_URL               = getIntent().getStringExtra("image_id");
        actionBar               = (LinearLayout) findViewById(R.id.actionBar);


        nombre.setText(Html.fromHtml("<font color=#FFFFFF>" + Group_Name + "</font>"));

       if(image_URL != null){
           if(!image_URL.equals("Null")){
               Glide.with(ChatGroupConversationActivity.this)
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

                    messsageTextStr = messageText;

                if(!messageText.equals("")){
                    //new NetworkAsyncTask().execute();
                    ArrayMap<String, String> map = new ArrayMap<>();

                    map.put("message", messageText);
                    map.put("sender",  email.replace("@","").replace(".",""));
                    map.put("name",   name);

                    myRef.push().setValue(map);
                    message_area.setText("");

                    new NetworkAsyncTask().execute();


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

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent goProfile = new Intent(ChatGroupConversationActivity.this, profile_sender.class);
                goProfile.putExtra("name",Group_Name);
                goProfile.putExtra("email_sender", getIntent().getStringExtra("email"));
                goProfile.putExtra("Profile_photo", image_URL);
                startActivity(goProfile);*/

            }
        });

        myRef2.addValueEventListener(new ValueEventListener() {
                                         @Override
                                         public void onDataChange (DataSnapshot dataSnapshot){
                                             name = (String) dataSnapshot.getValue();


                                         }

                                         @Override
                                         public void onCancelled (DatabaseError databaseError){

                                         }
                                     });




        attach_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatGroupConversationActivity.this);
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
                            if (ContextCompat.checkSelfPermission(ChatGroupConversationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                            {
                                ActivityCompat.requestPermissions(ChatGroupConversationActivity.this,
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

    private void showToolbar(String title, boolean upbutton) {

            Toolbar toolbar = (Toolbar) findViewById(R.id.sharedtoolbar_back);
            setSupportActionBar(toolbar);//Crea la compatibilidad con versiones anteriores a lolipop
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(upbutton);
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

        // Log.d("LOGGED", "On Start : " );
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Show_Chat_Conversation_Data_Items, Chat_Conversation_ViewHolder>
                (Show_Chat_Conversation_Data_Items.class,
                        R.layout.show_chat_conversation_single_item,
                        Chat_Conversation_ViewHolder.class,
                        myRef) {

            @Override
            protected void populateViewHolder(Chat_Conversation_ViewHolder viewHolder, Show_Chat_Conversation_Data_Items model, final int position) {


                String getName  = viewHolder.getName(model.getName());

                viewHolder.getSender(model.getSender(), email.replace("@","").replace(".",""), getName);
                viewHolder.getMessage(model.getMessage(),getName);

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
                                    Intent intent = (new Intent(ChatGroupConversationActivity.this,EnlargeImageView.class));
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


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

                            if (bottom < oldBottom) {
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
                Intent goback = new Intent(ChatGroupConversationActivity.this,Chat.class);
                startActivity(goback);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goback = new Intent(ChatGroupConversationActivity.this,Chat.class);
        startActivity(goback);
        finish();
    }

    //View Holder For Recycler View
    public static class Chat_Conversation_ViewHolder extends RecyclerView.ViewHolder {
        private final TextView message, sender;
        private final ImageView chat_image_incoming,chat_image_outgoing;
        private View mView;
        private final LinearLayout.LayoutParams params,text_params;


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
        }

        private void getSender(String senderStr,String emailStr, String name) {



            if(senderStr.equals(emailStr))
            {
                params.setMargins((Device_Width/3),5,10,10);
                text_params.setMargins(15,10,0,5);
                sender.setLayoutParams(text_params);
                mView.setLayoutParams(params);
                mView.setBackgroundResource(R.drawable.shape_outcoming_message);
                sender.setText("TU");
                chat_image_outgoing.setVisibility(View.VISIBLE);
                chat_image_incoming.setVisibility(View.GONE);

            }else{
                params.setMargins(10,0,(Device_Width/3),10);
                sender.setGravity(Gravity.START);
                text_params.setMargins(60,10,0,5);
                sender.setLayoutParams(text_params);
                mView.setLayoutParams(params);
                mView.setBackgroundResource(R.drawable.shape_incoming_message);
                sender.setText(name);
                chat_image_outgoing.setVisibility(View.GONE);
                chat_image_incoming.setVisibility(View.VISIBLE);
            }
        }


        private String getName(String name){
            return name;
        }


        private void getMessage(String title, String name) {

            if(!title.startsWith("https"))
            {
                if(!sender.getText().equals(name))
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
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("ChatGroup_Images").child(mImageUri.getLastPathSegment());
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
                    map.put("name",name);
                    myRef.push().setValue(map);
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
                        map.put("sender",  email);
                        map.put("name",    name);
                        myRef.push().setValue(map);

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

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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


                json.put("to", "/topics/" + Group_Name.replace("@","").replace(".","").replaceAll("\\s+","").trim());
                JSONObject info = new JSONObject();
                info.put("title", Group_Name);
                info.put("message", name +" : "  + messsageTextStr); // Notification body
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


    }
}