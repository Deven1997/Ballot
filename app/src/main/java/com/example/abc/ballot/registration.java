package com.example.abc.ballot;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView goto_loginpage_txt;
    Button btn_submit;
    EditText input_name,input_contact,input_ucid,input_password,input_cpassword;
    Spinner input_department;
    String selected_department;
    View focusView=null;

    DatabaseReference databasestudent;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("student");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        openDialog();

        input_name = findViewById(R.id.edit_name_id);
        input_contact = findViewById(R.id.enter_contact);
        input_ucid = findViewById(R.id.enter_UCID);
        input_password = findViewById(R.id.set_password);
        input_cpassword = findViewById(R.id.confirm_password);

        databasestudent = FirebaseDatabase.getInstance().getReference("students");

        goto_loginpage_txt = findViewById(R.id.link_login1);
        goto_loginpage_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.departments, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        btn_submit = findViewById(R.id.btn_signup);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AddStudent();

            }
        });
    }

    public void openDialog()
    {
        Registration_Dialogbox dialogbox = new Registration_Dialogbox();
        dialogbox.show(getSupportFragmentManager(),"Example Dialog");
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_department = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "You have selected "+selected_department+" department", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    void AddStudent()
    {
        String name = input_name.getText().toString().trim();
        String contact = input_contact.getText().toString();
        final String ucid = input_ucid.getText().toString();
        String pass = input_password.getText().toString().trim();
        String cpass = input_cpassword.getText().toString().trim();
        String department = selected_department;
        input_name.setError(null);
        input_password.setError(null);

        boolean status = true;
        if(TextUtils.isEmpty(name))
        {
            input_name.setError("Required!");
            focusView = input_name;
            status = false;
        }
        if(!TextUtils.isEmpty(contact))
        {
            if(contact.length()!=10)
            {
                Toast.makeText(this,"Invalid Contact",Toast.LENGTH_LONG).show();
                status = false;
            }

        }
        else
        {
            input_contact.setError("Required!");
            focusView = input_contact;
            status = false;
        }
        if(!TextUtils.isEmpty(ucid))
        {
            if(contact.length()!=10)
            {
                Toast.makeText(this,"Invalid UCID",Toast.LENGTH_LONG).show();
                status = false;
            }
        }
        else
        {
            input_ucid.setError("Required!");
            focusView = input_ucid;

            status = false;
        }
        if(TextUtils.isEmpty(pass))
        {
            input_password.setError("Required!");
            focusView = input_password;
            status = false;
        }

        if(!TextUtils.isEmpty(cpass))
        {
            if (!pass.equals(cpass))
            {
                Toast.makeText(this,"Password and Confirm Password are not same",Toast.LENGTH_LONG).show();
                status = false;
            }

        }
        else
        {
            input_cpassword.setError("Required!");
            focusView = input_cpassword;
            status = false;
        }
        if(status==true)
        {
           /* databasestudent.push();

            Student student = new Student(name,contact,ucid,department,pass);

            databasestudent.child(ucid).setValue(student);
            Toast.makeText(this,"Registration Successful..",Toast.LENGTH_LONG).show(); */

            final Student student = new Student(name,contact,ucid,department,pass);
            Query query = ref.child("student").orderByChild(ucid).equalTo(ucid);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(registration.this,"Sorry...Contact Number already exist",Toast.LENGTH_SHORT).show();
                        return;
                    }else
                    {


                        ref.child(ucid).setValue(student);

                        //   ref.push().child(contact).setValue(info);
                        Toast.makeText(registration.this, "Information Saved...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}

