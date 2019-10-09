package com.example.abc.ballot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLogin extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;

    ProgressDialog progress;

    TextView textView;
    Button submitButton;

    String dept_checked = "None"; // string of checked department

    EditText input_dept_pass;
    String dept_pass; // string of edittext password

    DatabaseReference admin_db_reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        radioGroup = findViewById(R.id.radiogroup_id);
        textView = findViewById(R.id.text_id);

        input_dept_pass = findViewById(R.id.adminPassword);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                dept_checked = onRadioButtonClicked(checkedId);
            }
        });



        progress = new ProgressDialog(this);
        progress.setTitle("Authentication..!!");
        progress.setMessage("Please Wait!!");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        submitButton = findViewById(R.id.submit_btn);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String deptPass = input_dept_pass.getText().toString().trim();

                if(dept_checked.equals("None") || deptPass.isEmpty())
                {
                    Toast.makeText(AdminLogin.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progress.show();

                    admin_db_reff = FirebaseDatabase.getInstance().getReference().child("department");

                    admin_db_reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            department d = dataSnapshot.child("password").getValue(department.class);

                            if(validPass(dept_checked.toLowerCase(),deptPass,d))
                            {

                                progress.cancel();
                                //Toast.makeText(AdminLogin.this, "Login Successfully..", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),Admin_homepage.class);
                                intent.putExtra("dept_n",dept_checked);
                                startActivity(intent);
                            }
                            else
                            {
                                progress.cancel();
                                Toast.makeText(AdminLogin.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

    }

    public boolean validPass(String dept1,String deptPass,department obj)
    {
        String mcapass = obj.getMca();
        String itpass = obj.getIt();
        String extcpass = obj.getExtc();
        String etrxpass = obj.getEtrx();
        String compspass = obj.getComps();

        if(dept1.equals("mca") && deptPass.equals(mcapass))
            return true;
        else if(dept1.equals("it")  && deptPass.equals(itpass))
            return true;
        else if(dept1.equals("extc") && deptPass.equals(extcpass))
            return true;
        else if(dept1.equals("etrx")  && deptPass.equals(etrxpass))
            return true;
        else if(dept1.equals("comps") && deptPass.equals(compspass))
            return true;
        else
            return false;
    }

    public String onRadioButtonClicked(int checked)
    {
        radioButton = findViewById(checked);
        textView.setText("Please Enter "+radioButton.getText().toString().trim()+" Department Password");
        return radioButton.getText().toString().trim();
    }

}
