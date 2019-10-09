package com.example.abc.ballot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddPost extends AppCompatActivity {

    Button addButton;
    EditText post_name,post_discription;
    TextView postlist_textView,election_name;


    String dept_name1;
    String name,class_name,from_uid,to_uid;


    HashMap<String,String> map = new HashMap<>();

    LinearLayout linearLayout,linearLayout2;
    int i = 0;


    DatabaseReference election_reff;
    DatabaseReference get_deptname_reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);



        post_name = findViewById(R.id.edit_postname_id);
        post_discription = findViewById(R.id.edit_description_id);
        postlist_textView = findViewById(R.id.postlist_textView_id);

        election_name = findViewById(R.id.textview_title_id);

        name = getIntent().getExtras().getString("title");
        class_name = getIntent().getExtras().getString("class_name");
        from_uid = getIntent().getExtras().getString("from");
        to_uid = getIntent().getExtras().getString("to");
        dept_name1 = getIntent().getExtras().getString("dept_name");

        election_name.setText(name);

        linearLayout = findViewById(R.id.addpost_linear_layout_id);
        linearLayout2 = findViewById(R.id.addpost_linear_layout_id_2);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

       // list = getResources().getStringArray(R.array.post_array);

        addButton = findViewById(R.id.add_button_id);

        final Button submit_button = new Button(AddPost.this);





      //  final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView rowTextView = new TextView(AddPost.this);

                String pname = post_name.getText().toString().trim();
                String pdisc = post_discription.getText().toString().trim();


                if(pname.trim().length() == 0 )
                {
                    Toast.makeText(AddPost.this, "Please add post name", Toast.LENGTH_SHORT).show();
                }
                else if(pdisc.trim().length() == 0)
                {
                    Toast.makeText(AddPost.this, "Please add Discription ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    boolean flag = true;
                    for(String str : map.keySet())
                    {
                        if(str.toLowerCase().equals(pname.toLowerCase()))
                        {
                            Toast.makeText(AddPost.this, "Sorry!.. This post is already added in the list.", Toast.LENGTH_SHORT).show();
                            flag = false;
                        }
                    }

                    if(flag)
                    {

                        map.put(pname,pdisc);

                        i++;

                        rowTextView.setText(i+" ) "+pname);
                        rowTextView.setTextColor(getResources().getColor(R.color.BlackColor));
                        rowTextView.setTextSize(20);

                        linearLayout.addView(rowTextView);


                        if(i==1)
                        {
                            submit_button.setText("Submit");
                            submit_button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            layoutParams.gravity = Gravity.BOTTOM;
                            layoutParams.gravity = Gravity.RIGHT;
                            layoutParams.rightMargin = 8;
                            layoutParams.bottomMargin = 5;
                            linearLayout2.addView(submit_button,layoutParams);
                        }

                        Toast.makeText(AddPost.this, "Post added Successfully...", Toast.LENGTH_SHORT).show();
                        post_name.setText(null);
                        post_discription.setText(null);
                        submit_button.requestFocus();

                    }

                }
            }
        });


        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                election elect = new election(class_name,name,from_uid,to_uid,map);


                    election_reff = FirebaseDatabase.getInstance().getReference().child("department").child("election").child(dept_name1);

                    String id = election_reff.push().getKey();

                    election_reff.child(id).setValue(elect);

                    Toast.makeText(AddPost.this, "Election added Successfully..", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getApplicationContext(),Admin_homepage.class);
                    i.putExtra("dept_n",dept_name1);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
            }
        });
    }

}
