package com.example.abc.ballot;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Add_ElectionFragment extends Fragment {
    View view;
    Button addpostbtn;
    EditText election_title;
    RadioGroup class_radiogrp;
    RadioButton class_radioButton;
    Calendar mycal=Calendar.getInstance();
    int hours,min;
    String AMPM;
//time
private TimePickerDialog time;
Button btn_set_time;


    Button btn_set_date;
    EditText start_uid, end_uid;

    String checked_class = "None";
    String fromUID,toUID,title;

    String dept;

    String E_time = "None";
    String E_date = "None";

    DatePickerDialog.OnDateSetListener date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.add_election_fragment, container, false );

        election_title = view.findViewById( R.id.edit_title_id );
        start_uid = view.findViewById( R.id.start_uid_id );
        end_uid = view.findViewById( R.id.end_uid_id );

        hours = mycal.get( Calendar.HOUR );
        min = mycal.get( Calendar.MINUTE );


        if (getArguments( ).getString( "dept_n" ) != null) {
            dept = getArguments( ).getString( "dept_n" );
        }

        Toast.makeText( getActivity( ), dept, Toast.LENGTH_SHORT ).show( );

        class_radiogrp = view.findViewById( R.id.radiogrp_class_id );
        class_radiogrp.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener( ) {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                class_radioButton = view.findViewById( checkedId );
                checked_class = class_radioButton.getText( ).toString( ).trim( );

            }
        } );



        date = new DatePickerDialog.OnDateSetListener( ) {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mycal.set( Calendar.YEAR, year );
                mycal.set( Calendar.MONTH, month );
                mycal.set( Calendar.DAY_OF_MONTH, day );
                updateLabel( );

            }
        };
        btn_set_date = view.findViewById( R.id.set_date_tbn_id );
        btn_set_date.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                new DatePickerDialog( getContext( ), date, mycal.get( Calendar.YEAR ), mycal.get( Calendar.MONTH ),
                                      mycal.get( Calendar.DAY_OF_MONTH ) ).show( );
            }
        } );
//        //timepicker 11 oct 2019

        btn_set_time=view.findViewById( R.id.set_time_tbn_id );

        btn_set_time.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                time = new TimePickerDialog( getContext( ), new TimePickerDialog.OnTimeSetListener( ) {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hrs, int mi) {

                        if(hrs>=12)
                        {
                          AMPM = "PM";

                        }
                        else
                            AMPM = "AM";

                        E_time = String.format( "%02d : %02d ",hrs,mi )+AMPM;
                        btn_set_time.setText( E_time);
                    }
                },hours,min,false );
                time.show();

            }
        } );


        addpostbtn = view.findViewById(R.id.addpost_btn);

        addpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromUID = start_uid.getText().toString().trim();
                toUID = end_uid.getText().toString().trim();
                title = election_title.getText().toString().trim();

                if(checked_class.equals( "None" )|| title.length()<=0 || fromUID.length()<=0|| toUID.length()<=0 || E_time.equals( "None")||E_date.equals( "None" ))                {
                    Toast.makeText(getContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(getActivity(),AddPost.class);
                    i.putExtra("title",election_title.getText().toString());
                    i.putExtra("class_name",checked_class);
                    i.putExtra("from",fromUID);
                    i.putExtra("to",toUID);
                    i.putExtra("dept_name",dept);
                    i.putExtra( "e_date",E_date );
                    i.putExtra( "e_time",E_time );
                    startActivity(i);
                }


            }

        });



        return view;
    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat( myFormat, Locale.US);
        E_date = sdf.format( mycal.getTime() );
        btn_set_date.setText( "Date : "+E_date );
    }



}
