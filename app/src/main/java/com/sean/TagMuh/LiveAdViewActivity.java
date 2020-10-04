package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class LiveAdViewActivity extends AppCompatActivity {

    ImageView im;
    TextView tvDes,tvTitle;
    String adKey;

    private FirebaseUser mFirebaseUser;
    Toolbar toolbar;
    String servicerId;
    private DatabaseReference mDatabaseReference;
    ImageButton imBack;
    TextView tvTitleToolbar;
    TextView tvRatingCount;
    Button btnAddContact;
    String uuid,type;
    ImageView imForward,imBackward;
    int count = 2;
    TextView tvRating;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_ad_view);




        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        if(getIntent()!=null){
            adKey = getIntent().getStringExtra("adId");

        }
        im = (ImageView) findViewById(R.id.im);
        tvDes = (TextView) findViewById(R.id.tvDes);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        imBack = (ImageButton) findViewById(R.id.imBack);
        tvTitleToolbar = (TextView) findViewById(R.id.tvTitleToolbar);
        btnAddContact = (Button) findViewById(R.id.btnAddContact);
        imForward = (ImageView) findViewById(R.id.imForward);
        imBackward = (ImageView) findViewById(R.id.imBackward);
        tvRatingCount = (TextView) findViewById(R.id.tvRatingCount);
        tvRating = (TextView) findViewById(R.id.tvRating);
//        android:padding="@dimen/_20sdp"










        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Servicer");
        mDatabaseReference.child("Ads").child(adKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    final Ads a = dataSnapshot.getValue(Ads.class);

                    tvTitle.setText(a.getAdTitle());
                    tvTitleToolbar.setText(a.getAdTitle());
                    tvDes.setText(a.getAdDescription());

                    Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                    servicerId = a.getServicerId();
                    Log.e("test67444 ", servicerId);


                    Query query2 = FirebaseDatabase.getInstance().getReference().child("Ratings").orderByChild("userId").startAt(servicerId).endAt(servicerId+ "\uf8ff");
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){

                                int sum = 0;
                                int count = 0;
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {


                                    String c = postSnapshot.child("rate").getValue().toString();
                                    Log.e("test452 ", String.valueOf(c));

                                    sum = sum + Integer.parseInt(c);
                                    //count = count + 1;

                                }

                                //Log.e("test45",sum+" asdas"+count);

                                int s = (int)dataSnapshot.getChildrenCount();

                                double d = sum / s;
                                //Log.e("test45", String.valueOf(sum));

                                tvRating.setText(String.valueOf(d));

                                tvRatingCount.setText("("+String.valueOf(s)+")");

                            }else{



                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    imForward.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            i++;

                            if(i==3) // switch to 11 because you got 10 images
                            {
                                i=0; // switch to 10, same reason
                            }

                            switch(i)
                            {

                                case 0:
                                    Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;

                                case 1:
                                    Picasso.get().load(a.getAdImage2()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;
                                case 2:
                                    Picasso.get().load(a.getAdImage3()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);

                                    break;
                            }

//                            if(count>3 || count==0){
//
//                                //count = 1;
////                                Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
////                                count = count+1;
//
//                            }else{
//
//                                if (count==1){
//
//                                    Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
//                                    count = count+1;
//
//                                }else if (count==2){
//
//                                    Picasso.get().load(a.getAdImage2()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
//                                    count = count+1;
//
//                                }else if (count==3){
//
//                                    Picasso.get().load(a.getAdImage3()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
//                                    count = count+1;
//
//                                }
//
//                            }

                            Log.e("countF",String.valueOf(i));




                        }
                    });
                    imBackward.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            i--;

                            if(i==-1)
                            {
                                i=0; // you can leave it this way or improve it later
                            }


                            switch(i)
                            {

                                case 0:
                                    Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;

                                case 1:
                                    Picasso.get().load(a.getAdImage2()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;
                                case 2:
                                    Picasso.get().load(a.getAdImage3()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);

                                    break;
                            }

//                            if(count ==4){
//                                count = 2;
//                                Picasso.get().load(a.getAdImage2()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
//
//                            }else
//
//                            if(count==0){
//
//                                count = 1;
//                                Picasso.get().load(a.getAdImage2()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
//                                //count = count+1;
//                            }else{
//
//                                if (count==1){
//
//                                    Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
//                                    count = count-1;
//                                    if(count==0){
//
//                                        count = 1;
//
//                                    }
//
//                                }else if (count==2){
//
//                                    Picasso.get().load(a.getAdImage2()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
//                                    count = count-1;
//                                    if(count==0){
//
//                                        count = 1;
//
//                                    }
//
//                                }else if (count==3){
//
//                                    Picasso.get().load(a.getAdImage3()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
//                                    count = count-1;
//                                    if(count==0){
//
//                                        count = 1;
//
//                                    }
//
//                                }

                            //  }

                            Log.e("countb",String.valueOf(i));


                        }
                    });



                    //Query query = FirebaseDatabase.getInstance().getReference().child("Contacts").orderByChild("servicerId").startAt(servicerId).endAt(servicerId+ "\uf8ff");


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });











        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });






    }
}