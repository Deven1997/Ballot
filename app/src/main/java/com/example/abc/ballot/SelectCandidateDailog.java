package com.example.abc.ballot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.widget.Button;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectCandidateDailog extends DialogFragment {

    //default selected position
    int position = 0;

    String[] names;
    String postname;



    public interface SingleChoiceListner
    {
        void onPositiveButtonClicked(String[] list,int position);


    }

    SingleChoiceListner listner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try
        {
            listner = (SingleChoiceListner) context;
        }catch (Exception e)
        {
            throw new ClassCastException(getActivity().toString()+"SingleChoiceListner must be implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(Html.fromHtml("<font color='#77E60D'>Vote for "+postname+"</font>"))
                .setSingleChoiceItems( names, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position = which;



                    }
                })
                .setPositiveButton("VOTE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listner.onPositiveButtonClicked(names,position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    public void setNames(String[] names,String postname)
    {
        this.names = names;
        this.postname = postname;
    }

    @Override
    public void onStart() {
        super.onStart();

        Button positive = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);

        positive.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        Button negative = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE);
        negative.setTextColor(getResources().getColor(R.color.BlackColor));


    }
}
