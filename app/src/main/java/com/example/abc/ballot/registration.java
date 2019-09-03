package com.example.abc.ballot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView goto_loginpage_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        openDialog();

        goto_loginpage_txt = findViewById(R.id.link_login1);
       /* goto_loginpage_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        }); */

        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.departments, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void openDialog()
    {
        Registration_Dialogbox dialogbox = new Registration_Dialogbox();
        dialogbox.show(getSupportFragmentManager(),"Example Dialog");
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected_department = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "You have selected "+selected_department+" department", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
