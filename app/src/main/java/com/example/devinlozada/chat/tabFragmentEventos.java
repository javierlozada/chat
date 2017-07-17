package com.example.devinlozada.chat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import android.widget.ListView;
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

import java.util.HashMap;
import java.util.Map;

public class tabFragmentEventos extends Fragment{

    private CardView card_viewCompartir;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference myRef,myRef2;
    private ImageView profile_Photo;
    private String photoURL,nombre;
    private RecyclerView mList;
    private DatabaseReference mDatabase;
    private LinearLayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter<news_feed,listFeedItem> firebaseRecyclerAdapter;



    @Override
    public void onStart(){
        super.onStart();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<news_feed, listFeedItem>(
                news_feed.class,R.layout.item_list,listFeedItem.class,mDatabase
        ) {
            @Override
            protected void populateViewHolder(listFeedItem viewHolder, news_feed model, int position) {
                viewHolder.setDescription(model.getComentario());
                viewHolder.setNombre(model.getName());
                viewHolder.setProfileImage(getActivity(),model.getProfileNamePhoto());
                viewHolder.setImage(getActivity(),model.getImage());
                viewHolder.sethoraPublicacion(model.getHoraPublicacion());

            }
        };


        mList.setHasFixedSize(false);
        mList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();

    }



    public static class listFeedItem extends RecyclerView.ViewHolder{

        View mView;

        public listFeedItem(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDescription(String description){
            TextView post_description = (TextView) mView.findViewById(R.id.description);
            post_description.setText(description);

        }

        public void setNombre(String name){
            TextView postName = (TextView) mView.findViewById(R.id.nombre);
            postName.setText(name);
        }

        public void setProfileImage(Context ctx, String image){
            ImageView profileImage = (ImageView) mView.findViewById(R.id.profilePhoto);
            Glide.with(ctx)
                    .load(image)
                    .crossFade()
                    .thumbnail(0.5f)
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profileImage);
        }

        public void setImage(Context ctx, String image){
            ImageView Image = (ImageView) mView.findViewById(R.id.photoImage);

            if(image !=null){
                Glide.with(ctx)
                        .load(image)
                        .crossFade()
                        .thumbnail(0.5f)
                        .placeholder(R.mipmap.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(Image);
            }



        }

        public void sethoraPublicacion(String horaPublicacion){
            TextView horaPublicaciontext = (TextView) mView.findViewById(R.id.horaPublicacion);
            horaPublicaciontext.setText(horaPublicacion);
        }


    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.eventos,container,false);

        card_viewCompartir  = (CardView) v.findViewById(R.id.card_viewCompartir);
        profile_Photo       = (ImageView) v.findViewById(R.id.profilePhoto);
        mList               = (RecyclerView) v.findViewById(R.id.show_eventos_recyclerView);
        mList.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mList.setLayoutManager(mLayoutManager);


        mDatabase           = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabase.keepSynced(true);



        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if(auth != null){
            user = auth.getCurrentUser();
            String email        = user.getEmail();
            String USER_ID      = email.replace("@","").replace(".","");
            myRef  = FirebaseDatabase.getInstance().getReference().child("android").child(USER_ID).child("image_Url");
            myRef2 = FirebaseDatabase.getInstance().getReference().child("android").child(USER_ID).child("Name");

        }


        card_viewCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent publicar =  new Intent(getActivity(), publicar.class);
                publicar.putExtra("Image_url",photoURL );
                publicar.putExtra("nombre",nombre);
                startActivity(publicar);
            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 photoURL = (String) dataSnapshot.getValue();


                if(!myRef.equals("Null")){
                    Glide.with(getActivity())
                            .load(photoURL)
                            .crossFade()
                            .thumbnail(0.5f)
                            .placeholder(R.drawable.loading)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(profile_Photo);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombre = (String) dataSnapshot.getValue();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        return v;
    }
}
