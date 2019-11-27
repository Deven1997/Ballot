package com.example.abc.ballot;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class result_election_list extends AppCompatActivity {
    ListView ElectionListView;
    List<election> elelist2;
    election obj2;
    String dept,uid;

    DatabaseReference elections_reff;

        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_result_election_list );
        dept=getIntent().getExtras().getString("dept");
        uid=getIntent().getExtras().getString( "uid" );

        ElectionListView  = findViewById( R.id.ListView2 );
        elelist2=new ArrayList<>();
        ElectionListView.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText( result_election_list.this, "position"+i, Toast.LENGTH_SHORT ).show( );

                obj2 = elelist2.get( i );
                //Toast.makeText( student_homepage.this, obj.getElection_name(), Toast.LENGTH_SHORT ).show( );
                Intent intent = new Intent( getApplicationContext(), ResultPage.class );
                intent.putExtra( "poss" , i);
                intent.putExtra( "dept" ,dept);
                intent.putExtra("uid",uid);
                intent.putExtra( "election_ID",elelist2.get( i ).getElection_id() );

                startActivity( intent);
            }
        } );
        elections_reff= FirebaseDatabase.getInstance().getReference().child( "department" ).child( "election" ).child( dept );
        elections_reff.addValueEventListener( new ValueEventListener( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                elelist2.clear();
                for(DataSnapshot electsnap: dataSnapshot.getChildren()){
                    election e=electsnap.getValue(election.class);
                    elelist2.add(e);
                    election_listview_adapter adapter=new election_listview_adapter(result_election_list.this,elelist2);
                    ElectionListView.setAdapter(adapter);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
