package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class EditAdActivity extends AppCompatActivity {



    ImageView im;
    String adKey;

    private FirebaseUser mFirebaseUser;
    Toolbar toolbar;
    String servicerId;
    private DatabaseReference mDatabaseReference;
    ImageButton imBack;
    TextView tvTitleToolbar;
    TextInputLayout edTitle,etDes,etCategory;
    Button btnDelete;
    ProgressDialog progressDialog;
    ImageView imForward,imBackward;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ad);


        progressDialog=new ProgressDialog(EditAdActivity.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        if(getIntent()!=null){
            adKey = getIntent().getStringExtra("adId");

        }
        im = (ImageView) findViewById(R.id.im);
        imBack = (ImageButton) findViewById(R.id.imBack);


        etCategory = (TextInputLayout) findViewById(R.id.etCategory);
        etDes = (TextInputLayout) findViewById(R.id.etDes);
        edTitle = (TextInputLayout) findViewById(R.id.etTitle);
        tvTitleToolbar =(TextView) findViewById(R.id.tvTitleToolbar);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        imForward = (ImageView) findViewById(R.id.imForward);
        imBackward = (ImageView) findViewById(R.id.imBackward);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Servicer");

        mDatabaseReference.child("Ads").child(adKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    final Ads a = dataSnapshot.getValue(Ads.class);

                    edTitle.getEditText().setText(a.getAdTitle());
                    tvTitleToolbar.setText(a.getAdTitle());
                    etDes.getEditText().setText(a.getAdDescription());
                    etCategory.getEditText().setText(a.getCategory());

                    //Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                    servicerId = a.getServicerId();

                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.not_found)
                            .error(R.drawable.not_found);



                    Glide.with(EditAdActivity.this).load(a.getAdImage1()).apply(options).into(im);

                    //Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);

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




                                    Glide.with(EditAdActivity.this).load(a.getAdImage1()).apply(options).into(im);
                                    /// Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;

                                case 1:


                                    Glide.with(EditAdActivity.this).load(a.getAdImage2()).apply(options).into(im);
                                    //Picasso.get().load(a.getAdImage2()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;
                                case 2:


                                    Glide.with(EditAdActivity.this).load(a.getAdImage3()).apply(options).into(im);
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
                                i=0; // you can leave it this way or improve it later
                            }


                            switch(i)
                            {

                                case 0:
                                    Glide.with(EditAdActivity.this).load(a.getAdImage1()).apply(options).into(im);

                                    //Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;

                                case 1:
                                    Glide.with(EditAdActivity.this).load(a.getAdImage2()).apply(options).into(im);

                                    //Picasso.get().load(a.getAdImage2()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;
                                case 2:
                                    Glide.with(EditAdActivity.this).load(a.getAdImage3()).apply(options).into(im);

                                    // Picasso.get().load(a.getAdImage3()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);

                                    break;
                            }


                            Log.e("countb",String.valueOf(i));


                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Deleting...");
                progressDialog.setCancelable(false);
                progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Servicer");
                mDatabaseReference.child("Ads").child(adKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressDialog.dismiss();
                        Intent intent = new Intent(EditAdActivity.this,MyAdsActivity.class);

                        startActivity(intent);
                        finish();

                    }
                });


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