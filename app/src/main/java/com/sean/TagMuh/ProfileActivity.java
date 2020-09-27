package com.sean.TagMuh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import id.zelory.compressor.Compressor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    AHBottomNavigation bottomNavigation;

    Toolbar toolbar;

    EditText etName;
    Button btnEdit;
    RoundedImageView profile_im;
    boolean editMode = false;
    private static final int GALLERY_PICK = 1;
    ProgressDialog mProgressDialog;
    byte[] thumb_bytes=null;
    private StorageReference mStorageReference;

    private FirebaseUser mFirebaseUser;

    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        mProgressDialog=new ProgressDialog(this);

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bnve);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        btnEdit = (Button) findViewById(R.id.btnEdit);
        etName = (EditText) findViewById(R.id.etName);

        profile_im = (RoundedImageView) findViewById(R.id.profile_im);
        etName.setFocusable(false);
        etName.setClickable(false);

        profile_im.setFocusable(false);
        profile_im.setClickable(false);
        mStorageReference = FirebaseStorage.getInstance().getReference();



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
        bottomNavigation.enableItemAtPosition(3);
        bottomNavigation.setCurrentItem(3);
        bottomNavigation.setForceTint(true);
        bottomNavigation.setNotificationBackgroundColorResource(R.color.colorPrimary);
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setOnTabSelectedListener(ob);







        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(editMode){

                    btnEdit.setText("Edit");

                    etName.setFocusable(false);
                    etName.setClickable(false);
                    profile_im.setClickable(false);
                    profile_im.setFocusable(false);

                    editMode = false;



                }else{

                    btnEdit.setText("Save");

                    etName.setFocusableInTouchMode(true);
                    etName.setClickable(true);

                    profile_im.setFocusableInTouchMode(true);
                    profile_im.setClickable(true);

                    editMode = true;
                }






            }
        });



        profile_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                if(editMode) {

                    Intent galleryIntent=new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK);




                }





            }
        });











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
                    Intent intent1 = new Intent(getApplicationContext(),SearchActivity.class);

                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    overridePendingTransition(0,0);
                    startActivity(intent1);

                    break;

                case 2:
                    Intent intent2 = new Intent(getApplicationContext(),ContactActivity.class);

                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    overridePendingTransition(0,0);
                    startActivity(intent2);

                    break;

                case 3:


                    break;


            }

            return true;
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
                    start(ProfileActivity.this);

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

                StorageReference filepath=mStorageReference.child("Customer");//.child(email).child(uid+".jpg");
                final StorageReference thumb_file_path=mStorageReference.child("profile_image").child("thumbs");//.child(uid+".jpg");

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
                                                    Toast.makeText(ProfileActivity.this, "Uploaded Successfuly...", Toast.LENGTH_SHORT).show();

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