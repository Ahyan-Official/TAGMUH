package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AdminAdViewActivity extends AppCompatActivity {

    ImageView im;
    RoundedImageView imProfile;
    TextView tvName,tvDes,tvTitle;
    String adKey;

    private FirebaseUser mFirebaseUser;
    Toolbar toolbar;
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

    String location,email,phone;
    ImageButton imback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_ad_view);




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










        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");
        mDatabaseReference.child("Ads").child(adKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    final AdsAdmin a = dataSnapshot.getValue(AdsAdmin.class);


                    tvTitle.setText(a.getAdTitle());
                    tvTitleToolbar.setText(a.getAdTitle());
                    tvDes.setText(a.getAdDescription());
                    email = a.getEmail();
                    location = a.getLocation();
                    phone = a.getPhoneNumber();


                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.not_found)
                            .error(R.drawable.not_found);



                    Glide.with(AdminAdViewActivity.this).load(a.getAdImage1()).apply(options).into(im);


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




                                    Glide.with(AdminAdViewActivity.this).load(a.getAdImage1()).apply(options).into(im);
                                    /// Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;

                                case 1:


                                    Glide.with(AdminAdViewActivity.this).load(a.getAdImage2()).apply(options).into(im);
                                    //Picasso.get().load(a.getAdImage2()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;
                                case 2:


                                    Glide.with(AdminAdViewActivity.this).load(a.getAdImage3()).apply(options).into(im);
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
                                    Glide.with(AdminAdViewActivity.this).load(a.getAdImage1()).apply(options).into(im);

                                    //Picasso.get().load(a.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;

                                case 1:
                                    Glide.with(AdminAdViewActivity.this).load(a.getAdImage2()).apply(options).into(im);

                                    //Picasso.get().load(a.getAdImage2()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);
                                    break;
                                case 2:
                                    Glide.with(AdminAdViewActivity.this).load(a.getAdImage3()).apply(options).into(im);

                                    // Picasso.get().load(a.getAdImage3()).placeholder(R.drawable.not_found).error(R.drawable.not_found).into(im);

                                    break;
                            }


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



        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdminAdViewActivity.this);
                LayoutInflater inflater = AdminAdViewActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.admin_contact, null);
                dialogBuilder.setView(dialogView);

                TextView tvCall = (TextView) dialogView.findViewById(R.id.tvCall);
                TextView tvEmail = (TextView) dialogView.findViewById(R.id.tvEmail);
                TextView tvMessage = (TextView) dialogView.findViewById(R.id.tvMessage);
                TextView tvDismiss = (TextView) dialogView.findViewById(R.id.tvDismiss);

                tvCall.setText("Tel: "+phone);
                tvEmail.setText("Email: "+email);
                tvMessage.setText("Message: "+phone);



                tvCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:"+phone));
                        startActivity(callIntent);

                    }
                });

                tvMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {





                        sendSmsMsgFnc(phone,"Hi");

                    }
                });

                tvEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto",email, null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");



                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send email..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(AdminAdViewActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                tvDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        alertDialog.dismiss();
                    }
                });

            }
        });





        imback =(ImageButton) findViewById(R.id.imBack);



        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });







    }

    void sendSmsMsgFnc(String mblNumVar, String smsMsgVar)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {
            try
            {



                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse("sms:" + phone));
                startActivity(smsIntent);



            }
            catch (Exception ErrVar)
            {
                Toast.makeText(getApplicationContext(),ErrVar.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ErrVar.printStackTrace();
            }
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
            }
        }

    }

}