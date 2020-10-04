package com.sean.TagMuh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RatingActivity extends AppCompatActivity {

    String uuid,type;
    Button btnExcellent,btnVeryGood,btnGood,btnFair,btnPoor;
    DatabaseReference databaseReference;
    String servicerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));

        btnExcellent = (Button) findViewById(R.id.btnExcellent);
        btnVeryGood = (Button) findViewById(R.id.btnVeryGood);
        btnGood = (Button) findViewById(R.id.btnGood);
        btnFair = (Button) findViewById(R.id.btnFair);
        btnPoor = (Button) findViewById(R.id.btnPoor);
        if(getIntent()!=null){

            servicerId = getIntent().getStringExtra("servicerId");
        }

        Log.e("serve",servicerId+"sad");


        final String uuidRating = UUID.randomUUID().toString().toUpperCase();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Ratings").child(uuidRating);

        btnExcellent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                databaseReference.child("rate").setValue(5);
                databaseReference.child("userId").setValue(servicerId).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent=new Intent(RatingActivity.this,ContactActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();


                    }
                });


            }
        });
        btnVeryGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                databaseReference.child("rate").setValue(4);
                databaseReference.child("userId").setValue(servicerId).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent=new Intent(RatingActivity.this,ContactActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();


                    }
                });


            }
        });
        btnGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                databaseReference.child("rate").setValue(3);
                databaseReference.child("userId").setValue(servicerId).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent=new Intent(RatingActivity.this,ContactActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();


                    }
                });


            }
        });
        btnFair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                databaseReference.child("rate").setValue(2);
                databaseReference.child("userId").setValue(servicerId).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent=new Intent(RatingActivity.this,ContactActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();


                    }
                });


            }
        });
        btnPoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                databaseReference.child("rate").setValue(1);
                databaseReference.child("userId").setValue(servicerId).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent=new Intent(RatingActivity.this,ContactActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();


                    }
                });


            }
        });







    }
}