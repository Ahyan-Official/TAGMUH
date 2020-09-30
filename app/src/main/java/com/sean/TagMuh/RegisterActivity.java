package com.sean.TagMuh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;


import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mauth;
    TextInputLayout etEmail,etP,etPN;
    Button buttonsubmit;
    ProgressDialog progressDialog;
    private DatabaseReference mDatabase;

    TextView tvLogin;

    boolean seller = true;
    boolean buyer = false;


    TextView tvSeller,tvBuyer;
    ImageView lineSeller,lineBuyer;
    CardView cdBuyer,cdSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //this.setTitle("Register");

        etEmail=(TextInputLayout)findViewById(R.id.etEmail);
        etP=(TextInputLayout)findViewById(R.id.etP);
        etPN=(TextInputLayout)findViewById(R.id.etCP);


        tvSeller=(TextView) findViewById(R.id.tvSeller);
        tvBuyer=(TextView)findViewById(R.id.tvBuyer);

        cdBuyer=(CardView) findViewById(R.id.cdBuyer);
        cdSeller=(CardView)findViewById(R.id.cdSeller);


        lineSeller=(ImageView)findViewById(R.id.lineSeller);
        lineBuyer=(ImageView)findViewById(R.id.lineBuyer);


        buttonsubmit=(Button)findViewById(R.id.button3);
        progressDialog=new ProgressDialog(RegisterActivity.this);

        mauth=FirebaseAuth.getInstance();


        tvLogin=(TextView) findViewById(R.id.tvLogin);



        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();


            }
        });





        cdBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                buyer = true;
                seller = false;


                tvSeller.setTextColor(Color.parseColor("#d2d2d2"));
                lineSeller.setBackgroundColor(Color.parseColor("#d2d2d2"));


                tvBuyer.setTextColor(Color.parseColor("#646464"));
                lineBuyer.setBackgroundColor(Color.parseColor("#67cc97"));





            }
        });

        cdSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                buyer = false;
                seller = true;

                tvBuyer.setTextColor(Color.parseColor("#d2d2d2"));
                lineBuyer.setBackgroundColor(Color.parseColor("#d2d2d2"));


                tvSeller.setTextColor(Color.parseColor("#646464"));
                lineSeller.setBackgroundColor(Color.parseColor("#67cc97"));




            }
        });


    }

    //-----REGISTER BUTTON IS PRESSED---
    public void buttonIsClicked(View view){

        if(view.getId()==R.id.button3){

            String email =etEmail.getEditText().getText().toString().trim();
            String password=etP.getEditText().getText().toString().trim();
            String conPassword=etPN.getEditText().getText().toString().trim();


            if(!email.equals("")){


                if(etP.getEditText().getText().toString().equals(etPN.getEditText().getText().toString())){



                    if(seller){

                        Intent intent=new Intent(RegisterActivity.this,CompleteSignupActivity.class);
                        intent.putExtra("email",email);
                        intent.putExtra("password",password);
                        startActivity(intent);

                    }else if(buyer){



                        progressDialog.setTitle("Registering User");
                        progressDialog.setMessage("Please wait while we are creating your account... ");
                        progressDialog.setCancelable(false);
                        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        register_user(email,password);



                    }







                }else{

                    Toast.makeText(RegisterActivity.this, "Password Don't match", Toast.LENGTH_SHORT).show();


                }


            }else{

                Toast.makeText(RegisterActivity.this, "Please Fill the email", Toast.LENGTH_SHORT).show();


            }



        }
    }


    private void register_user(final String email, final String password) {


        final String uuid = UUID.randomUUID().toString().toUpperCase();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Customer").child("Users").child(uuid);

        mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //------IF USER IS SUCCESSFULLY REGISTERED-----
                if(task.isSuccessful()){



                    String name = email.substring(0,email.indexOf("@"));


                    Log.e("uuid",uuid);
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    Map userMap=new HashMap();
                    userMap.put("email",email);
                    userMap.put("name", name);
                    userMap.put("profileImg","No Image");

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if(task1.isSuccessful()){



                                SharedPreferences preferences = getSharedPreferences("UUID", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("UUID",uuid);
                                editor.putString("type","buyer");

                                editor.apply();

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "New User is created", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();



                            }
                            else{

                                Toast.makeText(RegisterActivity.this, "YOUR NAME IS NOT REGISTERED... MAKE NEW ACCOUNT-- ", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


                }
                //---ERROR IN ACCOUNT CREATING OF NEW USER---
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "ERROR REGISTERING USER....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    //-----REGISTERING THE NEW USER------
}
