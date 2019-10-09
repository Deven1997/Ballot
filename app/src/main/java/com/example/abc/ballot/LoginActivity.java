package com.example.abc.ballot;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
    EditText input_id, input_pass;

    String myid,mypassword;
    String myname,myuid;
    String abcdef;
    // Hello github

    ProgressDialog progress;

    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        btn_login = findViewById(R.id.email_sign_in_button);
        btn_registration = findViewById(R.id.registration_btn_loginpage);


        progress = new ProgressDialog(this);
        progress.setTitle("Authentication..!!");
        progress.setMessage("Please Wait!!");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);



        input_id = findViewById(R.id.user_id);
        input_pass =  findViewById(R.id.password_id);
        if (!amIConnected())
        {
            displayInternetConnectivityToast();
        }
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!amIConnected())
                    {
                        displayInternetConnectivityToast();
                    }
                    else {
                        progress.show();
                    if (validateFields()) {
                        reff = FirebaseDatabase.getInstance().getReference().child("students");
                        myid = input_id.getText().toString().trim();
                        mypassword = input_pass.getText().toString().trim();


                        reff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnap) {
                                if (dataSnap.hasChild(myid)) {
                                    Student st = dataSnap.child(myid).getValue(Student.class);
                                    myname =  st.getName();
                                    myuid = st.getUcid();
                                    String mydept = st.getDepartment();

                                    if (st.getPassword().equals(mypassword)) {
                                        progress.cancel();
                                        Toast.makeText(LoginActivity.this, "Login Successful..", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), student_homepage.class);
                                        /* sending values to student homepage activity */
                                        i.putExtra("name",myname);
                                        i.putExtra("uid",myuid);
                                        i.putExtra("dept",mydept);
                                        startActivity(i);
                                    } else {
                                        progress.cancel();
                                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                        input_pass.setHintTextColor(getResources().getColor(R.color.RedColor));
                                    }
                                } else {
                                    progress.cancel();
                                    Toast.makeText(LoginActivity.this, "User not found!..", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
            });

        btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), registration.class);
                startActivity(i);
            }
        });

    }
    boolean validateFields()
    {
       String  tempid = input_id.getText().toString().trim();
       String temppass = input_pass.getText().toString().trim();
        if(TextUtils.isEmpty(tempid))
        {
            input_id.setError("Required!");
            return false;
        }else
        if(TextUtils.isEmpty(temppass))
        {
            input_pass.setError("Required!");
            return false;
        }
        return true;
    }
    private boolean amIConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activNetworkInfo !=null && activNetworkInfo.isConnected();
    }

    void displayInternetConnectivityToast()
    {
        Toast.makeText(this, "You are offline!, Check Internet Connection", Toast.LENGTH_SHORT).show();
    }



}
