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

public class MainActivity extends AppCompatActivity {


    AHBottomNavigation bottomNavigation;

    FirebaseRecyclerAdapter adapter;
    Toolbar toolbar;
    LinearLayoutManager linearLayoutManager;

    RecyclerView recyclerView;

    ListView listView;
    String countryList[] = {"All Ads", "Carpentry", "Delivery", "Electrical", "Graphics", "Maintenance", "Mobile Apps","Media", "Pumping","Plumbing","Photographer","Services","Septic","Tiling","Tractor","Trucking","Trash Removal","Voiceover","Website"};
    ImageButton btnCross;

    RelativeLayout rlFilter;
    TextView tvFilter;

    FirebaseAuth mAuth;
    String uuid,type;

    TextView tvNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));

        tvNo = (TextView) findViewById(R.id.tvNo);


        listView = (ListView)findViewById(R.id.listView);
        tvFilter = (TextView) findViewById(R.id.tvFilter);
        btnCross = (ImageButton) findViewById(R.id.btnCross);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.ad_filter_view, R.id.btnItem, countryList);
        listView.setAdapter(arrayAdapter);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Button btn = view.findViewById(R.id.btnItem);

                rlFilter.setVisibility(View.GONE);


                if(btn.getText().toString().equals("All Ads")){


                    Query query = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads");
                    adapter = null;
                    fetch(query);
                }else{
                    Query query = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads").orderByChild("category").startAt(btn.getText().toString()).endAt(btn.getText().toString()+ "\uf8ff");
                    adapter = null;
                    fetch(query);
                }





                //Toast.makeText(getApplicationContext(),btn.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        rlFilter = (RelativeLayout) findViewById(R.id.rlFilter);

        btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                rlFilter.setVisibility(View.GONE);
            }
        });
        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rlFilter.setVisibility(View.VISIBLE);

            }
        });



        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){

            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bnve);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);



        setSupportActionBar(toolbar); //NO PROBLEM !!!!


        AHBottomNavigationItem item1 = new AHBottomNavigationItem("", R.drawable.all_add_3, R.color.gray);
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
        bottomNavigation.enableItemAtPosition(0);
        bottomNavigation.setCurrentItem(0);
        bottomNavigation.setForceTint(true);
        bottomNavigation.setNotificationBackgroundColorResource(R.color.colorPrimary);
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setOnTabSelectedListener(ob);



        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        Query query2 = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads");
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



        Query query = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads");

        fetch(query);








    }



    public class AdsViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public TextView txtDesc;
        RelativeLayout rlAd;
        RoundedImageView image;
        TextView tvRatingCount,tvRating;


        public AdsViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.tvTitle);
            txtDesc = itemView.findViewById(R.id.tvDec);
            rlAd = itemView.findViewById(R.id.rlAd);
            image = itemView.findViewById(R.id.image);
            tvRatingCount = itemView.findViewById(R.id.tvRatingCount);
            tvRating = itemView.findViewById(R.id.tvRating);


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
                Picasso.get().load(model.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).fit().centerCrop().config(Bitmap.Config.RGB_565).into(holder.image);

                Query query2 = FirebaseDatabase.getInstance().getReference().child("Ratings").orderByChild("userId").startAt(model.getServicerId()).endAt(model.getServicerId()+ "\uf8ff");
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

                            holder.tvRating.setText(String.valueOf(d));

                            holder.tvRatingCount.setText("("+String.valueOf(s)+")");

                        }else{



                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });











                holder.rlAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(MainActivity.this,AdViewActivity.class);

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


                    if(type.equals("buyer")){

                        Intent intent3 = new Intent(getApplicationContext(),ProfileActivity.class);

                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        overridePendingTransition(0,0);
                        startActivity(intent3);
                    }else{

                        Intent intent3 = new Intent(getApplicationContext(),SellerProfileActivity.class);

                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        overridePendingTransition(0,0);
                        startActivity(intent3);

                    }


                    break;


            }

            return true;
        }
    };





}