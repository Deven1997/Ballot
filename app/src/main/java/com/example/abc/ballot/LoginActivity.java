package com.example.abc.ballot;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button btn_login,btn_registration;
    EditText input_id, input_password;
    View focusView = null;
    boolean status = true;
    String myid,mypassword;
    String temppassword;

    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        btn_login = (Button) findViewById(R.id.email_sign_in_button);
        btn_registration = (Button) findViewById(R.id.registration_btn_loginpage);


        input_id = findViewById(R.id.user_id);
        input_password =  findViewById(R.id.password);


        myid = input_id.getText().toString().trim();
        mypassword = input_password.getText().toString().trim();
        reff = FirebaseDatabase.getInstance().getReference().child("students");
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Toast.makeText(LoginActivity.this, "data validate successfully", Toast.LENGTH_SHORT).show();
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(myid))
                            {

                                reff = FirebaseDatabase.getInstance().getReference().child("students").child(myid);

                                reff.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        temppassword = dataSnapshot.child("password").getValue().toString();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                if (temppassword==mypassword)
                                {
                                    Log.i("comparing password","comparing password");
                                    Toast.makeText(LoginActivity.this, "Login Successful..", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(),student_homepage.class);
                                    startActivity(i);
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "User not found!..", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


            }
        });

        btn_registration.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),registration.class);
                startActivity(i);
            }
        });



    }


}
