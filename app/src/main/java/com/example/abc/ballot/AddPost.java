package com.example.abc.ballot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
    TextView TV_maxCandidate;
    SeekBar seekBar;

    int maxCand = 2;

    String dept_name1;
    String name,class_name,from_uid,to_uid;
    String E_date;
    String E_time;


    HashMap<String,String> map = new HashMap<>();

    LinearLayout linearLayout,linearLayout2;
    int inc = 0;


    DatabaseReference election_reff;
    DatabaseReference get_deptname_reff;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.action_bar_menu,menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.checkbtn)
        {
            submitdata();
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        // The bellow code is for seekbar
        TV_maxCandidate = findViewById( R.id.tv_max_candidate );
        seekBar  = findViewById( R.id.seekbar_addpost_id );


        seekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener( ) {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i>8)
                {
                    TV_maxCandidate.setText( "Maximum candidate to be apply : Valid range 2-10" );
                }
                else
                {
                    TV_maxCandidate.setText( "Maximum candidate to be apply : " +(i+2));
                }
                maxCand = i+2;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        } );


        post_name = findViewById(R.id.edit_postname_id);
        post_discription = findViewById(R.id.edit_description_id);
        postlist_textView = findViewById(R.id.postlist_textView_id);

        election_name = findViewById(R.id.textview_title_id);

        name = getIntent().getExtras().getString("title");
        class_name = getIntent().getExtras().getString("class_name");
        from_uid = getIntent().getExtras().getString("from");
        to_uid = getIntent().getExtras().getString("to");
        dept_name1 = getIntent().getExtras().getString("dept_name");
        E_date = getIntent().getExtras().getString( "e_date" );
        E_time = getIntent().getExtras().getString( "e_time" );


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
                else if(maxCand>10)
                {
                    Toast.makeText( AddPost.this, "Range Inappropriate", Toast.LENGTH_SHORT ).show( );
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

                        inc++;

                        rowTextView.setText(inc+" ) "+pname);
                        rowTextView.setTextColor(getResources().getColor(R.color.BlackColor));
                        rowTextView.setTextSize(20);

                        linearLayout.addView(rowTextView);


                        Toast.makeText(AddPost.this, "Post added Successfully...", Toast.LENGTH_SHORT).show();
                        post_name.setText(null);
                        post_discription.setText(null);
//                        submit_button.requestFocus();

                    }

                }
            }
        });



    }

    void submitdata()
    {
        if(inc==0)
        {
            Toast.makeText( this, "Add at least one post", Toast.LENGTH_SHORT ).show( );
        }
        else
        {

            election_reff = FirebaseDatabase.getInstance().getReference().child("department").child("election").child(dept_name1);

            String eid = election_reff.push().getKey();

            election elect = new election(class_name,name,from_uid,to_uid,E_date,E_time,eid,maxCand,map);

            election_reff.child(eid).setValue(elect);

            Toast.makeText(AddPost.this, "Election added Successfully..", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(getApplicationContext(),Admin_homepage.class);
            i.putExtra("dept_n",dept_name1);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

    }

}
