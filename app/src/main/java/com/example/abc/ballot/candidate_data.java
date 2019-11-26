package com.example.abc.ballot;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class candidate_data extends AppCompatActivity {

    ImageView imageView;
    Button buttonGallery,btnapply;
    Uri uri;
    Intent GalIntent, CropIntent ;
    public  static final int RequestPermissionCode  = 1 ;
    DatabaseReference apply_reff,apply_reff2;
    List<election> eleList = new ArrayList<>(  );
    ProgressDialog progressDailog;
    ProgressDialog progress2;
    String dept,electionID;
    String stud_name;
    int election_position;
    int post_position;
    String uid;
    List<post> postList = new ArrayList<>();
    HashMap<String,String> post1 = new HashMap<>();
    public StorageReference storageReference;
    DatabaseReference stud_reff;

    TextView AddDiscription,TVpostname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_data);

        dept = getIntent().getExtras().getString( "dept" );
        electionID = getIntent().getExtras().getString( "electionID" );
        post_position = getIntent().getExtras().getInt("position");
        election_position = getIntent().getExtras().getInt("election_position");
        uid = getIntent().getExtras().getString("uid");

        AddDiscription = findViewById(R.id.TVAddDescription);

        imageView = findViewById(R.id.image_viewu );

        buttonGallery = findViewById(R.id.BTNChooseImageu );

        TVpostname = findViewById(R.id.tv_posttitle_id);


        progressDailog = new ProgressDialog(this);

        progress2 = new ProgressDialog(this);
        progress2.setTitle("Data Uploading..!!");
        progress2.setMessage("Please Wait!!");
        progress2.setCancelable(true);
        progress2.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        EnableRuntimePermission();


        stud_reff = FirebaseDatabase.getInstance().getReference().child("students").child(uid).child("name");
        stud_reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stud_name = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        apply_reff = FirebaseDatabase.getInstance().getReference().child( "department").child( "election").child( dept);
        apply_reff.addValueEventListener( new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eleList.clear();
                for(DataSnapshot electsnap: dataSnapshot.getChildren()){
                    election e = electsnap.getValue(election.class);
                    if(e.getElection_id().equals(electionID))
                    {
                      post1 = e.getPost();
                      break;
                    }
                    //eleList.add(e);


                    TVpostname.setText((postList.get(post_position)).getPname());
                }

               // Load_Post();

                for (Map.Entry<String, String> entry : post1.entrySet()) {
                    String pname = entry.getKey();
                    String pdisc = entry.getValue();
                    post p = new post(pname, pdisc);
                    postList.add(p);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );



        btnapply=findViewById(R.id.BTNApply);
        btnapply.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                storageReference= FirebaseStorage.getInstance().getReference();

                post post_obj = postList.get(post_position);
                final String pname = post_obj.getPname();
                final String pdisc = post_obj.getPdisc();

                apply_reff2 = FirebaseDatabase.getInstance().getReference().child("candidate").child(electionID).child(pname).child(uid);



                final String add_discription = AddDiscription.getText().toString().trim();

                if(add_discription.trim().length()==0)
                {
                    Toast.makeText(candidate_data.this, "Your Description needed ", Toast.LENGTH_SHORT).show();
                }
                else {

                    if (uri != null) {



                        final StorageReference imageRef = storageReference.child("candidates/"+uid+"."+getFileExtension(uri));
                        progressDailog.show();

                        imageRef.putFile(uri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        progressDailog.dismiss();
                                        Toast.makeText(candidate_data.this, "Image uploaded", Toast.LENGTH_SHORT).show();

                                        progress2.show();
                                        candidate candidate_obj = new candidate(electionID,pname,pdisc,add_discription,uid,imageRef.getDownloadUrl().toString(),stud_name);
                                        apply_reff2.setValue(candidate_obj);
                                        progress2.dismiss();
                                        Toast.makeText( candidate_data.this, "Your data saved Sucessfully...", Toast.LENGTH_SHORT ).show( );

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        // ...
                                        progressDailog.dismiss();
                                        Toast.makeText(candidate_data.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDailog.setMessage(((int) progress) + "% Uploading...");

                            }
                        });
                    } else {
                        Toast.makeText(candidate_data.this, "Image not selected....", Toast.LENGTH_SHORT).show();
                    }

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

    // the below method is to get the extension of image
    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void Load_Post() {

        try {
            election obj = eleList.get(election_position);

            post1 = obj.getPost();

            for (Map.Entry<String, String> entry : post1.entrySet()) {
                String pname = entry.getKey();
                String pdisc = entry.getValue();
                post p = new post(pname, pdisc);
                postList.add(p);

            }

        } catch (ArrayIndexOutOfBoundsException e) {
            Toast.makeText(this, "please wait", Toast.LENGTH_SHORT).show();
        }

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

                try
                {
                    Bitmap bitmap = bundle.getParcelable( "data");
                    imageView.setImageBitmap(bitmap);

                }catch (Exception e)
                {
                    Toast.makeText(this, "image not selected", Toast.LENGTH_SHORT).show();
                }


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

