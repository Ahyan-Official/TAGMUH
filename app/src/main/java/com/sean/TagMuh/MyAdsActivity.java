package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MyAdsActivity extends AppCompatActivity {

    AHBottomNavigation bottomNavigation;

    FirebaseRecyclerAdapter adapter;
    Toolbar toolbar;
    LinearLayoutManager linearLayoutManager;

    RecyclerView recyclerView;
    ImageButton imback;
    String uuid,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);



        imback = (ImageButton) findViewById(R.id.imback);

        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));



        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bnve);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);



        setSupportActionBar(toolbar); //NO PROBLEM !!!!






        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


        fetch();








    }



    public class AdsViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public TextView txtDesc;
        RelativeLayout rlAd;


        public AdsViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.tvTitle);
            txtDesc = itemView.findViewById(R.id.tvDec);
            rlAd = itemView.findViewById(R.id.rlAd);

        }

        public void setTxtTitle(String string) {
            txtTitle.setText(string);
        }


        public void setTxtDesc(String string) {
            txtDesc.setText(string);
        }
    }








    private void fetch() {


        Query query = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads").orderByChild("servicerId").startAt(uuid).endAt(uuid+ "\uf8ff");


        FirebaseRecyclerOptions<Ads> options = new FirebaseRecyclerOptions.Builder<Ads>().setQuery(query, Ads.class).build();



        adapter = new FirebaseRecyclerAdapter<Ads, AdsViewHolder>(options) {
            @Override
            public AdsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_itemview, parent, false);

                return new AdsViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(AdsViewHolder holder, final int position, Ads model) {
                holder.setTxtTitle(model.getAdTitle());
                holder.setTxtDesc(model.getAdDescription());

                holder.rlAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(MyAdsActivity.this,EditAdActivity.class);

                        intent.putExtra("adId",getRef(position).getKey().toString());
                        startActivity(intent);
                    }
                });


            }

        };
        recyclerView.setAdapter(adapter);
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



                case R.id.tvFilter:

                    break;
            }

        }
    };



    AHBottomNavigation.OnTabSelectedListener ob = new AHBottomNavigation.OnTabSelectedListener() {
        @Override
        public boolean onTabSelected(int position, boolean wasSelected) {
            switch(position){

                case 0:


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
                    Intent intent3 = new Intent(getApplicationContext(),ProfileActivity.class);

                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    overridePendingTransition(0,0);
                    startActivity(intent3);

                    break;


            }

            return true;
        }
    };


}