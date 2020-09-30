package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import id.zelory.compressor.Compressor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreatePostActivity extends AppCompatActivity {



    private FirebaseUser mFirebaseUser;


    private StorageReference mStorageReference;
    ProgressDialog mProgressDialog;


    String uuid,type;


    //-------GETTING ITEM NO FOR IMAGE-------
    private static final int GALLERY_PICK = 1;
    private static final int GALLERY_PICK_2 = 2;
    private static final int GALLERY_PICK_3 = 3;


    ImageButton imback,im1,im2,im3;

    String email;
    Button btnUpload;
    Uri resultUri,uri1,uri2,uri3;
    DatabaseReference databaseReference;
    String category;
    String adUUID;

    EditText etTitle,etDes;
    String a = "https://firebasestorage.googleapis.com/v0/b/tagmuh-ios.appspot.com/o/Servicer%2FAds%2F584DF8BF-D725-4566-ADE3-7637BF887F37?alt=media&token=5a78637f-8b81-4a5b-9644-cae3ffbcc9ad";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);



        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        //uid=mFirebaseUser.getUid();

        //mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
       // mDatabaseReference.keepSynced(true);

        mProgressDialog=new ProgressDialog(this);


        SharedPreferences shared = getSharedPreferences("UUID", MODE_PRIVATE);
        uuid = (shared.getString("UUID", ""));
        type = (shared.getString("type", ""));


        mStorageReference = FirebaseStorage.getInstance().getReference();
        imback = (ImageButton) findViewById(R.id.imback);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etDes = (EditText) findViewById(R.id.etDes);

        im1 = (ImageButton) findViewById(R.id.im1);
        im2 = (ImageButton) findViewById(R.id.im2);
        im3 = (ImageButton) findViewById(R.id.im3);

        btnUpload = (Button) findViewById(R.id.btnUpload);

        adUUID = UUID.randomUUID().toString().toUpperCase();

        mStorageReference = FirebaseStorage.getInstance().getReference();

        Spinner spinner = findViewById(R.id.spinner);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select Category");
        arrayList.add("Carpentry");
        arrayList.add("Delivery");
        arrayList.add("Electrical");
        arrayList.add("Graphics");
        arrayList.add("Maintenance");
        arrayList.add("Mobile Apps");
        arrayList.add("Media");
        arrayList.add("Pumping");
        arrayList.add("Plumbing");
        arrayList.add("Photographer");
        arrayList.add("Services");
        arrayList.add("Septic");
        arrayList.add("Tiling");
        arrayList.add("Tractor");
        arrayList.add("Trucking");
        arrayList.add("Trash Removal");
        arrayList.add("Voiceover");
        arrayList.add("Website");



        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });





        databaseReference = FirebaseDatabase.getInstance().getReference().child("Servicer").child("Ads").child(adUUID);








        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ActivityCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreatePostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PICK);
                } else {

                    Intent galleryIntent=new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK);

                }
            }
        });
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ActivityCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreatePostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PICK);
                } else {

                    Intent galleryIntent=new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK_2);

                }
            }
        });
        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ActivityCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreatePostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PICK);
                } else {

                    Intent galleryIntent=new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK_3);

                }
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(etTitle.getText().toString().isEmpty() || etDes.getText().toString().isEmpty() || category.equals("Select Category")){

                    Toast.makeText(getApplicationContext(),"Plz Fill details",Toast.LENGTH_SHORT).show();
                }else{

                    mProgressDialog.setTitle("Uploading Image");
                    mProgressDialog.setMessage("Please wait while we process and upload the image...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.show();

                    if(uri1!=null || uri2!=null || uri3!=null){


                        uploadIm1();




                    }

                    databaseReference.child("adTitle").setValue(etTitle.getText().toString());
                    databaseReference.child("adDescription").setValue(etDes.getText().toString());
                    databaseReference.child("servicerId").setValue(uuid);
                }






            }
        });

    }



    public void uploadIm1(){

        if(uri1!=null){


            String adstoreageUUID = UUID.randomUUID().toString().toUpperCase();

            final StorageReference filepath=mStorageReference.child("Servicer").child("Ads").child(adstoreageUUID+".png");

            //------STORING IMAGE IN FIREBASE STORAGE--------
            filepath.putFile(uri1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();

                                databaseReference.child("adImage1").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        if(task.isSuccessful()){
                                            uri1 = null;

                                                uploadIm2();




                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), " Image is not uploading...", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });


                            }
                        });









                    }
                    else{
                        mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), " Image is not uploading...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{

            databaseReference.child("adImage1").setValue(a);

                uploadIm2();



        }


    }


    public void uploadIm2(){

        if(uri2!=null){


            String adstoreageUUID = UUID.randomUUID().toString().toUpperCase();

            final StorageReference filepath=mStorageReference.child("Servicer").child("Ads").child(adstoreageUUID+".png");

            //------STORING IMAGE IN FIREBASE STORAGE--------
            filepath.putFile(uri2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();

                                databaseReference.child("adImage2").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        if(task.isSuccessful()){
                                            uri2 = null;

                                                uploadIm3();



                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), " Image is not uploading...", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });


                            }
                        });









                    }
                    else{
                        mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), " Image is not uploading...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{

            databaseReference.child("adImage2").setValue(a);

            uploadIm3();


        }


    }


    public void uploadIm3(){

        if(uri3!=null){


            String adstoreageUUID = UUID.randomUUID().toString().toUpperCase();

            final StorageReference filepath=mStorageReference.child("Servicer").child("Ads").child(adstoreageUUID+".png");

            //------STORING IMAGE IN FIREBASE STORAGE--------
            filepath.putFile(uri3).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();

                                databaseReference.child("adImage3").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        if(task.isSuccessful()){


                                            uri3 = null;

                                            databaseReference.child("category").setValue(category).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mProgressDialog.dismiss();
                                                }
                                            });

                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), " Image is not uploading...", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });


                            }
                        });









                    }
                    else{
                        mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), " Image is not uploading...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            databaseReference.child("adImage3").setValue(a);


            databaseReference.child("category").setValue(category).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mProgressDialog.dismiss();
                }
            });
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //-----STARTING GALLERY----
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            uri1 = data.getData();
            im1.setImageURI(uri1);



        }
        if(requestCode == GALLERY_PICK_2 && resultCode == RESULT_OK){

            uri2 = data.getData();
            im2.setImageURI(uri2);



        }
        if(requestCode == GALLERY_PICK_3 && resultCode == RESULT_OK){

            uri3 = data.getData();

            im3.setImageURI(uri3);


        }

        //------START CROP IMAGE ACTIVITY------
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {

            //------CROP IMAGE RESULT------
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {




                resultUri = result.getUri();
                im1.setImageURI(resultUri);



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
}