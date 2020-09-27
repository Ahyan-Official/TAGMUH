package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class AdViewActivity extends AppCompatActivity {

    ImageView im;
    RoundedImageView imProfile;
    TextView tvName,tvDes,tvTitle;
    String adKey;

    private FirebaseUser mFirebaseUser;
    Toolbar toolbar;

    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        if(getIntent()!=null){
            adKey = getIntent().getStringExtra("adId");

        }
        im = (ImageView) findViewById(R.id.im);
        imProfile = (RoundedImageView) findViewById(R.id.imProfile);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDes = (TextView) findViewById(R.id.tvDes);
        tvTitle = (TextView) findViewById(R.id.tvTitle);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Servicer");
        mDatabaseReference.child("Ads").child(adKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Ads a = dataSnapshot.getValue(Ads.class);

                    tvTitle.setText(a.getAdTitle());

                    tvDes.setText(a.getAdDescription());


                    String servicerId = a.getServicerId();


                    mDatabaseReference.child("Users").child(servicerId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Servicer s = dataSnapshot.getValue(Servicer.class);
                            tvName.setText(s.getFirstName()+" "+s.getLastName());
                            Picasso.get().load(s.getProfileImg()).placeholder(R.drawable.profile_im).error(R.drawable.profile_im).into(imProfile);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }
}