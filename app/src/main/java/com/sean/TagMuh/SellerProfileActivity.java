package com.sean.TagMuh;

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
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class SellerProfileActivity extends AppCompatActivity {


    RoundedImageView profile_im;
    TextView tvName,tvEmail,tvAdCount;
    CardView cdProfile,cdCreatePost,cdMyAds;
    Toolbar toolbar;
    private StorageReference mStorageReference;

    private FirebaseUser mFirebaseUser;

    private DatabaseReference mDatabaseReference;
    AHBottomNavigation bottomNavigation;
    String uuid,type;

    ImageView imLogout,imUploadPhoto;
    DatabaseReference databaseReferenceAdCount;
        TextView tvRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);



        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));




        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cdMyAds = (CardView) findViewById(R.id.cdMyAds);
        cdProfile = (CardView) findViewById(R.id.cdProfile);
        cdCreatePost = (CardView) findViewById(R.id.cdCreatePost);
        imLogout = (ImageView) findViewById(R.id.imLogout);
        imUploadPhoto = (ImageView) findViewById(R.id.imUploadPhoto);
        tvRating = (TextView) findViewById(R.id.tvRating);

        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvAdCount = (TextView) findViewById(R.id.tvAdCount);

        profile_im = (RoundedImageView) findViewById(R.id.profile_im);


        mStorageReference = FirebaseStorage.getInstance().getReference();
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bnve);


        AHBottomNavigationItem item1 = new AHBottomNavigationItem("", R.drawable.bar_allad, R.color.gray);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("", R.drawable.search_icon, R.color.gray);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.feed, R.color.gray);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("", R.drawable.bar_profile, R.color.gray);
//        int qq = getResources().getDimensionPixelSize(R.dimen._10sdp);
//        int qqa = getResources().getDimensionPixelSize(R.dimen._11sdp);
//
//        bottomNavigation.setTitleTextSize(qqa,qq);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);


        bottomNavigation.setVisibility(View.VISIBLE);


        bottomNavigation.setAccentColor(Color.parseColor("#66cd95"));
        bottomNavigation.setInactiveColor(Color.parseColor("#959595"));
        bottomNavigation.enableItemAtPosition(3);
        bottomNavigation.setCurrentItem(3);
        bottomNavigation.setForceTint(true);
        bottomNavigation.setNotificationBackgroundColorResource(R.color.colorPrimary);
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setOnTabSelectedListener(ob);





        Toast.makeText(getApplicationContext(),"Signed In As Seller",Toast.LENGTH_SHORT).show();



        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Users").child(uuid);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Servicer c = dataSnapshot.getValue(Servicer.class);
                tvName.setText(c.getFirstName()+" "+c.getLastName());
                tvEmail.setText(c.getEmail());
                Picasso.get().load(c.getProfileImg()).placeholder(R.drawable.profile_im).error(R.drawable.profile_im).into(profile_im);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReferenceAdCount = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads");
        Query query = databaseReferenceAdCount.orderByChild("servicerId").startAt(uuid).endAt(uuid+ "\uf8ff");
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



        Query query2 = FirebaseDatabase.getInstance().getReference().child("Ratings").orderByChild("userId").startAt(uuid).endAt(uuid+ "\uf8ff");
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












        cdMyAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(SellerProfileActivity.this,MyAdsActivity.class);
                startActivity(intent);
            }
        });


        cdCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(SellerProfileActivity.this,CreatePostActivity.class);
                startActivity(intent);


            }
        });


        cdProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                Intent intent = new Intent(SellerProfileActivity.this,EditProfileActivity.class);
                startActivity(intent);



            }
        });
        imUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SellerProfileActivity.this,UploadPersonalPhotoSellerActivity.class);
                startActivity(intent);

            }
        });



        imLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent=new Intent(SellerProfileActivity.this,SettingsActivity.class);
                startActivity(intent);



            }
        });


    }






    AHBottomNavigation.OnTabSelectedListener ob = new AHBottomNavigation.OnTabSelectedListener() {
        @Override
        public boolean onTabSelected(int position, boolean wasSelected) {
            switch(position){

                case 0:

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    overridePendingTransition(0,0);
                    startActivity(intent);

                    break;

                case 1:
                    Intent intent1 = new Intent(getApplicationContext(),SearchActivity.class);

                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    overridePendingTransition(0,0);
                    startActivity(intent1);

                    break;

                case 2:
                    Intent intent2 = new Intent(getApplicationContext(),ContactActivity.class);

                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    overridePendingTransition(0,0);
                    startActivity(intent2);

                    break;

                case 3:


                    break;


            }

            return true;
        }
    };
}