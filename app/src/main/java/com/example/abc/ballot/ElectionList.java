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

import java.nio.charset.IllegalCharsetNameException;

public class ElectionList extends Fragment {
    View view;
    Button result_button;

    public ElectionList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.elections_fragment,container, false);

        result_button = view.findViewById(R.id.button_result);
        result_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),ResultPage.class);
                startActivity(i);
            }
        });

        return view;
    }
}
