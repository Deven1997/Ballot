package com.example.abc.ballot;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class student_homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public  static final int RequestPermissionCode  = 1 ;
    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    ImageView profilePhoto;
    NavigationView navigationView;
    ListView ElectionlistView;
    String uid,mydept;
    List<election> elelist;
    election obj;
    Uri uri;
    Intent GalIntent, CropIntent;

    DatabaseReference student_db_reff, getStudent_db_reff2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homepage);

        Toolbar toolbar = findViewById(R.id.stud_homepage_toolbar_id);
        setSupportActionBar(toolbar);

        /* get values from login activity*/
        String name = getIntent().getExtras().getString("name");
        uid = getIntent().getExtras().getString("uid");
        mydept = getIntent().getExtras().getString("dept");


        mDrawerLayout = findViewById(R.id.drawer_layout_id);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        /* setting name and ucid to header of navigation bar */
        navigationView = findViewById(R.id.nav1_id);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.myname_id);
        TextView navUCID = headerView.findViewById(R.id.myuid_id);
        profilePhoto = headerView.findViewById(R.id.profilePhoto);

        navUsername.setText(name);
        navUCID.setText(uid);

        navigationView.setNavigationItemSelectedListener(this);

        ElectionlistView=findViewById(R.id.ListView1);
        elelist=new ArrayList<>();
        student_db_reff= FirebaseDatabase.getInstance().getReference().child("department").child("election").child(mydept);

        ElectionlistView.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                Toast.makeText( student_homepage.this, "position"+i, Toast.LENGTH_SHORT ).show( );

                obj = elelist.get( i );
                //Toast.makeText( student_homepage.this, obj.getElection_name(), Toast.LENGTH_SHORT ).show( );
                Intent intent = new Intent( getApplicationContext(),Election_profile.class );
                intent.putExtra( "poss" , i);
                intent.putExtra( "mydept" ,mydept);
                intent.putExtra("uid",uid);

                startActivity( intent);

            }
        } );

    }


    @Override
    protected void onStart() {
        super.onStart( );
        student_db_reff.addValueEventListener( new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                elelist.clear();
                for(DataSnapshot electsnap: dataSnapshot.getChildren()){
                    election e=electsnap.getValue(election.class);
                    elelist.add(e);
                    election_listview_adapter adapter=new election_listview_adapter(student_homepage.this,elelist);
                    ElectionlistView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_id);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.uploadImage_id:
                Toast.makeText(this, "Upload your profile ", Toast.LENGTH_SHORT).show();
                profilePhoto.setOnClickListener( new View.OnClickListener( ) {
                    @Override
                    public void onClick(View view) {
                        profilePhoto=view.findViewById( R.id.profilePhoto);
                        GetImageFromGallery();
                    }
                } );

                break;
            case R.id.changepass_id:
                Toast.makeText(this, "change password", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),change_password.class);
                intent.putExtra("uid1",uid);
                startActivity(intent);
                break;
            case  R.id.resultmenu_id:
                Toast.makeText(this, "Result", Toast.LENGTH_SHORT).show();
                Intent i1=new Intent(getApplicationContext(),result_election_list.class);
                i1.putExtra("dept",mydept);
                startActivity(i1);
                break;
            case R.id.logoutmenu_id:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();

                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);


        return true;
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

                profilePhoto.setImageBitmap(bitmap);

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

        if (ActivityCompat.shouldShowRequestPermissionRationale( student_homepage.this,
                                                                 Manifest.permission.CAMERA))
        {

            Toast.makeText(student_homepage.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(student_homepage.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }
}
