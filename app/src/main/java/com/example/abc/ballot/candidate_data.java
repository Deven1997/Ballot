package com.example.abc.ballot;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class candidate_data extends AppCompatActivity {

    ImageView imageView;
    Button buttonGallery,btnapply;
    Uri uri;
    Intent GalIntent, CropIntent ;
    public  static final int RequestPermissionCode  = 1 ;
    DatabaseReference apply_reff;
    List<election> eleList = new ArrayList<>(  );
    ProgressDialog progressDailog;
    String dept,electionID;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_data);

        dept = getIntent().getExtras().getString( "dept" );
        electionID = getIntent().getExtras().getString( "electionID" );

        //position are passed

        imageView = findViewById(R.id.image_viewu );

        buttonGallery = (Button)findViewById(R.id.BTNChooseImageu );

        progressDailog = new ProgressDialog(this);
       // progressDailog.setTitle("Uploading..!!");
        //progressDailog.setMessage("Please Wait!!");
       // progressDailog.setCancelable(true);
       // progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        EnableRuntimePermission();

//        buttonCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                ClickImageFromCamera() ;
//
//            }
//        });

        apply_reff = FirebaseDatabase.getInstance().getReference().child( "department").child( "election").child( dept);
        apply_reff.addValueEventListener( new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eleList.clear();
                for(DataSnapshot electsnap: dataSnapshot.getChildren()){
                    election e = electsnap.getValue(election.class);
                    eleList.add(e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


        btnapply=findViewById(R.id.BTNApply);
        storageReference= FirebaseStorage.getInstance().getReference();
        btnapply.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Toast.makeText( candidate_data.this, "Your data saved Sucessfully...", Toast.LENGTH_SHORT ).show( );

                if(uri !=null){
                    StorageReference riversRef = storageReference.child("images/Candidate_profile.jpg");
                    progressDailog.show();

                    riversRef.putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDailog.dismiss();
                                    Toast.makeText( candidate_data.this, "data uploaded", Toast.LENGTH_SHORT ).show( );
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                    progressDailog.dismiss();
                                    Toast.makeText( candidate_data.this, exception.getMessage(), Toast.LENGTH_SHORT ).show( );
                                }
                            }).addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>( ) {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressDailog.setMessage( ((int) progress) +"% Uploading..." );

                        }
                    } );
                }
                else{
                    Toast.makeText( candidate_data.this, "Image not selected....", Toast.LENGTH_SHORT ).show( );
                }

            }
        } );




        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetImageFromGallery();

            }
        });

    }

//    public void ClickImageFromCamera() {
//
//        CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//        file = new File( Environment.getExternalStorageDirectory(),
//                         "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
//        uri = Uri.fromFile(file);
//
//        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
//
//        CamIntent.putExtra("return-data", true);
//
//        startActivityForResult(CamIntent, 0);
//
//    }

    public void GetImageFromGallery(){

        GalIntent = new Intent(Intent.ACTION_PICK,
                               android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {

           ImageCropFunction();

        }
        else if (requestCode == 2) {

            if (data != null) {

                uri = data.getData();

               ImageCropFunction();

            }
        }
        else if (requestCode == 1) {

            if (data != null) {

                Bundle bundle = data.getExtras();

                Bitmap bitmap = bundle.getParcelable( "data");

                imageView.setImageBitmap(bitmap);

            }
        }
    }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 3);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }

    }
    //Image Crop Code End Here

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale( candidate_data.this,
                                                                 Manifest.permission.CAMERA))
        {

            Toast.makeText(candidate_data.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(candidate_data.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

//    @Override
//    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
//
//        switch (RC) {
//
//            case RequestPermissionCode:
//
//                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    Toast.makeText(candidate_data.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
//
//                } else {
//
//                    Toast.makeText(candidate_data.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
//
//                }
//                break;
//        }
    //}
}

