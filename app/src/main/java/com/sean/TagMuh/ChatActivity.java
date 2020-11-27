 package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.Date;
import java.util.UUID;

 public class ChatActivity extends AppCompatActivity {





     Toolbar toolbar;
     private StorageReference mStorageReference;

     private FirebaseUser mFirebaseUser;

     private DatabaseReference databaseReference;
     String uuid,type;

     String servicerId;

     TextView tv1;


     FirebaseRecyclerAdapter adapter;
     LinearLayoutManager linearLayoutManager;

     RecyclerView recyclerView;
     ImageView imLocation;

     ImageButton imback;
     ImageView send;
     EditText etText;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_chat);

         if(getIntent()!=null){
             servicerId = getIntent().getStringExtra("servicerId");

         }

         SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
         uuid = (shared.getString("UUID", ""));
         type = (shared.getString("type", ""));




         toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

         etText = (EditText) findViewById(R.id.etText);

         tv1 = (TextView) findViewById(R.id.tv1);
         imLocation =(ImageView) findViewById(R.id.imLocation);

         send =(ImageView) findViewById(R.id.send);

         recyclerView =(RecyclerView) findViewById(R.id.recyclerView);
         linearLayoutManager = new LinearLayoutManager(this);
         recyclerView.setLayoutManager(linearLayoutManager);
         recyclerView.setHasFixedSize(true);
         
         
         
         mStorageReference = FirebaseStorage.getInstance().getReference();



         databaseReference = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Users").child(servicerId);

         databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                 if(dataSnapshot.exists()){

                     String a = dataSnapshot.child("firstName").getValue().toString();

                     String b = dataSnapshot.child("lastName").getValue().toString();

                     tv1.setText(a+" "+b);




                 }


             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });















        //.orderByChild("customerId").startAt(uuid).endAt(uuid+ "\uf8ff");

         Query query = FirebaseDatabase.getInstance().getReference().child("Messages");
         fetch(query);




         imLocation.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 Intent intent = new Intent(ChatActivity.this,MapsActivity.class);

                 startActivity(intent);

             }
         });



         imback =(ImageButton) findViewById(R.id.imBack);
         imback.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 Intent intent = new Intent(ChatActivity.this,ChatListActivity.class);

                 startActivity(intent);             }
         });









        final DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("Messages");

         send.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 final String uuid1 = UUID.randomUUID().toString().toUpperCase();

                 String a = etText.getText().toString();
                 etText.setText("");

                 d.child(uuid1).child("receiverId").setValue(servicerId);
                 d.child(uuid1).child("senderId").setValue(uuid);
                 d.child(uuid1).child("sentTime").setValue((int) (new Date().getTime()/1000));
                 d.child(uuid1).child("textMsg").setValue(a);


             }
         });

     }


     public class ChatViewHolder extends RecyclerView.ViewHolder {
         public TextView tv11;
         public TextView tv22;

         public ChatViewHolder(View itemView) {
             super(itemView);
             tv11 = itemView.findViewById(R.id.tv11);
             tv22 = itemView.findViewById(R.id.tv22);


         }


     }


     private void fetch(Query query) {




         FirebaseRecyclerOptions<Chat> options = new FirebaseRecyclerOptions.Builder<Chat>().setQuery(query, Chat.class).build();



         adapter = new FirebaseRecyclerAdapter<Chat, ChatViewHolder>(options) {
             @Override
             public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                 View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_view_1, parent, false);

                 return new ChatViewHolder(view);
             }



             @Override
             protected void onBindViewHolder(final ChatViewHolder holder, final int position, final Chat model) {
                 // holder.setTxtTitle(model.getAdsId());
                 //holder.setTxtDesc(model.getCustomerId());

                 //recyclerView.setRightBg(R.color.red);


                 if((model.getSenderId().equals(uuid) && model.getReceiverId().equals(servicerId)) || (model.getSenderId().equals(servicerId) && model.getReceiverId().equals(uuid))){


                     holder.tv11.setVisibility(View.VISIBLE);

                     holder.tv22.setVisibility(View.VISIBLE);


                     if(model.getSenderId().equals(uuid) && model.getReceiverId().equals(servicerId)){



                         holder.tv11.setVisibility(View.VISIBLE);

                         holder.tv11.setText(model.getTextMsg());
                         //holder.tv12.setText(model.getTextMsg());
                         holder.tv22.setVisibility(View.GONE);


                     }else if(model.getSenderId().equals(servicerId) && model.getReceiverId().equals(uuid)){

                         holder.tv22.setVisibility(View.VISIBLE);

                         holder.tv22.setText(model.getTextMsg());
                         holder.tv11.setVisibility(View.GONE);


                     }





                 }else{
                     holder.tv11.setVisibility(View.GONE);

                     holder.tv22.setVisibility(View.GONE);

                 }










                 Log.e("asd2 ",String.valueOf(position));








             }

         };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);

         recyclerView.setAdapter(adapter);
         adapter.startListening();
     }



     @Override
     protected void onStart() {
         super.onStart();
         adapter.startListening();
     }

     @Override
     protected void onStop() {
         super.onStop();
         adapter.stopListening();
     }



 }