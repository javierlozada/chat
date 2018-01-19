package com.example.devinlozada.chat.tabsHome;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.devinlozada.chat.ChatGroupConversationActivity;
import com.example.devinlozada.chat.R;
import com.example.devinlozada.chat.Show_Chat_Activity_Group_Items;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class tabFragmentGroups extends Fragment{

    private FirebaseAuth auth;
    private FirebaseUser user;
    private RecyclerView mList;
    private DatabaseReference mDatabase;
    private LinearLayoutManager mLayoutManager;
    private com.getbase.floatingactionbutton.FloatingActionButton groupFab;
    private FirebaseRecyclerAdapter<Show_Chat_Activity_Group_Items,tabFragmentGroups.listGroupItem> firebaseRecyclerAdapter;



    @Override
    public void onStart(){
        super.onStart();

         firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Show_Chat_Activity_Group_Items, tabFragmentGroups.listGroupItem>(
                Show_Chat_Activity_Group_Items.class, R.layout.show_chat_group_item,tabFragmentGroups.listGroupItem.class,mDatabase
        ) {
            @Override
            protected void populateViewHolder(listGroupItem viewHolder, Show_Chat_Activity_Group_Items model, final int position) {
                viewHolder.setGroupImage(getActivity(),model.getImageGroup());
                viewHolder.setGroupName(model.getGroupName());


                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {

                        DatabaseReference ref = firebaseRecyclerAdapter.getRef(position);

                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                String retrieveGroup_name        = dataSnapshot.child("GroupName").getValue(String.class);

                                Intent intent = new Intent(getContext(), ChatGroupConversationActivity.class);
                                intent.putExtra("GroupName", retrieveGroup_name);
                                startActivity(intent);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {/**/}
                        });
                    }
                });

            }

        };

        mList.setHasFixedSize(false);
        mList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    public static class listGroupItem extends RecyclerView.ViewHolder{

        View mView;

        public listGroupItem(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setGroupName(String groupName){
            TextView group_name = (TextView) mView.findViewById(R.id.chat_group_name);
            group_name.setText(groupName);

        }
        public void setGroupImage(Context ctx, String image){
            ImageView groupImage = (ImageView) mView.findViewById(R.id.chat_group_image);
            Glide.with(ctx)
                    .load(image)
                    .crossFade()
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.loading)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(groupImage);
        }

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.grupos,container,false);

        mList               = (RecyclerView) v.findViewById(R.id.show_Groupchat_recyclerView);
        mList.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mList.setLayoutManager(mLayoutManager);

        mDatabase           = FirebaseDatabase.getInstance().getReference("Groups");
        mDatabase.keepSynced(true);

        groupFab = (com.getbase.floatingactionbutton.FloatingActionButton) v.findViewById(R.id.groupFab);

        groupFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_addgroup_dialog);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width    = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height   = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity  = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);

                Button buttonCancelar       = (Button) dialog.findViewById(R.id.cancel_action);
                Button agregarGroup         = (Button) dialog.findViewById(R.id.agregar_action);
                final EditText groupText    = (EditText) dialog.findViewById(R.id.addGroupText);

                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                agregarGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String grupoStr = groupText.getText().toString().trim();


                        if (TextUtils.isEmpty(grupoStr)) {
                            groupText.setError("Ingresar nombre de Grupo!");
                            return;
                        }else {

                            DatabaseReference mRootRef  = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference ref1      = mRootRef.child("Groups").child(grupoStr);
                            ref1.child("GroupName").setValue(grupoStr);
                            ref1.child("GroupImage").setValue("Null");
                        }

                        dialog.dismiss();


                    }
                });



                dialog.show();

            }
        });



        return v;
    }


}
