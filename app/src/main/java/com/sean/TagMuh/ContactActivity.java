package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ContactActivity extends AppCompatActivity {


    AHBottomNavigation bottomNavigation;

    Toolbar toolbar;

    FirebaseRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);




        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bnve);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        setSupportActionBar(toolbar);


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
        bottomNavigation.enableItemAtPosition(2);
        bottomNavigation.setCurrentItem(2);
        bottomNavigation.setForceTint(true);
        bottomNavigation.setNotificationBackgroundColorResource(R.color.colorPrimary);
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setOnTabSelectedListener(ob);







        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        fetch();







    }
    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public TextView txtDesc;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.tvTitle);
            txtDesc = itemView.findViewById(R.id.tvDec);
        }

        public void setTxtTitle(String string) {
            txtTitle.setText(string);
        }


        public void setTxtDesc(String string) {
            txtDesc.setText(string);
        }
    }








    private void fetch() {

        Query query = FirebaseDatabase.getInstance().getReference().child("Contacts");


        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(query, Contacts.class).build();



        adapter = new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
            @Override
            public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_itemview, parent, false);

                return new ContactsViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ContactsViewHolder holder, final int position, Contacts model) {
                holder.setTxtTitle(model.getAdsId());
                holder.setTxtDesc(model.getCustomerId());


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