package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

public class ContactActivity extends AppCompatActivity {


    AHBottomNavigation bottomNavigation;

    Toolbar toolbar;

    FirebaseRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    SwipeableRecyclerView recyclerView;
    String uuid,type;
    TextView tvNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bnve);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (SwipeableRecyclerView) findViewById(R.id.recyclerView);
        tvNo = (TextView) findViewById(R.id.tvNo);
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



//        if(type.equals("buyer")){
//
//            Query query = FirebaseDatabase.getInstance().getReference().child("Contacts").orderByChild("customerId").equalTo(uuid);
//
//            fetch(query);
//
//        }else{
//
//            Query query = FirebaseDatabase.getInstance().getReference().child("Contacts").orderByChild("servicerId").equalTo(uuid);
//            fetch(query);
//
//
//
//        }





        Query query2 = FirebaseDatabase.getInstance().getReference().child("Contacts").orderByChild("customerId").startAt(uuid).endAt(uuid+ "\uf8ff");
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




        Query query = FirebaseDatabase.getInstance().getReference().child("Contacts").orderByChild("customerId").startAt(uuid).endAt(uuid+ "\uf8ff");
        fetch(query);

        //fetch();







    }
    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public TextView txtDesc;
        RoundedImageView image;
        Button btnContactDetails,btnViewAds;
        TextView tvRatingCount,tvRating;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.tvTitle);
            txtDesc = itemView.findViewById(R.id.tvDec);
            image = (RoundedImageView) itemView.findViewById(R.id.image);
            btnContactDetails = (Button) itemView.findViewById(R.id.btnContactDetails);
            tvRatingCount = itemView.findViewById(R.id.tvRatingCount);
            tvRating = itemView.findViewById(R.id.tvRating);

            btnViewAds = (Button) itemView.findViewById(R.id.btnViewAds);

        }

        public void setTxtTitle(String string) {
            txtTitle.setText(string);
        }


        public void setTxtDesc(String string) {
            txtDesc.setText(string);
        }
    }






    ItemTouchHelper.SimpleCallback callback;


    private void fetch(Query query) {



//
//
//        callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
//                // Take action for the swiped item
//
//
//                if(direction == ItemTouchHelper.LEFT){
//
//
//                    recyclerView.removeView(viewHolder.itemView);
//
//
//
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ContactActivity.this);
//                    builder1.setMessage("Delete?");
//                    builder1.setCancelable(true);
//
//
//
//                    builder1.setPositiveButton(
//                            "Yes",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
//
//                                    dialog.cancel();
//                                    //recyclerView.removeView(holder.itemView);
//                                    recyclerView.removeView(viewHolder.itemView);
//                                    viewHolder.getAdapterPosition();
//
//                                    FirebaseDatabase.getInstance().getReference().child("Contacts").child(getRef(position).getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            //recyclerView.removeViewAt(position);
//                                            //recyclerView.removeViewAt(position);
//                                            //adapter.notifyDataSetChanged();
//                                            //recyclerView.removeViewAt(position);
//                                            recyclerView.removeView(viewHolder.itemView);
//
//
//                                        }
//                                    });
//
//                                }
//                            });
//
//                    builder1.setNegativeButton(
//                            "No",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    recyclerView.removeView(viewHolder.itemView);
//
//                                    //recyclerView.removeView(holder.itemView);
//
//
//
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//
//
//                }
//
//
//            }
//
//            @Override
//            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//
//                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                        .addSwipeLeftLabel("Delete")
//                        //.addActionIcon(R.drawable.id)
//                        .addBackgroundColor(ContextCompat.getColor(ContactActivity.this, R.color.red))
//                        .create()
//                        .decorate();
//
//
//
//
//
//
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//
//            @Override
//            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
//
//
//
//                super.onSelectedChanged(viewHolder, actionState);
//            }
//        };
//
//
//
//



        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(query, Contacts.class).build();



        adapter = new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
            @Override
            public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_itemview, parent, false);

                return new ContactsViewHolder(view);
            }



            @Override
            protected void onBindViewHolder(final ContactsViewHolder holder, final int position, final Contacts model) {
               // holder.setTxtTitle(model.getAdsId());
                //holder.setTxtDesc(model.getCustomerId());

                //recyclerView.setRightBg(R.color.red);




                recyclerView.setListener(new SwipeLeftRightCallback.Listener() {
                    @Override
                    public void onSwipedLeft(final int position) {

                        recyclerView.setRightBg(R.color.red);

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ContactActivity.this);
                        builder1.setMessage("Delete?");
                        builder1.setCancelable(true);



                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        recyclerView.setRightBg(R.color.white);

                                        dialog.cancel();
                                        //recyclerView.removeView(holder.itemView);
                                        recyclerView.removeViewAt(position);


                                        FirebaseDatabase.getInstance().getReference().child("Contacts").child(getRef(position).getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Intent intent=new Intent(ContactActivity.this,RatingActivity.class);
                                                intent.putExtra("servicerId",model.getServicerId());
                                                startActivity(intent);



                                                //recyclerView.removeViewAt(position);
                                                //recyclerView.removeViewAt(position);
                                                //adapter.notifyDataSetChanged();
                                                //recyclerView.removeViewAt(position);
                                                //recyclerView.setRightBg(R.color.white);


                                            }
                                        });

                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                  new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        recyclerView.removeViewAt(position);

                                        //recyclerView.removeView(holder.itemView);



                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                        Log.e("asd",String.valueOf(getRef(position).getKey()));

                        //recyclerView.removeViewAt(position);
                        //recyclerView.setRightBg(R.color.white);
                        //recyclerView.removeViewAt(position);
                        //adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onSwipedRight(int position) {
                        //mList.remove(position);
                        adapter.notifyDataSetChanged();
                    }





                });




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






                Log.e("asd2 ",String.valueOf(position));

                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Users").child(model.getServicerId());

                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        Servicer s = dataSnapshot.getValue(Servicer.class);

                       //holder.txtTitle.setText(s.getFirstName()+ " "+s.getLastName());
                        holder.txtDesc.setText(s.getLocation());
                        //Picasso.get().load(s.getProfileImg()).placeholder(R.drawable.not_found).error(R.drawable.not_found).fit().centerCrop().config(Bitmap.Config.RGB_565).into(holder.image);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads").child(model.getAdsId());

                db2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        Ads s = dataSnapshot.getValue(Ads.class);

                        holder.txtTitle.setText(s.getAdTitle());
                        ///holder.txtDesc.setText(s.getLocation());
                        Picasso.get().load(s.getAdImage1()).placeholder(R.drawable.not_found).error(R.drawable.not_found).fit().centerCrop().config(Bitmap.Config.RGB_565).into(holder.image);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                holder.btnContactDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(ContactActivity.this,ContactDetailActivity.class);

                        intent.putExtra("servicerId",model.getServicerId().toString());
                        startActivity(intent);




                    }
                });


                holder.btnViewAds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(ContactActivity.this,AdViewActivity.class);

                        intent.putExtra("adId",model.getAdsId().toString());
                        startActivity(intent);



                    }
                });



            }

        };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);

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

                    Intent intent2 = new Intent(getApplicationContext(),SearchActivity.class);

                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    overridePendingTransition(0,0);
                    startActivity(intent2);
                    break;

                case 2:


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