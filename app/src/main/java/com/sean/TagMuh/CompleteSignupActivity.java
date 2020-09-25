package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CompleteSignupActivity extends AppCompatActivity {


    FirebaseAuth mauth;
    TextInputLayout etdisplayname,etemail,etpassword;
    Button buttonsubmit;
    ProgressDialog progressDialog;
    private DatabaseReference mDatabase;

    TextInputLayout etFN,etLN,etGL,etPN,etDes;
    String email, password;
    Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_signup);




        if(getIntent()!=null){

            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");

        }



//        mauth.getCurrentUser().getUid();
//
//        mauth.getUid();



        etFN=(TextInputLayout)findViewById(R.id.edFN);
        etLN=(TextInputLayout)findViewById(R.id.etLN);
        etGL=(TextInputLayout)findViewById(R.id.etGL);
        etPN =(TextInputLayout)findViewById(R.id.etPN);
        etDes=(TextInputLayout)findViewById(R.id.etDes);
        btnNext = (Button) findViewById(R.id.btnNext);



        buttonsubmit=(Button)findViewById(R.id.button3);
        progressDialog=new ProgressDialog(CompleteSignupActivity.this);
        mauth=FirebaseAuth.getInstance();




        //Database.database().reference().child("Servicer").child("Users").child("\(id)").setValue(["email": "\(self.email)", "firstName": self.firstNameText.text!, "lastName": self.lastNameText.text!, "location": self.stateText.text!, "phoneNumber": self.phoneNumberText.text!, "description": self.descriptionText.text!, "profileImg": "No Image"]) { (error, db_ref) in



        }



    //-----REGISTER BUTTON IS PRESSED---
    public void buttonIsClicked(View view){

        if(view.getId()==R.id.button3){

            String FN=etFN.getEditText().getText().toString().trim();
            String LN=etLN.getEditText().getText().toString().trim();
            String GL=etGL.getEditText().getText().toString().trim();
            String PN=etPN.getEditText().getText().toString().trim();
            String des=etDes.getEditText().getText().toString().trim();


            //----CHECKING THE EMPTINESS OF THE EDITTEXT-----
            if(FN.equals("") || LN.equals("") || GL.equals("") || PN.equals("") || des.equals("")){
                Toast.makeText(CompleteSignupActivity.this, "Please Fill Details", Toast.LENGTH_SHORT).show();
                return;
            }



            progressDialog.setTitle("Registering User");
            progressDialog.setMessage("Please wait while we are creating your account... ");
            progressDialog.setCancelable(false);
            progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            register_user(FN,LN,GL,PN,des,email,password);
        }
    }


    //-----REGISTERING THE NEW USER------
    private void register_user(final String FN, final String LN, final String GL, final String PN, final String des, final String email, String password) {


        final String uuid = UUID.randomUUID().toString().toUpperCase();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Servicer").child("Users").child(uuid);

        mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //------IF USER IS SUCCESSFULLY REGISTERED-----
                if(task.isSuccessful()){

                    Log.e("uuid",uuid);
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    //final String uid=current_user.getUid();
                    Map userMap=new HashMap();
                    userMap.put("description",des);
                    userMap.put("email",email);
                    userMap.put("firstName", FN);
                    userMap.put("lastName",LN);
                    userMap.put("location",GL);
                    userMap.put("phoneNumber",PN);
                    userMap.put("profileImage","No Image");

                    mDatabase.child(uuid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if(task1.isSuccessful()){



                                SharedPreferences preferences = getSharedPreferences("UUID", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("UUID",uuid);
                                editor.apply();

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "New User is created", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(CompleteSignupActivity.this,MainActivity.class);

                                //----REMOVING THE LOGIN ACTIVITY FROM THE QUEUE----
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();



                            }
                            else{

                                Toast.makeText(CompleteSignupActivity.this, "YOUR NAME IS NOT REGISTERED... MAKE NEW ACCOUNT-- ", Toast.LENGTH_SHORT).show();

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
}