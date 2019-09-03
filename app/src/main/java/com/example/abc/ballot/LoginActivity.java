package com.example.abc.ballot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    Button btn_login,btn_registration,btn_temp;
    EditText input_email, input_password;
    View focusView = null;
    boolean status = true;
    String myemail,mypassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        btn_login = (Button) findViewById(R.id.email_sign_in_button);
        btn_registration = (Button) findViewById(R.id.registration_btn_loginpage);
        btn_temp = (Button) findViewById(R.id.temp);

        input_email = findViewById(R.id.email);
        input_password =  findViewById(R.id.password);


        myemail = input_email.getText().toString().trim();
        mypassword = input_password.getText().toString().trim();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputFields())
                {
                    Intent i = new Intent(getApplicationContext(),student_homepage.class);
                    startActivity(i);
                }
            }
        });

    }
    boolean validateInputFields()
    {
        input_email.setError(null);
        input_password.setError(null);
        status = true;
        if (!TextUtils.isEmpty(myemail)) {
            input_email.setError(getString(R.string.error_invalid_email));
            focusView = input_email;
            status = false;
        }
        if (!TextUtils.isEmpty(mypassword)) {
            input_password.setError(getString(R.string.error_invalid_password));
            focusView = input_password;
            status  = false;
        }
        return status;
    }

}
