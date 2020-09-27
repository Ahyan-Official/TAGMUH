package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import id.zelory.compressor.Compressor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity {



    private FirebaseUser mFirebaseUser;

    private DatabaseReference mDatabaseReference;

    private StorageReference mStorageReference;
    ProgressDialog mProgressDialog;

    String status="";
    String uid;
    byte[] thumb_bytes=null;


    //-------GETTING ITEM NO FOR IMAGE-------
    private static final int GALLERY_PICK = 1;
    private static final int GALLERY_PICK_2 = 2;
    private static final int GALLERY_PICK_3 = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);


        mProgressDialog=new ProgressDialog(this);

        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        //uid=mFirebaseUser.getUid();

        //mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
       // mDatabaseReference.keepSynced(true);




        mStorageReference = FirebaseStorage.getInstance().getReference();




        Spinner spinner = findViewById(R.id.spinner);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("JAVA");
        arrayList.add("ANDROID");
        arrayList.add("C Language");
        arrayList.add("CPP Language");
        arrayList.add("Go Language");
        arrayList.add("AVN SYSTEMS");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
    }



    View.OnClickListener ov = new View.OnClickListener() {
        @Override
        public void onClick(View view) {



            switch(view.getId()){

                //------CHANGING IMAGE------
                case R.id.im1:

                    Intent galleryIntent=new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK);
                    break;
                case R.id.im2:

                    Intent galleryIntent2=new Intent();
                    galleryIntent2.setType("image/*");
                    galleryIntent2.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent2,"Select Image"),GALLERY_PICK_2);
                    break;
                case R.id.im3:

                    Intent galleryIntent3=new Intent();
                    galleryIntent3.setType("image/*");
                    galleryIntent3.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent3,"Select Image"),GALLERY_PICK_3);
                    break;

                default:
                    break;

            }

        }
    };



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
                    start(CreatePostActivity.this);

        }

        //------START CROP IMAGE ACTIVITY------
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {

            //------CROP IMAGE RESULT------
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mProgressDialog.setTitle("Uploading Image");
                mProgressDialog.setMessage("Please wait while we process and upload the image...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.show();


                Uri resultUri = result.getUri();
                File thumb_filepath = new File(resultUri.getPath());
                try {

                    //--------COMPRESSING IMAGE--------
                    Bitmap thumb_bitmap = new Compressor(this).
                            setMaxWidth(200).
                            setMaxHeight(200).
                            setQuality(75).
                            compressToBitmap(thumb_filepath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumb_bytes= baos.toByteArray();


                } catch (IOException e) {
                    e.printStackTrace();
                }

                StorageReference filepath=mStorageReference.child("profile_image").child(uid+".jpg");
                final StorageReference thumb_file_path=mStorageReference.child("profile_image").child("thumbs").child(uid+".jpg");

                //------STORING IMAGE IN FIREBASE STORAGE--------
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){

                            @SuppressWarnings("VisibleForTests")
                            final String downloadUrl=  task.getResult().getStorage().getDownloadUrl().toString();
                            UploadTask uploadTask = thumb_file_path.putBytes(thumb_bytes);

                            //---------- STORING THUMB IMAGE INTO STORAGE REFERENCE --------
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    @SuppressWarnings("VisibleForTests")
                                    String thumb_download_url=thumb_task.getResult().getStorage().getDownloadUrl().toString();
                                    if(thumb_task.isSuccessful()){
                                        Map update_HashMap=new HashMap();
                                        update_HashMap.put("image",downloadUrl);
                                        update_HashMap.put("thumb_image",thumb_download_url);

                                        //--------ADDING URL INTO DATABASE REFERENCE--------
                                        mDatabaseReference.updateChildren(update_HashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){
                                                    mProgressDialog.dismiss();
                                                    Toast.makeText(CreatePostActivity.this, "Uploaded Successfuly...", Toast.LENGTH_SHORT).show();

                                                }
                                                else{
                                                    mProgressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), " Image is not uploading...", Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        });

                                    }
                                    else{
                                        mProgressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), " Error in uploading Thumbnail..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                        else{
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), " Image is not uploading...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }
}