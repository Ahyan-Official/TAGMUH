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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AdViewActivity extends AppCompatActivity {

    ImageView im;
    RoundedImageView imProfile;
    TextView tvName,tvDes,tvTitle;
    String adKey;

    private FirebaseUser mFirebaseUser;
    Toolbar toolbar;
    String servicerId;
    private DatabaseReference mDatabaseReference;
    ImageButton imBack;
    TextView tvTitleToolbar;
    TextView tvLocation,tvRatingCount;
    Button btnAddContact;
    String uuid,type;
    ImageView imForward,imBackward;
    int count = 2;
    TextView tvRating;
    int i = 0;

    LinearLayout llrating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_view);




        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        if(getIntent()!=null){
            adKey = getIntent().getStringExtra("adId");

        }
        llrating = (LinearLayout) findViewById(R.id.llrating);

        im = (ImageView) findViewById(R.id.im);
        imProfile = (RoundedImageView) findViewById(R.id.imProfile);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDes = (TextView) findViewById(R.id.tvDes);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        imBack = (ImageButton) findViewById(R.id.imBack);
        tvTitleToolbar = (TextView) findViewById(R.id.tvTitleToolbar);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
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

                    if(a.getServicerId().equals(uuid)){

                        Drawable d = getResources().getDrawable(R.drawable.button_disable);

                        btnAddContact.setBackground(d);
                        int margin = getResources().getDimensionPixelSize(R.dimen._20sdp);

                        btnAddContact.setPadding(margin,margin,margin,margin);
                        btnAddContact.setOnClickListener(null);
                        btnAddContact.setEnabled(false);


                    }else{
                        btnAddContact.setEnabled(true);

                        btnAddContact.setClickable(true);

                    }


                    tvTitle.setText(a.getAdTitle());
                    tvTitleToolbar.setText(a.getAdTitle());
                    tvDes.setText(a.getAdDescription());


                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.not_found)
                            .error(R.drawable.not_found);



                    Glide.with(AdViewActivity.this).load(a.getAdImage1()).apply(options).into(im);
                    ///Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                    servicerId = a.getServicerId();

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

                            RequestOptions options = new RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.not_found)
                                    .error(R.drawable.not_found);

                            i++;

                            if(i==3) // switch to 11 because you got 10 images
                            {
                                i=0; // switch to 10, same reason
                            }

                            switch(i)
                            {


                                case 0:




                                    Glide.with(AdViewActivity.this).load(a.getAdImage1()).apply(options).into(im);
                                   /// Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;

                                case 1:


                                    Glide.with(AdViewActivity.this).load(a.getAdImage2()).apply(options).into(im);
                                    //Picasso.get().load(a.getAdImage2()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;
                                case 2:


                                    Glide.with(AdViewActivity.this).load(a.getAdImage3()).apply(options).into(im);
                                    //Picasso.get().load(a.getAdImage3()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);

                                    break;
                            }



                            Log.e("countF",String.valueOf(i));




                        }
                    });
                    imBackward.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RequestOptions options = new RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.not_found)
                                    .error(R.drawable.not_found);

                            i--;

                            if(i==-1)
                            {
                                i=0;
                            }


                            switch(i)
                            {

                                case 0:
                                    Glide.with(AdViewActivity.this).load(a.getAdImage1()).apply(options).into(im);

                                    //Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;

                                case 1:
                                    Glide.with(AdViewActivity.this).load(a.getAdImage2()).apply(options).into(im);

                                    //Picasso.get().load(a.getAdImage2()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;
                                case 2:
                                    Glide.with(AdViewActivity.this).load(a.getAdImage3()).apply(options).into(im);

                                   // Picasso.get().load(a.getAdImage3()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);

                                    break;
                            }


                            Log.e("countb",String.valueOf(i));


                        }
                    });



                    imProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            //Toast.makeText(getApplicationContext(),servicerId+" ",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(AdViewActivity.this,SellerProfilePublicActivity.class);
                            intent.putExtra("servicerId",servicerId);
                            startActivity(intent);

                        }
                    });
                    mDatabaseReference.child("Users").child(servicerId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Servicer s = dataSnapshot.getValue(Servicer.class);
                            tvName.setText(s.getFirstName()+" "+s.getLastName());
                            tvLocation.setText(s.getLocation());

                            Picasso.get().load(s.getProfileImg()).placeholder(R.drawable.profile_im).error(R.drawable.profile_im).into(imProfile);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //Query query = FirebaseDatabase.getInstance().getReference().child("Contacts").orderByChild("servicerId").startAt(servicerId).endAt(servicerId+ "\uf8ff");


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Contacts");

        Query query = FirebaseDatabase.getInstance().getReference().child("Contacts").orderByChild("adsId").startAt(adKey).endAt(adKey+ "\uf8ff");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){


                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                             Contacts c = postSnapshot.getValue(Contacts.class);

                        if(c.getCustomerId().equals(uuid)){

                            Drawable d = getResources().getDrawable(R.drawable.button_disable);

                            btnAddContact.setBackground(d);
                            int margin = getResources().getDimensionPixelSize(R.dimen._20sdp);

                            btnAddContact.setPadding(margin,margin,margin,margin);
                            btnAddContact.setOnClickListener(null);
                            btnAddContact.setEnabled(false);

                        }else{


                            btnAddContact.setClickable(true);

                            btnAddContact.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String n = tvName.getText().toString();
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AdViewActivity.this);
                                    builder1.setMessage("Would you like to connect with "+n+" ?");
                                    builder1.setCancelable(true);
                                    builder1.setPositiveButton(
                                            "Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                    final String uuidContact = UUID.randomUUID().toString().toUpperCase();

                                                    Date currentTime = Calendar.getInstance().getTime();
                                                    databaseReference.child(uuidContact).child("adsId").setValue(adKey);
                                                    databaseReference.child(uuidContact).child("customerId").setValue(uuid);
                                                    databaseReference.child(uuidContact).child("dateTime").setValue((int) (new Date().getTime()/1000));
                                                    databaseReference.child(uuidContact).child("requestState").setValue("Accepted");
                                                    databaseReference.child(uuidContact).child("servicerId").setValue(servicerId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {


                                                            Drawable d = getResources().getDrawable(R.drawable.button_disable);

                                                            btnAddContact.setBackground(d);
                                                            int margin = getResources().getDimensionPixelSize(R.dimen._20sdp);

                                                            btnAddContact.setPadding(margin,margin,margin,margin);
                                                            btnAddContact.setOnClickListener(null);
                                                            btnAddContact.setOnClickListener(null);
                                                            btnAddContact.setEnabled(false);

                                                            Intent intent=new Intent(AdViewActivity.this,ContactActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });

                                                }
                                            });

                                    builder1.setNegativeButton(
                                            "No",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();



                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();


                                }
                            });






                        }

                    }









                }else{

                    btnAddContact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String n = tvName.getText().toString();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(AdViewActivity.this);
                            builder1.setMessage("Would you like to connect with "+n+" ?");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            final String uuidContact = UUID.randomUUID().toString().toUpperCase();

                                            Date currentTime = Calendar.getInstance().getTime();
                                            databaseReference.child(uuidContact).child("adsId").setValue(adKey);
                                            databaseReference.child(uuidContact).child("customerId").setValue(uuid);
                                            databaseReference.child(uuidContact).child("dateTime").setValue((int) (new Date().getTime()/1000));
                                            databaseReference.child(uuidContact).child("requestState").setValue("Accepted");
                                            databaseReference.child(uuidContact).child("servicerId").setValue(servicerId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {


                                                    Drawable d = getResources().getDrawable(R.drawable.button_disable);

                                                    btnAddContact.setBackground(d);
                                                    int margin = getResources().getDimensionPixelSize(R.dimen._20sdp);

                                                    btnAddContact.setPadding(margin,margin,margin,margin);
                                                    btnAddContact.setClickable(false);
                                                    btnAddContact.setOnClickListener(null);
                                                    btnAddContact.setEnabled(false);

                                                    Intent intent=new Intent(AdViewActivity.this,ContactActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });

                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();



                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();


                        }
                    });


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