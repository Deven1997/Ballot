package com.example.abc.ballot;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class change_password extends AppCompatActivity {

    Button button;
    EditText oldpass;
    EditText newpass1;
    EditText conpass2;
    String dbpass;
    Student obj;

    DatabaseReference change_pass_reff;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_change_password );

        oldpass=findViewById(R.id.ETOldPassword);
        newpass1=findViewById( R.id.ETSetPassword);
        conpass2=findViewById( R.id.ETConfirmPassword);


        final String uid=getIntent().getExtras().getString("uid1");


        button=findViewById(R.id.BTNSave);
        button.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {

                change_pass_reff = FirebaseDatabase.getInstance().getReference().child( "students").child( uid);

                change_pass_reff.addValueEventListener( new ValueEventListener( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dbpass = dataSnapshot.child("password").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
                String old = oldpass.getText().toString().trim();
                if(old.equals(dbpass))
                {
                    String newp = newpass1.getText().toString().trim();
                    String newcp = conpass2.getText().toString().trim();

                    if(newp.equals(newcp))
                    {
                        change_pass_reff.child("password").setValue(newpass1.getText().toString().trim());

                        Toast.makeText( change_password.this, "Password Changed", Toast.LENGTH_SHORT ).show();
                        Intent i=new Intent( getApplicationContext(), LoginActivity .class);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText( change_password.this, "New and Confirm passwords not matched..please enter proper password", Toast.LENGTH_SHORT ).show( );
                    }
                }
                else{
                    Toast.makeText( change_password.this, "Wrong Old Password.....", Toast.LENGTH_SHORT ).show( );
                }

            }
        } );

    }
}
