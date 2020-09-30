package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseAuth mauth;
    Button buttonsubmit;
    ProgressDialog progressDialog;
    private DatabaseReference mDatabase;

    TextInputLayout etFN,etLN,etGL,etPN,etDes;
    Button btnSave;
    String uuid,type;
    ImageButton imBack;
    RoundedImageView profile_im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));





//        mauth.getCurrentUser().getUid();
//
//        mauth.getUid();



        etFN=(TextInputLayout)findViewById(R.id.edFN);
        etLN=(TextInputLayout)findViewById(R.id.etLN);
        etGL=(TextInputLayout)findViewById(R.id.etGL);
        etPN =(TextInputLayout)findViewById(R.id.etPN);
        etDes=(TextInputLayout)findViewById(R.id.etDes);
        btnSave = (Button) findViewById(R.id.btnSave);
        profile_im = (RoundedImageView) findViewById(R.id.profile_im);
        imBack = (ImageButton) findViewById(R.id.imBack);


        progressDialog=new ProgressDialog(EditProfileActivity.this);
        mauth=FirebaseAuth.getInstance();




        mDatabase = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Users").child(uuid);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Servicer c = dataSnapshot.getValue(Servicer.class);
                etFN.getEditText().setText(c.getFirstName());
                etLN.getEditText().setText(c.getLastName());
                etGL.getEditText().setText(c.getLocation());
                etPN.getEditText().setText(c.getPhoneNumber());
                etDes.getEditText().setText(c.getDescription());


                Picasso.get().load(c.getProfileImg()).placeholder(R.drawable.profile_im).error(R.drawable.profile_im).into(profile_im);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();


            }
        });



    }



    //-----REGISTER BUTTON IS PRESSED---
    public void buttonIsClicked(View view){

        if(view.getId()==R.id.btnSave){

            String FN=etFN.getEditText().getText().toString().trim();
            String LN=etLN.getEditText().getText().toString().trim();
            String GL=etGL.getEditText().getText().toString().trim();
            String PN=etPN.getEditText().getText().toString().trim();
            String des=etDes.getEditText().getText().toString().trim();


            //----CHECKING THE EMPTINESS OF THE EDITTEXT-----
            if(FN.equals("") || LN.equals("") || GL.equals("") || PN.equals("") || des.equals("")){
                Toast.makeText(EditProfileActivity.this, "Please Fill Details", Toast.LENGTH_SHORT).show();
                return;
            }



            progressDialog.setTitle("Registering User");
            progressDialog.setMessage("Please wait while we are creating your account... ");
            progressDialog.setCancelable(false);
            progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            add_details_user(FN,LN,GL,PN,des);


        }
    }


    private void add_details_user(final String FN, final String LN, final String GL, final String PN, final String des) {






        Log.e("uuid",uuid);
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        //final String uid=current_user.getUid();


        mDatabase.child("description").setValue(des);
        mDatabase.child("firstName").setValue(FN);
        mDatabase.child("lastName").setValue(LN);
        mDatabase.child("location").setValue(GL);
        mDatabase.child("phoneNumber").setValue(PN).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){



                    progressDialog.dismiss();
                    Intent intent=new Intent(EditProfileActivity.this,SellerProfileActivity.class);
                    startActivity(intent);
                    finish();



                }
                else{


                }
            }
        });



    }
}