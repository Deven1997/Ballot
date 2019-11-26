package com.example.abc.ballot;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;


public class Candidate_List extends AppCompatActivity {

    String dept = null;
    String electionID = null;
    int post_position;
    int election_position;
    String uid;
    String pname;

    List<candidate> candi_list = new ArrayList<>();
    List<Uri> uriList = new ArrayList<>();
    ViewPager mViewPager;

    RelativeLayout relativeLayout;

    DatabaseReference databaseReff,databaseReff2;

    ProgressBar mProgressBar;
    TextView TV;
    int mProgressStatus = 0;
    private Handler handler = new Handler();
    boolean loadingStatus =false;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.apply,menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.apply_for_post_ID)
        {
            Intent intent = new Intent(getApplicationContext(),candidate_data.class);
            intent.putExtra( "dept",dept );
            intent.putExtra( "electionID",electionID );
            intent.putExtra( "position",post_position );
            intent.putExtra("election_position",election_position);
            intent.putExtra("uid",uid);
            startActivity(intent);
        }
        return super.onOptionsItemSelected( item );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate__list);

        mProgressBar = findViewById(R.id.pbHeaderProgress);
        TV = findViewById(R.id.TV_id);

        mProgressBar.getProgressDrawable().setColorFilter(
                Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mProgressStatus<100)
                {
                    mProgressStatus++;
                    android.os.SystemClock.sleep(50);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(mProgressStatus);

                        }
                    });
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!loadingStatus)
                            TV.setVisibility(VISIBLE);
                    }
                });
            }
        }).start();

        dept = getIntent().getExtras().getString( "dept" );
        Toast.makeText(this, dept, Toast.LENGTH_SHORT).show();

        electionID = getIntent().getExtras().getString( "electionID" );
        Toast.makeText(this, electionID, Toast.LENGTH_SHORT).show();

        post_position = getIntent().getExtras().getInt("position");
        Toast.makeText(this, " "+post_position, Toast.LENGTH_SHORT).show();

        election_position = getIntent().getExtras().getInt("election_position");
        Toast.makeText(this, " "+election_position, Toast.LENGTH_SHORT).show();

        uid = getIntent().getExtras().getString("uid");
        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();

        pname = getIntent().getExtras().getString("postname");


        mViewPager = findViewById(R.id.Viewpager_id);

        relativeLayout = findViewById(R.id.relative_id);



        final SliderAdapter sliderAdapter = new SliderAdapter(this);
        databaseReff = FirebaseDatabase.getInstance().getReference().child("candidate").child(electionID).child(pname);
        databaseReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot candi : dataSnapshot.getChildren())
                {
                    candidate c = candi.getValue(candidate.class);
                    candi_list.add(c);

                }


                for(final candidate cobj: candi_list) {
                    StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("candidates/" + cobj.getUid() + ".jpg");
                    try {
                        final File file = File.createTempFile("image", "jpg");
                        imageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Uri uris = Uri.fromFile(file);

                                sliderAdapter.setData(uris,cobj.getStud_name(),cobj.getUid(),cobj.getCandidate_disc());


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Candidate_List.this, "Image failed to Load", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {

                    }

                }

                if(candi_list.size()>0) {
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(Candidate_List.this, R.drawable.election_bg) );
                    } else {
                        relativeLayout.setBackground(ContextCompat.getDrawable(Candidate_List.this, R.drawable.election_bg));
                    }



                    mViewPager.setAdapter(sliderAdapter);

                    mProgressStatus = 100;
                    loadingStatus = true;
                    mProgressBar.setVisibility(View.GONE);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
