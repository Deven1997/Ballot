package com.example.abc.ballot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Add_ElectionFragment extends Fragment {
    View view;
    Button addpostbtn;
    public Add_ElectionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_election_fragment,container,false);
        addpostbtn = view.findViewById(R.id.addpost_btn);
        addpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDailog();
            }
        });
        return view;
    }
    public void openDailog()
    {
        add_post_dailog dailog_object = new add_post_dailog();
        dailog_object.show(getFragmentManager(),"add post Dailog");
    }
}
