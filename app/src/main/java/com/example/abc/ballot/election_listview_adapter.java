package com.example.abc.ballot;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class election_listview_adapter extends ArrayAdapter<election> {

    private Activity context;
    private List<election> election_list;

    public election_listview_adapter(Activity context, List<election> election_list)
    {
        super(context, R.layout.election_list,election_list);
        this.context = context;
        this.election_list = election_list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflatera2 = context.getLayoutInflater();

        View listViewItem = inflatera2.inflate(R.layout.election_list,null,true);

        TextView textViewTitle = listViewItem.findViewById(R.id.tv_title_id);
        TextView textViewclassname = listViewItem.findViewById(R.id.tv_class_name_id);

        election elec = election_list.get(position);
        textViewTitle.setText(elec.getElection_name());
        textViewclassname.setText(elec.getClass_name());

        return listViewItem;

    }
}
