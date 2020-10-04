package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class SellerProfilePublicActivity extends AppCompatActivity {


    RoundedImageView profile_im;
    TextView tvName,tvEmail,tvAdCount,tvRating;
    String servicerId;
    DatabaseReference databaseReference;
    ImageView imHome;
    ImageButton imback;
    DatabaseReference databaseReferenceAdCount;
    CardView cdLiveAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile_public);




        if(getIntent()!=null){
            servicerId = getIntent().getStringExtra("servicerId");

        }

        imback = (ImageButton) findViewById(R.id.imback);
        profile_im = (RoundedImageView) findViewById(R.id.profile_im);
        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvAdCount = (TextView) findViewById(R.id.tvAdCount);
        tvRating = (TextView) findViewById(R.id.tvRating);
        imHome = (ImageView) findViewById(R.id.imHome);
        cdLiveAds = (CardView) findViewById(R.id.cdLiveAds);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Servicer").child("Users").child(servicerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Servicer s = dataSnapshot.getValue(Servicer.class);

                    tvName.setText(s.getFirstName()+" "+s.getLastName());

                    tvEmail.setText(s.getEmail());


                    Picasso.get().load(s.getProfileImg()).placeholder(R.drawable.profile_im).error(R.drawable.profile_im).into(profile_im);





                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        databaseReferenceAdCount = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads");
        Query query = databaseReferenceAdCount.orderByChild("servicerId").startAt(servicerId).endAt(servicerId+ "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int a = (int)dataSnapshot.getChildrenCount();
                tvAdCount.setText(String.valueOf(a));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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


                }else{



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







        cdLiveAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProfilePublicActivity.this,LiveAdsOtherActivity.class);
                intent.putExtra("servicerId",servicerId);
                startActivity(intent);


            }
        });




        imHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProfilePublicActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


    }
}