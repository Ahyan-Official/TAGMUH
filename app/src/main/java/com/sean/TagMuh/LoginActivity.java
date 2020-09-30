package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    TextInputLayout emailTextInputLayout,passTextInputLayout;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mauth;



    FirebaseAuth mAuth;
    TextView forget;

    boolean seller = true;
    boolean buyer = false;

    TextView tvSignUp;

    TextView tvSeller,tvBuyer;
    ImageView lineSeller,lineBuyer;
    CardView cdBuyer,cdSeller;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //this.setTitle("Login");

        emailTextInputLayout=(TextInputLayout)findViewById(R.id.editText1);
        passTextInputLayout=(TextInputLayout)findViewById(R.id.editText2);
        progressDialog=new ProgressDialog(LoginActivity.this);

        mauth=FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        forget = (TextView) findViewById(R.id.forget);


        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);



        tvSeller=(TextView) findViewById(R.id.tvSeller);
        tvBuyer=(TextView)findViewById(R.id.tvBuyer);

        cdBuyer=(CardView) findViewById(R.id.cdBuyer);
        cdSeller=(CardView)findViewById(R.id.cdSeller);


        lineSeller=(ImageView)findViewById(R.id.lineSeller);
        lineBuyer=(ImageView)findViewById(R.id.lineBuyer);


        mAuth = FirebaseAuth.getInstance();





        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailTextInputLayout.getEditText().getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Please Fill Email", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();

                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(getApplicationContext(),"Email sent",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }

            }
        });








        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }


    @Override
    public void onBackPressed() {
        finish();
    }


    public class MyListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    }

    public void buttonIsClicked(View view){

        switch(view.getId()){

            case R.id.btnLogin:


                final String email=emailTextInputLayout.getEditText().getText().toString().trim();
                String password=passTextInputLayout.getEditText().getText().toString().trim();

                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Please Fill all blocks", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if(buyer){

                    progressDialog.setTitle("Logging in");
                    progressDialog.setMessage("Please wait while we are checking the credentials..");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                mDatabaseReference.child("Customer").child("Users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for(DataSnapshot data: dataSnapshot.getChildren()) {

                                            String uuid = data.getKey();
                                            Log.e("uuid",uuid);

                                            SharedPreferences preferences = getSharedPreferences("UUID", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("UUID",uuid);
                                            editor.putString("type","buyer");
                                            editor.apply();

                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                            intent.putExtra("uuid",uuid);
                                            intent.putExtra("type","buyer");

                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();



                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }else{


                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();



                            }


                        }
                    });




                }else if(seller){



                    progressDialog.setTitle("Logging in");
                    progressDialog.setMessage("Please wait while we are checking the credentials..");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();


                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){


                                mDatabaseReference.child("Servicer").child("Users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for(DataSnapshot data: dataSnapshot.getChildren()) {

                                            String uuid = data.getKey();
                                            Log.e("uuid",uuid);

                                            SharedPreferences preferences = getSharedPreferences("UUID", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("UUID",uuid);
                                            editor.putString("type","seller");

                                            editor.apply();

                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                            intent.putExtra("uuid",uuid);
                                            intent.putExtra("type","seller");

                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();




                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }else{

                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();


                            }



                        }
                    });







                }




                break;

            case R.id.buttonRegister:

                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void login_user(String email, String password) {

        //---SIGN IN FOR THE AUTHENTICATE EMAIL-----
        mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){

                            String user_id=mauth.getCurrentUser().getUid();
                            String token_id= FirebaseInstanceId.getInstance().getToken();
                            Map addValue = new HashMap();
                            addValue.put("device_token",token_id);
                            addValue.put("online","true");



                            mDatabaseReference.child(user_id).updateChildren(addValue, new DatabaseReference.CompletionListener(){

                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                    if(databaseError==null){

                                        //---OPENING MAIN ACTIVITY---
                                        Log.e("Login : ","Logged in Successfully" );

                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, databaseError.toString()  , Toast.LENGTH_SHORT).show();
                                        Log.e("Error is : ",databaseError.toString());

                                    }
                                }
                            });


                            Toast.makeText(getApplicationContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else{
                            //---IF AUTHENTICATION IS WRONG----
                            Toast.makeText(LoginActivity.this, "Wrong Credentials" +
                                    "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private DatabaseReference mDatabase;






}
