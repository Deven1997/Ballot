package com.example.abc.ballot;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class Add_ElectionFragment extends Fragment{
    View view;
    Button addpostbtn;
    EditText election_title;
    RadioGroup class_radiogrp;
    RadioButton class_radioButton;

    EditText start_uid, end_uid;

    String checked_class = "None";
    String fromUID,toUID;

    public Add_ElectionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_election_fragment,container,false);

        election_title = view.findViewById(R.id.edit_title_id);

        start_uid = view.findViewById(R.id.start_uid_id);
        end_uid = view.findViewById(R.id.end_uid_id);

        fromUID = start_uid.getText().toString().trim();
        toUID = end_uid.getText().toString().trim();

        class_radiogrp = view.findViewById(R.id.radiogrp_class_id);
        class_radiogrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                class_radioButton = view.findViewById(checkedId);
                checked_class = class_radioButton.getText().toString().trim();

            }
        });

        addpostbtn = view.findViewById(R.id.addpost_btn);

        addpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),AddPost.class);
                i.putExtra("title",election_title.getText().toString());
                i.putExtra("class_name",checked_class);
                i.putExtra("from",fromUID);
                i.putExtra("to",toUID);
                startActivity(i);
            }
        });

        return view;
    }




}
