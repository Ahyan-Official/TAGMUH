package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    String servicerId;
    private DatabaseReference mDatabaseReference;
    ImageButton imBack;
    TextView tvTitleToolbar;
    TextView tvLocation;
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
        imBack = (ImageButton) findViewById(R.id.imBack);
        tvTitleToolbar = (TextView) findViewById(R.id.tvTitleToolbar);
        tvLocation = (TextView) findViewById(R.id.tvLocation);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Servicer");
        mDatabaseReference.child("Ads").child(adKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Ads a = dataSnapshot.getValue(Ads.class);

                    tvTitle.setText(a.getAdTitle());
                    tvTitleToolbar.setText(a.getAdTitle());
                    tvDes.setText(a.getAdDescription());

                    Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                    servicerId = a.getServicerId();


                    Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);

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















        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });






    }
}