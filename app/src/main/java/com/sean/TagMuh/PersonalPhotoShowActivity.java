package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class PersonalPhotoShowActivity extends AppCompatActivity {



    String servicerId;
    RoundedImageView im;
    ImageButton imBack;
    Toolbar toolbar;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_photo_show);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        im = (RoundedImageView) findViewById(R.id.im);


        if(getIntent()!=null){
            servicerId = getIntent().getStringExtra("servicerId");

        }
        imBack = (ImageButton) findViewById(R.id.imBack);



        databaseReference = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Users").child(servicerId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Servicer s = dataSnapshot.getValue(Servicer.class);

                Picasso.get().load(s.getProfileImg()).placeholder(R.drawable.profile_im).error(R.drawable.profile_im).into(im);


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