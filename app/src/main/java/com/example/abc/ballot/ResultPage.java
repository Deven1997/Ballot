package com.example.abc.ballot;


import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ResultPage extends AppCompatActivity {

    String dept,ucid,election_position,election_ID;

    DatabaseReference eleReff,candiReff;
    election election_obj;
    TextView TV_EleTitle;

    HashMap<String,String> postList = new HashMap<>();
    List<String> postNames = new ArrayList<>(  );
    List<candidate> cand_list = new ArrayList<>(  );
    List<result> resultslist = new ArrayList<>();
    List<String> temp = new ArrayList<>();



    int abc;
    LinearLayout linearLayout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);


        dept = getIntent().getExtras().getString( "dept" );
        // ucid = getIntent().getExtras().getString( "uid" );
        election_position = getIntent().getExtras().getString( "poss" );
        election_ID = getIntent().getExtras().getString( "election_ID" );

        //  Toast.makeText( this, dept+" "+ucid+" "+election_ID, Toast.LENGTH_SHORT ).show( );

        TV_EleTitle = findViewById( R.id.Election_title_id );

        linearLayout = findViewById( R.id.result_linear_layout_id );


        eleReff = FirebaseDatabase.getInstance().getReference().child( "department" ).child( "election" ).child( dept ).child( election_ID );

        eleReff.addValueEventListener( new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                election_obj = dataSnapshot.getValue(election.class);
                postList = election_obj.getPost();

                TV_EleTitle.setText( election_obj.getElection_name());

                for (Map.Entry<String, String> entry : postList.entrySet( )) {
                    String pname = entry.getKey( );
                    String pdisc = entry.getValue( );
                    post p = new post( pname, pdisc );
                    postNames.add( p.getPname( ) );
                }


                for(final String pname:postNames)
                {
                    final TextView postTextView = new TextView(ResultPage.this);
                    postTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    postTextView.setTextSize(25);
                    postTextView.setGravity(Gravity.CENTER);
                    postTextView.setTypeface(Typeface.DEFAULT_BOLD);
                    postTextView.setText(pname);
                    linearLayout.addView(postTextView);

                    getData(pname);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }




    void getData(final String pname)
    {

            candiReff = FirebaseDatabase.getInstance().getReference().child( "candidate" ).child( election_ID );
            candiReff.addValueEventListener( new ValueEventListener( ) {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild(pname))
                    {
                       // Toast.makeText( ResultPage.this, pname+" Matched", Toast.LENGTH_SHORT ).show( );
                        DatabaseReference candiReff2 = FirebaseDatabase.getInstance().getReference().child( "candidate" ).child( election_ID ).child( pname );
                        candiReff2.addValueEventListener( new ValueEventListener( ) {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                cand_list.clear();



                               // Toast.makeText( ResultPage.this, pname+" added", Toast.LENGTH_SHORT ).show( );
                                for(DataSnapshot candi:dataSnapshot.getChildren())
                                {



                                    candidate c = candi.getValue(candidate.class);
                                    final String cadnameee = c.getStud_name();


                                    DatabaseReference resultReff = FirebaseDatabase.getInstance().getReference().child( "result" ).child( election_ID ).child( c.getPname() ).child( c.getUid() );
                                    resultReff.addValueEventListener( new ValueEventListener( ) {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            //abc = (int) dataSnapshot.getChildrenCount();
                                            resultslist.clear();
                                            for(DataSnapshot db : dataSnapshot.getChildren())
                                            {
                                                result r = db.getValue(result.class);
                                                resultslist.add(r);
                                            }


                                            final TextView nameTextView = new TextView(ResultPage.this);
                                            nameTextView.setTextColor( getResources().getColor( R.color.BlackColor) );
                                            nameTextView.setTextSize( 15 );
                                            nameTextView.setGravity( Gravity.LEFT );

                                            nameTextView.setText( cadnameee + "   " +resultslist.size() );

                                            temp.add(cadnameee + "   " +resultslist.size());

                                            linearLayout.addView( nameTextView );
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    } );

                                }

                                temp.add("abc");

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        } );

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );

    }

}