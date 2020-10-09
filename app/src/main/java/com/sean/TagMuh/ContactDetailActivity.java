package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactDetailActivity extends AppCompatActivity {





    TextView tvCall,tvEmail,tvMessage;
    Button btnCall,btnEmail,btnMessage,btnPersonalPhoto;
    DatabaseReference databaseReference;

    String servicerId;

    String phone,email;
    ImageButton imBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);




        if(getIntent()!=null){
            servicerId = getIntent().getStringExtra("servicerId");

        }
        tvCall = (TextView) findViewById(R.id.tvCall);
        tvEmail = (TextView) findViewById(R.id.tvEmail);

        tvMessage = (TextView) findViewById(R.id.tvMessage);

        btnCall = (Button) findViewById(R.id.btnCall);
        btnEmail = (Button) findViewById(R.id.btnEmail);
        btnMessage = (Button) findViewById(R.id.btnMessage);
        btnPersonalPhoto = (Button) findViewById(R.id.btnPersonalPhoto);
        imBack = (ImageButton) findViewById(R.id.imBack);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Users").child(servicerId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Servicer s = dataSnapshot.getValue(Servicer.class);

                phone = s.getPhoneNumber();
                email = s.getEmail();

                tvCall.setText(s.getPhoneNumber());
                tvEmail.setText(s.getEmail());
                tvMessage.setText(s.getPhoneNumber());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+phone));
                startActivity(callIntent);

            }
        });

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                sendSmsMsgFnc(phone,"Hi");

            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");



                try {
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactDetailActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnPersonalPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ContactDetailActivity.this,PersonalPhotoShowActivity.class);
                intent.putExtra("servicerId",servicerId);
                startActivity(intent);

            }
        });


        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onBackPressed();


            }
        });







    }

    void sendSmsMsgFnc(String mblNumVar, String smsMsgVar)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {
            try
            {



                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse("sms:" + phone));
                startActivity(smsIntent);



            }
            catch (Exception ErrVar)
            {
                Toast.makeText(getApplicationContext(),ErrVar.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ErrVar.printStackTrace();
            }
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RESULT_OK && requestCode==10){


            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
            smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.setData(Uri.parse("sms:" + phone));
            startActivity(smsIntent);



        }




























    }
}