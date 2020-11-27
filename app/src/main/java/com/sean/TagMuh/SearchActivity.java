package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {


    AHBottomNavigation bottomNavigation;

    Toolbar toolbar;

    FirebaseRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView recyclerView;
    ClearableEditText tvSearch;
    boolean search =false;

    String uuid,type;


    ImageButton imEnvalop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));


        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bnve);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvSearch = (ClearableEditText) findViewById(R.id.tvSearch);

        setSupportActionBar(toolbar); //NO PROBLEM !!!!
        imEnvalop = (ImageButton)findViewById(R.id.imEnvalop);


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
        bottomNavigation.enableItemAtPosition(1);
        bottomNavigation.setCurrentItem(1);
        bottomNavigation.setForceTint(true);
        bottomNavigation.setNotificationBackgroundColorResource(R.color.colorPrimary);
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setOnTabSelectedListener(ob);






        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);



//        tvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//
//
//
//
//
//                    String searchtext = tvSearch.getText().toString();
//
//
//                    if(searchtext!=null && !searchtext.isEmpty()){
//
//
//                        search = true;
//
//                        Query query = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads").orderByChild("adTitle").startAt(searchtext).endAt(searchtext+ "\uf8ff");
//                        adapter = null;
//                        fetch(query);
//
//
//                    }
//
//
//
//
//                    return true;
//                }
//                return false;
//            }
//        });

        tvSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){


                    String searchtext = tvSearch.getText().toString();


                    if(searchtext!=null && !searchtext.isEmpty()){


                        search = true;

                        Query query = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads").orderByChild("adTitle").startAt(searchtext).endAt(searchtext+ "\uf8ff");
                        adapter = null;
                        fetch(query);


                    }
                }else {

                    Query query = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads");
                    adapter = null;

                    fetch(query);

                }
            }
        });

        Query query = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads");
        fetch(query);



        imEnvalop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SearchActivity.this,AdminAdsListActivity.class);
                startActivity(intent);

            }
        });
    }




    public class AdsViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public TextView txtDesc;
        RelativeLayout rlAd;

        RoundedImageView image;

        public AdsViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.tvTitle);
            txtDesc = itemView.findViewById(R.id.tvDec);
            rlAd = itemView.findViewById(R.id.rlAd);
            image = itemView.findViewById(R.id.image);


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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_itemview, parent, false);

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
                Glide.with(SearchActivity.this).load(model.getAdImage1()).apply(options).into(holder.image);

                //Picasso.get().load(model.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).fit().centerCrop().config(Bitmap.Config.RGB_565).into(holder.image);



                holder.rlAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(SearchActivity.this,AdViewActivity.class);

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


    @Override
    public void onBackPressed() {


        if(search){

            Query query = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads");
            fetch(query);

        }else{
            super.onBackPressed();

        }
    }
}