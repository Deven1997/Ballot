package com.example.abc.ballot;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class post_list_adapter extends ArrayAdapter<post> {
    private Activity context;
    private List<post> postlist;

    public post_list_adapter(Activity context, List<post> postlist)
    {
        super(context, R.layout.post,postlist);
        this.context = context;
        this.postlist = postlist;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater3 = context.getLayoutInflater();
        View postviewItem = inflater3.inflate( R.layout.post,null,true );

        TextView tv_post = postviewItem.findViewById( R.id.Post_title_id);
        TextView tv_disc = postviewItem.findViewById(R.id.post_description_id);

        post po = postlist.get( position );

        tv_post.setText( po.getPname() );
        tv_disc.setText( po.getPdisc() );

        return postviewItem;
    }
}
