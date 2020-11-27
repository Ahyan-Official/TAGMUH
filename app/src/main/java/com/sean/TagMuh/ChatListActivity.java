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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import java.util.concurrent.ConcurrentHashMap;

public class ChatListActivity extends AppCompatActivity {



    Toolbar toolbar;

    FirebaseRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView recyclerView;
    String uuid,type;
    TextView tvNo;

    ImageButton imBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);


        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvNo = (TextView) findViewById(R.id.tvNo);
        setSupportActionBar(toolbar);








        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);





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


        imBack = (ImageButton)findViewById(R.id.imBack);


        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChatListActivity.this,ContactActivity.class);
                startActivity(intent);

            }
        });




    }
    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvEmail;
        RoundedImageView imProfile;
        LinearLayout ll1;

        public ContactsViewHolder(View itemView) {
            super(itemView);

            ll1 = itemView.findViewById(R.id.ll1);


            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            imProfile = itemView.findViewById(R.id.imProfile);

        }


    }






    ItemTouchHelper.SimpleCallback callback;


    private void fetch(Query query) {




        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(query, Contacts.class).build();



        adapter = new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
            @Override
            public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chta_list_row, parent, false);

                return new ContactsViewHolder(view);
            }



            @Override
            protected void onBindViewHolder(final ContactsViewHolder holder, final int position, final Contacts model) {







                Log.e("asd2 ",String.valueOf(position));

                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Users").child(model.getServicerId());

                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        Servicer s = dataSnapshot.getValue(Servicer.class);

                        holder.tvName.setText(s.getFirstName()+" "+s.getLastName());
                        holder.tvEmail.setText(s.getEmail());

                        Picasso.get().load(s.getProfileImg()).placeholder(R.drawable.not_found).error(R.drawable.not_found).fit().centerCrop().config(Bitmap.Config.RGB_565).into(holder.imProfile);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                holder.ll1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(ChatListActivity.this,ChatActivity.class);

                        intent.putExtra("servicerId",model.getServicerId().toString());
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




}