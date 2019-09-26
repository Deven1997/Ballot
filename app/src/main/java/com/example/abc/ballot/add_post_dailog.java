package com.example.abc.ballot;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class add_post_dailog extends AppCompatDialogFragment {

    private EditText postname;
    private EditText discription;
    public String pname= "None",disc="None";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_add_post_dailog, null);

        postname = view.findViewById(R.id.edit_postname_id);
        discription = view.findViewById(R.id.edit_Add_description);


        builder.setView(view).setTitle("Add Post")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String pname = postname.getText().toString();
                        String pdisc = discription.getText().toString();


                    }
                });

        return builder.create();
    }



}
