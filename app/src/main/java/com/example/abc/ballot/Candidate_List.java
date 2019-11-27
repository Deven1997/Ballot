package com.example.abc.ballot;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.view.View.VISIBLE;


public class Candidate_List extends AppCompatActivity implements SelectCandidateDailog.SingleChoiceListner {

    String dept = null;
    String electionID = null;
    int post_position;
    int election_position;
    String uid;
    String pname;
    String edate,etime;


    List<candidate> candi_list = new ArrayList<>();
    List<Uri> uriList = new ArrayList<>();
    ViewPager mViewPager;

    RelativeLayout relativeLayout;

    DatabaseReference databaseReff,resultReff;

    ProgressBar mProgressBar;
    TextView TV,TV_voted_text;
    int mProgressStatus = 0;
    private Handler handler = new Handler();
    boolean loadingStatus =false;

    private LinearLayout mdotsLayout;
    private TextView[] mdots;
    Button votingButton;
    int candidate_possition;


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

    public void dotsIndicator(int size,int possition)
    {
        mdots = new TextView[size];
        mdotsLayout.removeAllViews();

        for(int i = 0;i<mdots.length;i++)
        {
            mdots[i] = new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226"));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.WhiteColor));

            mdotsLayout.addView(mdots[i]);
        }

        if(mdots.length>0)
        {
            mdots[possition].setTextColor(getResources().getColor(R.color.tablayoutindicatorcolor));
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate__list);

        votingButton = findViewById(R.id.voteButton_id);


        mdotsLayout = findViewById(R.id.dotsLayout);


        mProgressBar = findViewById(R.id.pbHeaderProgress);
        TV = findViewById(R.id.TV_id);
        TV_voted_text = findViewById(R.id.TV_status_id);

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
        electionID = getIntent().getExtras().getString( "electionID" );
        post_position = getIntent().getExtras().getInt("position");
        election_position = getIntent().getExtras().getInt("election_position");
        uid = getIntent().getExtras().getString("uid");
        pname = getIntent().getExtras().getString("postname");
        edate = getIntent().getExtras().getString("edate");
        etime = getIntent().getExtras().getString("etime");

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


                    dotsIndicator(candi_list.size(),0);

                    mViewPager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(candidate_possition);
                        }
                    },100);

                    mViewPager.setAdapter(sliderAdapter);

                    mProgressStatus = 100;
                    loadingStatus = true;
                    mProgressBar.setVisibility(View.GONE);
                    votingButton.setVisibility(VISIBLE);

                    try {
                        if(checktime(edate,etime))
                        {
                            for(candidate c: candi_list)
                            {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("result").child(electionID).child(pname).child(c.getUid());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for(DataSnapshot resultsnapshot:dataSnapshot.getChildren() )
                                        {
                                            result e=resultsnapshot.getValue(result.class);
                                            if(e.getUid().equals(uid))
                                            {
                                                votingButton.setVisibility(View.GONE);
                                                TV_voted_text.setVisibility(VISIBLE);
                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                               votingButton.setText("VOTE NOW");
                               votingButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        }
                    } catch (ParseException e) {
                        Toast.makeText(Candidate_List.this, "unable to compare date and time", Toast.LENGTH_SHORT).show();
                    }


                    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int i, float v, int i1) {

                        }

                        @Override
                        public void onPageSelected(int i) {
                            dotsIndicator(candi_list.size(),i);
                            candidate_possition = i;
                        }

                        @Override
                        public void onPageScrollStateChanged(int i) {

                        }
                    });




                }


            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

     votingButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             if(votingButton.getText().equals("VOTE NOW"))
             {
                 SelectCandidateDailog dailog1 = new SelectCandidateDailog();

                 candidate co = candi_list.get(candidate_possition);

                 String names[] = new String[candi_list.size()];

                 for(int i=0;i<candi_list.size();i++)
                 {
                     candidate c = candi_list.get(i);

                     names[i] = c.stud_name;
                 }

                 dailog1.setNames(names,pname);

                 dailog1.setCancelable(false);
                 dailog1.show(getSupportFragmentManager(),"Single Choice Dialog");

//                 String name = co.getStud_name();
//                 Toast.makeText(Candidate_List.this, "You have voted "+name, Toast.LENGTH_SHORT).show();
             }

             else
             {
                 Toast.makeText(Candidate_List.this, "please wait for a while..!", Toast.LENGTH_SHORT).show();
             }


         }
     });

    }

    boolean checktime(String date,String time) throws ParseException {

        Boolean status = false;
        //convert string date to Date
        Date date1 = new SimpleDateFormat("MM/dd/yy").parse(date);

        // fetch hours and minutes from time of election which is  of string type
        String hrs = time.substring(0,2);
        String mins = time.substring(5,7);

        //convert string mins and hours into Int type
        int ehrs = Integer.parseInt(hrs);
        int emins = Integer.parseInt(mins);

        //getting current date and time
        long millis=System.currentTimeMillis();
        java.sql.Date currentdate = new java.sql.Date(millis);

        //separate current date, month and year
        int cdate = currentdate.getDate();
        int cmonth = currentdate.getMonth()+1;
        int cyear = currentdate.getYear();


        Date currentTime = Calendar.getInstance().getTime();
        int chrs = currentTime.getHours();
        int cmins = currentTime.getMinutes();


        //separate election date, month and year
        int edate = date1.getDate();
        int emonth = date1.getMonth();
        int eyear = date1.getYear();

        if(cmonth>=emonth && cdate>=edate)
        {
            if(ehrs<=chrs && emins<= cmins)
            {
                status = true;
            }
        }

        Toast.makeText(this, "current date : "+cdate+"/"+cmonth+"/"+cyear, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "current time : "+chrs+" : "+cmins, Toast.LENGTH_SHORT).show();

        return status;

    }

    @Override
    public void onPositiveButtonClicked(String[] list, int position) {

        resultReff = FirebaseDatabase.getInstance().getReference().child("result").child(electionID).child(pname).child(candi_list.get(position).getUid());
        String key = resultReff.push().getKey();

        result r = new result(uid);
        resultReff.child(key).setValue(r);

        Toast.makeText(this, "You voted "+list[position], Toast.LENGTH_SHORT).show();

        Intent intent = new Intent( getApplicationContext(),Election_profile.class );
        intent.putExtra( "poss" , election_position);
        intent.putExtra( "mydept" ,dept);
        intent.putExtra("uid",uid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity( intent);


    }
}
