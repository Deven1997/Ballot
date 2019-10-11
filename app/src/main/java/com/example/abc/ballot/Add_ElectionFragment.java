package com.example.abc.ballot;

import android.app.DatePickerDialog;
import android.content.Intent;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.RatingCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
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
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;


//Teacher can add multiple elections
public class Add_ElectionFragment extends Fragment{
    View view;
    Button addpostbtn;
    EditText election_title;
    RadioGroup class_radiogrp;
    RadioButton class_radioButton;
    CalendarView calendarView;
    TextView tvselect;


    private DatePicker datePicker;
    private Calendar calendar;
   // private TextView dateView;
    private int year, month, day;


    Button btn_set_date;

    EditText start_uid, end_uid;

    String checked_class = "None";
    String fromUID,toUID;

    String ddate;

    String dept;

    DatabaseReference database_list_reff;

    DatePickerDialog.OnDateSetListener date;
    Calendar mycal;


    public Add_ElectionFragment() {
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_election_fragment,container,false);

        election_title = view.findViewById(R.id.edit_title_id);

        start_uid = view.findViewById(R.id.start_uid_id);
        end_uid = view.findViewById(R.id.end_uid_id);


        if(getArguments().getString("dept_n") != null)
        {
            dept = getArguments().getString("dept_n");
        }

        Toast.makeText(getActivity(), dept, Toast.LENGTH_SHORT).show();

        class_radiogrp = view.findViewById(R.id.radiogrp_class_id);
        class_radiogrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                class_radioButton = view.findViewById(checkedId);
                checked_class = class_radioButton.getText().toString().trim();

            }
        });
        tvselect=view.findViewById(R.id.TVSelectedDate);
        mycal=Calendar.getInstance();

        date=new DatePickerDialog.OnDateSetListener( ) {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            mycal.set(Calendar.YEAR,year );
            mycal.set(Calendar.MONTH,month);
            mycal.set(Calendar.DAY_OF_MONTH,day);
            updateLabel();

        }
    };
        btn_set_date = view.findViewById( R.id.set_date_tbn_id );
        btn_set_date.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                new DatePickerDialog( getContext(), date , mycal.get( Calendar.YEAR), mycal.get( Calendar.MONTH), mycal.get( Calendar.DAY_OF_MONTH)).show();
            }
        } );



        addpostbtn = view.findViewById(R.id.addpost_btn);

        addpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromUID = start_uid.getText().toString().trim();
                toUID = end_uid.getText().toString().trim();


                Intent i = new Intent(getActivity(),AddPost.class);
                i.putExtra("title",election_title.getText().toString());
                i.putExtra("class_name",checked_class);
                i.putExtra("from",fromUID);
                i.putExtra("to",toUID);
                i.putExtra("dept_name",dept);
                startActivity(i);
            }
        });



        return view;
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat( myFormat, Locale.US);
        tvselect.setText( "Selected Date : "+sdf.format( mycal.getTime() ) );
    }



}
