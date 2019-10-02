package com.example.abc.ballot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.List;

public class ElectionList extends Fragment {
    View view;
    Button result_button;

    public ElectionList() {
    }

    DatabaseReference electionlist_reff;

    ListView electionListView;

    List<election> electionList;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.elections_fragment,container, false);

        electionListView = view.findViewById(R.id.listView_id);

        electionlist_reff = FirebaseDatabase.getInstance().getReference().child("department").child("election");

        electionList = new ArrayList<>();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


        electionlist_reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                electionList.clear();
                for(DataSnapshot electionSnapshot : dataSnapshot.getChildren())
                {
                    election elect = electionSnapshot.getValue(election.class);

                    electionList.add(elect);
                }

                election_listview_adapter adapter = new election_listview_adapter(getActivity(),electionList);
                electionListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
