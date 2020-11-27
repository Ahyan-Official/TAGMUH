package com.sean.TagMuh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.auth.FirebaseAuth;
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
import com.squareup.picasso.Picasso;

public class StoreHoursActivity extends AppCompatActivity {


    Toolbar toolbar;
    private StorageReference mStorageReference;

    private FirebaseUser mFirebaseUser;

    private DatabaseReference databaseReference;
    String uuid,type;

    Switch sw;
    TextView tvAvailable;
    Button save;
    boolean status = true;
    ImageButton imback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_hours);



        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));




        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvAvailable = (TextView) findViewById(R.id.tvAvailable);
        sw = (Switch) findViewById(R.id.sw);
        save = (Button) findViewById(R.id.btnSave);



        mStorageReference = FirebaseStorage.getInstance().getReference();



        databaseReference = FirebaseDatabase.getInstance().getReference().child("Store Hours").child(uuid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){

                    String s = dataSnapshot.child("state").getValue().toString();

                    String desTxt = dataSnapshot.child("desTxt").getValue().toString();

                    tvAvailable.setText(desTxt);
                    if(s.equals("Off")){
                        sw.setChecked(false);

                    }else{

                        sw.setChecked(true);

                    }
                    
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(b){

                    status=true; //edit here
                }else{

                    status=false;
                }



            }
        });








        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.child("desTxt").setValue(tvAvailable.getText().toString());

                if(status){

                    databaseReference.child("state").setValue("Off");
                }else{

                    databaseReference.child("state").setValue("On");
                }

            }
        });



        imback =(ImageButton) findViewById(R.id.imBack);

        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });



    }









}