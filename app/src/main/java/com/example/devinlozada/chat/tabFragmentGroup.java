package com.example.devinlozada.chat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



public class tabFragmentGroup extends Fragment{


    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    public FirebaseRecyclerAdapter<Show_Chat_Activity_Data_Items, Show_Chat_ViewHolder> mFirebaseAdapter;
    ProgressBar progressBar;
    LinearLayoutManager mLinearLayoutManager;

    private SharedPreferences prefs;
    private String getEmail;
    private FirebaseAuth auth;
    private String email;
    private FirebaseUser user;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contacts,container,false);

        //groups   = (ListView) v.findViewById(R.id.messagesContainer);
        //addGroup = (FloatingActionButton) v.findViewById(R.id.addGroup);

         /* iniciating getSharedPreferences to put Strings and to Edit*/
        prefs    = getActivity().getSharedPreferences("Prefs", getActivity().MODE_PRIVATE);

        getEmail = prefs.getString("Email",null);

        firebaseDatabase = FirebaseDatabase.getInstance();

        myRef = firebaseDatabase.getReference("android");
        myRef.keepSynced(true);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if(auth != null){
            user   = auth.getCurrentUser();
            email = user.getEmail();
        }


        progressBar = (ProgressBar) v.findViewById(R.id.show_chat_progressBar2);

        //Recycler View
        recyclerView = (RecyclerView)v.findViewById(R.id.show_chat_recyclerView);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        //mLinearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLinearLayoutManager);

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(ProgressBar.VISIBLE);
        //Log.d("LOGGED", "Will Start Calling populateViewHolder : ");
        //Log.d("LOGGED", "IN onStart ");


        mFirebaseAdapter = new FirebaseRecyclerAdapter<Show_Chat_Activity_Data_Items, Show_Chat_ViewHolder>(Show_Chat_Activity_Data_Items.class, R.layout.show_chat_single_item, Show_Chat_ViewHolder.class, myRef) {

            @Override
            protected void populateViewHolder(Show_Chat_ViewHolder viewHolder, Show_Chat_Activity_Data_Items model, final int position) {
                //Log.d("LOGGED", "populateViewHolder Called: ");
                progressBar.setVisibility(ProgressBar.INVISIBLE);


                if (!model.getName().equals("Null")) {


                    viewHolder.Person_Name(model.getName());
                    viewHolder.Person_Image(model.getImage_Url());
                    //viewHolder.Person_Email(model.getEmail());

                    if(model.getEmail().equals(email)){

                        viewHolder.Layout_hide();
                        viewHolder.person_email.setVisibility(View.GONE);
                        viewHolder.isOnline.setVisibility(View.GONE);
                        viewHolder.person_name.setVisibility(View.GONE);


                    } else{
                        viewHolder.Person_Email(model.getEmail());

                        //OnClick Item
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(final View v) {

                                DatabaseReference ref = mFirebaseAdapter.getRef(position);

                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        String retrieve_name        = dataSnapshot.child("Name").getValue(String.class);
                                        String retrieve_Email       = dataSnapshot.child("Email").getValue(String.class);
                                        String retrieve_url         = dataSnapshot.child("image_Url").getValue(String.class);

                                        Intent intent = new Intent(getContext(), ChatConversationActivity.class);
                                        intent.putExtra("image_id", retrieve_url);
                                        intent.putExtra("email", retrieve_Email);
                                        intent.putExtra("name", retrieve_name);
                                        startActivity(intent);


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {/**/}
                                });
                            }
                        });


                    }
                }
            }
        };

        recyclerView.setAdapter(mFirebaseAdapter);

    }

    //View Holder For Recycler View
    public static class Show_Chat_ViewHolder extends RecyclerView.ViewHolder {
        private final TextView person_name, person_email;
        private final ImageView person_image;
        private final ImageView isOnline;
        private final LinearLayout layout;
        final LinearLayout.LayoutParams params;


        public Show_Chat_ViewHolder(final View itemView) {
            super(itemView);
            person_name     = (TextView) itemView.findViewById(R.id.chat_persion_name);
            person_email    = (TextView) itemView.findViewById(R.id.chat_persion_email);
            person_image    = (ImageView) itemView.findViewById(R.id.chat_persion_image);
            layout          = (LinearLayout)itemView.findViewById(R.id.show_chat_single_item_layout);
            isOnline        = (ImageView) itemView.findViewById(R.id.isOnline);
            params          = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }


        private void Person_Name(String title) {
            // Log.d("LOGGED", "Setting Name: ");
            person_name.setText(title);
        }
        private void Layout_hide() {
            params.height = 0;
            //itemView.setLayoutParams(params);
            layout.setLayoutParams(params);

        }

        private void Person_Email(String title) {
            person_email.setText(title);
        }

        private void isOnlineView(String isOnlineString){
            if(isOnlineString.equals("true")){
                isOnline.setVisibility(View.VISIBLE);
            }else {
                isOnline.setVisibility(View.GONE);
            }

        }

        private void Person_Image(String url) {

            if (!url.equals("Null")) {
                Glide.with(itemView.getContext())
                        .load(url)
                        .crossFade()
                        .thumbnail(0.5f)
                        .placeholder(R.drawable.loading)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(person_image);
            }

        }
    }


}
