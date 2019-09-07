package com.example.abc.ballot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class student_homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button add_description_btn;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    ImageView profilePhoto;
    NavigationView navigationView;


    private static final int IMAGE_PICK_CODE=1000;
    private static final int PERMISSION_CODE=1001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homepage);

        /* get values from login activity*/
        String name = getIntent().getExtras().getString("name");
        String uid = getIntent().getExtras().getString("uid");

        /* setting name and ucid to header of navigation bar */


        mDrawerLayout = findViewById(R.id.drawer_layout_id);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav1_id);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.myname_id);
        TextView navUCID = headerView.findViewById(R.id.myuid_id);
        profilePhoto = headerView.findViewById(R.id.profilePhoto);

        navUsername.setText(name);
        navUCID.setText(uid);

        navigationView.setNavigationItemSelectedListener(this);


        add_description_btn = findViewById(R.id.BTN_adddescription);
        add_description_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),candidate_data.class);
                startActivity(i);
            }
        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
        {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.upoloadImage_id)
        {
            Toast.makeText(this, "Upload image from Gallery", Toast.LENGTH_SHORT).show();
        }
        else if (id==R.id.changepass_id)
        {
            Toast.makeText(this, "Change Password", Toast.LENGTH_SHORT).show();
        }
        else if (id==R.id.resultmenu_id)
        {
            Toast.makeText(this, "Result manu", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.logoutmenu_id)
        {
            Toast.makeText(this, "Logout Menu", Toast.LENGTH_SHORT).show();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void uploadImage()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //permission not granted,request it
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //show popup for runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                //permission already granted
                pickImageFromGallery();
            }
        } else {
            //System os is less than marshmallow
            pickImageFromGallery();
        }
    }

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);

    }

    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    pickImageFromGallery();
                }
                else{
                    //permission was denied
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode==IMAGE_PICK_CODE){
            //set image to image View
            profilePhoto.setImageURI(data.getData());
        }
    }
}
