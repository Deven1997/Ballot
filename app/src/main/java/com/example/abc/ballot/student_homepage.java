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

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    Button add_description_btn;
    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    ImageView profilePhoto;
    NavigationView navigationView;
    ListView ElectionlistView;
    String uid,mydept;
    List<election> elelist;

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

        /* setting name and ucid to header of navigation bar */


        mDrawerLayout = findViewById(R.id.drawer_layout_id);

        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

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
        ElectionlistView=findViewById(R.id.ListView1);
        elelist=new ArrayList<>();
        student_db_reff= FirebaseDatabase.getInstance().getReference().child("department").child("election").child(mydept);

        ElectionlistView.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText( student_homepage.this, "position"+i, Toast.LENGTH_SHORT ).show( );

//                Intent intent = new Intent( getApplicationContext(),Election_profile.class );
//
//                startActivity( intent);

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
                Intent i=new Intent( getApplicationContext(),Upload_profile.class );
                startActivity( i );

                break;
            case R.id.changepass_id:
                Toast.makeText(this, "change password", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),change_password.class);
                intent.putExtra("uid1",uid);
                startActivity(intent);
                break;
            case  R.id.resultmenu_id:
                Toast.makeText(this, "Result", Toast.LENGTH_SHORT).show();
                Intent i1=new Intent(getApplicationContext(),ResultPage.class);
                startActivity(i1);
                break;
            case R.id.logoutmenu_id:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();

                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
