package com.example.abc.ballot;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



        postListView.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {




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
        } );

        postreff =  FirebaseDatabase.getInstance().getReference().child( "department").child( "election").child( dept);
        postreff.addValueEventListener( new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
}
