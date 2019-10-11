package com.example.abc.ballot;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class Upload_profile extends AppCompatActivity {
    ImageView imageView1;
    Button  btnchoose,btnupload;

    Uri uri;
    Intent GalIntent, CropIntent ;
    public  static final int RequestPermissionCode  = 1 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_upload_profile );

            imageView1 = findViewById(R.id.image_viewu );
            btnchoose = findViewById(R.id.BTNChooseImageu );

            EnableRuntimePermission();

            btnupload=findViewById(R.id.BTNUpload);
            btnupload.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Toast.makeText( Upload_profile.this, "YourProfile Uploded Sucessfully...", Toast.LENGTH_SHORT ).show( );
                }
            } );

            btnchoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    GetImageFromGallery();

                }
            });

        }
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

                    imageView1.setImageBitmap(bitmap);

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

            if (ActivityCompat.shouldShowRequestPermissionRationale( Upload_profile.this,
                                                                     Manifest.permission.CAMERA))
            {

                Toast.makeText(Upload_profile.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

            } else {

                ActivityCompat.requestPermissions(Upload_profile.this,new String[]{
                        Manifest.permission.CAMERA}, RequestPermissionCode);

            }
        }
}
