package com.example.abc.ballot;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    private final List<Uri> ImageURl = new ArrayList<>();
    private final List<String> candidate_name = new ArrayList<>();
    private final List<String> uid = new ArrayList<>();
    private final List<String> Description = new ArrayList<>();


    public SliderAdapter(Context context)
    {
            this.context = context;
    }



    @Override
    public int getCount() {
        return uid.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.candidate_layout,container,false);

        final ImageView imageView = view.findViewById(R.id.IV_candidate_prof);
        TextView candidateName = view.findViewById(R.id.TV_candidateNAME_id);
        TextView candidateUID = view.findViewById(R.id.TV_uid_id);
        TextView candidateDesc = view.findViewById(R.id.TV_disc_id);

        candidateName.setText(candidate_name.get(position));
        candidateUID.setText(uid.get(position));
        candidateDesc.setText(Description.get(position));

        Picasso.with(view.getContext()).load(ImageURl.get(position)).fit().centerCrop().into(imageView);

//        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("candidates/"+uid.get(position)+".jpg");
//        try
//        {
//            final File file = File.createTempFile("image","jpg");
//            imageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Uri u = Uri.fromFile(file);
//                    Picasso.with(view.getContext()).load(u).fit().centerCrop().into(imageView);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(context, "Image failed to Load", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }catch (Exception e)
//        {
//
//        }


        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);

    }

    public void setData(Uri url, String name, String ucid, String desc)
    {
        ImageURl.add(url);
        candidate_name.add(name);
        uid.add(ucid);
        Description.add(desc);
        notifyDataSetChanged();
    }


}
