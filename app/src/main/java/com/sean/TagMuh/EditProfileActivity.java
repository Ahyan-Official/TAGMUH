package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseAuth mauth;
    ProgressDialog progressDialog;
    private DatabaseReference mDatabase;

    TextInputLayout etFN,etLN,etGL,etPN,etDes;
    Button btnSave;
    String uuid,type;
    ImageButton imBack;
    RoundedImageView profile_im;



    private static final int GALLERY_PICK = 1;
    String email;
    Uri resultUri;

    DatabaseReference databaseReference;

    private StorageReference mStorageReference;
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


        mStorageReference = FirebaseStorage.getInstance().getReference();


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

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Users").child(uuid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Servicer s = dataSnapshot.getValue(Servicer.class);
                email = s.getEmail();

                //Picasso.get().load(s.getProfileImg()).placeholder(R.drawable.profile_im).error(R.drawable.profile_im).into(im);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        profile_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PICK);
                } else {

                    Intent galleryIntent=new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK);

                }
            }
        });

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();


            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //-----STARTING GALLERY----
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri sourceUri = data.getData();

            //-------CROPPING IMAGE AND SETTING MINIMUM SIZE TO 500 , 500------
            CropImage.activity(sourceUri).
                    setAspectRatio(1,1).
                    setMinCropWindowSize(500,500).
                    start(EditProfileActivity.this);

        }

        //------START CROP IMAGE ACTIVITY------
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {

            //------CROP IMAGE RESULT------
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {




                resultUri = result.getUri();
                profile_im.setImageURI(resultUri);



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case GALLERY_PICK:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, GALLERY_PICK);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
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



            progressDialog.setTitle("Adding Details");
            progressDialog.setMessage("Please wait while we are adding details... ");
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



                    if(resultUri!=null){


                        final StorageReference filepath=mStorageReference.child("Servicer").child(email).child(uuid+".png");

                        //------STORING IMAGE IN FIREBASE STORAGE--------
                        filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if(task.isSuccessful()){

                                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String downloadUrl = uri.toString();

                                            mDatabase.child("profileImg").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {


                                                    if(task.isSuccessful()){
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EditProfileActivity.this, "Uploaded Successfuly...", Toast.LENGTH_SHORT).show();

                                                    }
                                                    else{
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getApplicationContext(), " Image is not uploading...", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });


                                        }
                                    });









                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), " Image is not uploading...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        progressDialog.dismiss();
                        Intent intent=new Intent(EditProfileActivity.this,SellerProfileActivity.class);
                        startActivity(intent);
                        finish();


                    }








                }
                else{


                }
            }
        });



    }
}