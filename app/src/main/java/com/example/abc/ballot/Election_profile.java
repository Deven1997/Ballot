package com.example.abc.ballot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Election_profile extends AppCompatActivity {

    ListView postListView;
    List<post> postList = new ArrayList<>();
    List<election> eleList = new ArrayList<>(  );

    HashMap<String,String> post1 = new HashMap<>();

    election obj;
    String dept,electionID;
    int possion;
    String uid;
    String edate,etime;


    TextView eleTitle,TVdatetime;

    DatabaseReference postreff;
    Button adminResultBtn;

    TextView counterTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_election_profile );

        possion = getIntent().getExtras().getInt( "poss" );
        dept = getIntent().getExtras().getString( "mydept" );
        uid = getIntent().getExtras().getString("uid");


        eleTitle = findViewById( R.id.ele_title_id );

        postListView = findViewById( R.id.post_listview_id );

        TVdatetime = findViewById(R.id.TV_datetime_id);

        adminResultBtn = findViewById(R.id.adminResultButton_id);

        counterTV = findViewById(R.id.TV_countDown_id);



        adminResultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Election_profile.this,ResultPage.class );
                intent.putExtra("election_ID",electionID);
                intent.putExtra("poss",possion);
                intent.putExtra("dept",dept);
                startActivity( intent);
            }
        });


        postListView.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!uid.equals("123"))
                {
                    Intent intent = new Intent( getApplicationContext(), Candidate_List.class);
                    intent.putExtra( "dept",dept );
                    intent.putExtra( "electionID",electionID );
                    intent.putExtra( "position",i);
                    intent.putExtra("election_position",possion);
                    intent.putExtra("uid",uid);
                    intent.putExtra("postname",postList.get(i).getPname());
                    intent.putExtra("edate",edate);
                    intent.putExtra("etime",etime);
                    startActivity(intent);
                }
                else
                {


                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(Election_profile.this);
                    builderSingle.setTitle("Candidates for "+postList.get(i).getPname());

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Election_profile.this, android.R.layout.select_dialog_singlechoice);

                    DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("candidate").child(eleList.get(possion).getElection_id()).child(postList.get(i).getPname());
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot db : dataSnapshot.getChildren())
                            {
                                candidate ca = db.getValue(candidate.class);
                                arrayAdapter.add(ca.getStud_name());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                    builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            String strName = arrayAdapter.getItem(which);
//                            AlertDialog.Builder builderInner = new AlertDialog.Builder(Election_profile.this);
//                            builderInner.setMessage(strName);
//                            builderInner.setTitle("Your Selected Item is");
//                            builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog,int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                            builderInner.show();


                        }


                    });
                    builderSingle.show();
                }



            }
        } );

        postreff =  FirebaseDatabase.getInstance().getReference().child( "department").child( "election").child( dept);
        postreff.addValueEventListener( new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(uid.equals("123"))
                {
                    adminResultBtn.setVisibility(View.VISIBLE);
                }

                try
                {

                    new CountDownTimer(checktime(edate, etime), 1000) {

                        public void onTick(long millisUntilFinished) {
                            counterTV.setText(String.format(Locale.getDefault(), "Election Starts in %02d min: %02d sec",
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60));
                        }

                        public void onFinish() {
                            counterTV.setText("done!");
                        }
                    }.start();

                }catch (Exception e)
                {

                }

                postList.clear();
                for(DataSnapshot electsnap: dataSnapshot.getChildren()){
                    election e = electsnap.getValue(election.class);
                    eleList.add(e);
                }

                setPost();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }

    private void setPost() {

        try {
            election obj = eleList.get( possion );

            edate = obj.getEdate();
            etime = obj.getEtime();

            TVdatetime.setText("Election Date: "+obj.getEdate()+"       Time: "+obj.getEtime());

            post1 = obj.getPost();
            electionID = obj.getElection_id();

            eleTitle.setText( obj.getElection_name() );

            for (Map.Entry<String, String> entry : post1.entrySet( )) {
                String pname = entry.getKey( );
                String pdisc = entry.getValue( );
                post p = new post( pname, pdisc );
                postList.add( p );
                post_list_adapter adapter = new post_list_adapter( Election_profile.this, postList );
                postListView.setAdapter( adapter );
            }

        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            Toast.makeText( this, "please wait", Toast.LENGTH_SHORT ).show( );
        }


    }
    
    int checktime(String date,String time) throws ParseException {

        int remainingTIME=0;
        
        Date date1 = new SimpleDateFormat("MM/dd/yy").parse(date);
        //separate election date, month and year
        int edate = date1.getDate();
        int emonth = date1.getMonth();
        int eyear = date1.getYear();

        //getting current date and time
        long millis=System.currentTimeMillis();
        java.sql.Date currentdate = new java.sql.Date(millis);

        //separate current date, month and year
        int cdate = currentdate.getDate();
        int cmonth = currentdate.getMonth()+1;
        int cyear = currentdate.getYear();
        
        if(cdate>edate)
        {
            remainingTIME=0;
        }
        else
        {
            String hrs = time.substring(0,2);
            String mins = time.substring(5,7);

            //convert string mins and hours into Int type
            int ehrs = Integer.parseInt(hrs);
            int emins = Integer.parseInt(mins);

            Date currentTime = Calendar.getInstance().getTime();
            int chrs = currentTime.getHours();
            int cmins = currentTime.getMinutes();

            int remainHrs = ehrs-chrs;
            if(remainHrs<0)
                remainHrs =0;

            int remainMins;

            if(emins>cmins)
                remainMins = emins-cmins;
            else if(emins<cmins)
                remainMins = cmins-emins;
            else
                remainMins = 0;
            
            remainingTIME = (remainHrs*60000)+(remainMins*1000);
        }
        
        
            
        
        return remainingTIME;

    }
}
