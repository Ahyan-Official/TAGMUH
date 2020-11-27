package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class AdminAdsListActivity extends AppCompatActivity {



    FirebaseRecyclerAdapter adapter;
    Toolbar toolbar;
    LinearLayoutManager linearLayoutManager;

    RecyclerView recyclerView;



    FirebaseAuth mAuth;
    String uuid,type;
    TextView tvNo;

    ImageButton imback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_ads_list);


        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));

       ;



        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){

            Intent intent = new Intent(AdminAdsListActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvNo = (TextView) findViewById(R.id.tvNo);



        setSupportActionBar(toolbar); //NO PROBLEM !!!!




        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        Query query2 = FirebaseDatabase.getInstance().getReference().child("Admin").child("Ads");
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int a = (int)dataSnapshot.getChildrenCount();
                if(a==0){
                    tvNo.setVisibility(View.VISIBLE);
                }else{

                    tvNo.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Query query = FirebaseDatabase.getInstance().getReference().child("Admin").child("Ads");

        fetch(query);





        imback =(ImageButton) findViewById(R.id.imBack);

        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


    }



    public class AdsViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public TextView txtDesc;
        RelativeLayout rlAd;
        RoundedImageView image;
        ImageView rating;

        TextView tvRatingCount,tvRating;


        public AdsViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.tvTitle);
            txtDesc = itemView.findViewById(R.id.tvDec);
            rlAd = itemView.findViewById(R.id.rlAd);
            image = itemView.findViewById(R.id.image);
            tvRatingCount = itemView.findViewById(R.id.tvRatingCount);
            tvRating = itemView.findViewById(R.id.tvRating);
            rating = itemView.findViewById(R.id.rating);


        }

        public void setTxtTitle(String string) {
            txtTitle.setText(string);
        }


        public void setTxtDesc(String string) {
            txtDesc.setText(string);
        }


    }








    private void fetch(Query query) {


        FirebaseRecyclerOptions<Ads> options = new FirebaseRecyclerOptions.Builder<Ads>().setQuery(query, Ads.class).build();



        adapter = new FirebaseRecyclerAdapter<Ads, AdsViewHolder>(options) {
            @Override
            public AdsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_itemview, parent, false);

                return new AdsViewHolder(view);

            }


            @Override
            protected void onBindViewHolder(final AdsViewHolder holder, final int position, Ads model) {

                holder.setTxtTitle(model.getAdTitle());
                holder.setTxtDesc(model.getAdDescription());

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.not_found)
                        .error(R.drawable.not_found);
                Glide.with(AdminAdsListActivity.this).load(model.getAdImage1()).apply(options).into(holder.image);

                //Picasso.get().load(model.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).fit().centerCrop().config(Bitmap.Config.RGB_565).into(holder.image);

                holder.rating.setVisibility(View.GONE);

                holder.tvRating.setVisibility(View.GONE);
                holder.tvRatingCount.setVisibility(View.GONE);
                Query query2 = FirebaseDatabase.getInstance().getReference().child("Ratings").orderByChild("userId").startAt(model.getServicerId()).endAt(model.getServicerId()+ "\uf8ff");
//                query2.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists()){
//
//                            int sum = 0;
//                            int count = 0;
//                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//
//
//                                String c = postSnapshot.child("rate").getValue().toString();
//                                Log.e("test452 ", String.valueOf(c));
//
//                                sum = sum + Integer.parseInt(c);
//                                //count = count + 1;
//
//                            }
//
//                            //Log.e("test45",sum+" asdas"+count);
//
//                            int s = (int)dataSnapshot.getChildrenCount();
//
//                            double d = sum / s;
//                            //Log.e("test45", String.valueOf(sum));
//
//                            holder.tvRating.setVisibility(View.GONE);
//                            holder.tvRatingCount.setVisibility(View.GONE);
//
//                            holder.tvRating.setText(String.valueOf(d));
//
//                            holder.tvRatingCount.setText("("+String.valueOf(s)+")");
//
//                        }else{
//
//
//
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });











                holder.rlAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(AdminAdsListActivity.this,AdminAdViewActivity.class);

                        intent.putExtra("adId",getRef(position).getKey().toString());
                        startActivity(intent);
                    }
                });


            }

        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //menu.add(0,3,0,"Filter").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



    View.OnClickListener ov = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            switch(view.getId()){

                case R.id.btnCross:


                    break;

                case R.id.tvFilter:

                    break;
            }

        }
    };






}