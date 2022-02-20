package com.example.learn.Fragment_main.Bookmark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learn.Helper_class.Interface.VideoItemClickListener;
import com.example.learn.Helper_class.Model.filemodel;
import com.example.learn.R;

import java.util.ArrayList;

public class markAdaptor extends RecyclerView.Adapter {

    Context context;
    ArrayList<filemodel> list;
    VideoItemClickListener videoItemClickListener;

    public markAdaptor(Context context, ArrayList<filemodel> list, VideoItemClickListener videoItemClickListener) {
        this.context = context;
        this.list = list;
        this.videoItemClickListener = videoItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_detail,parent,false);
        markAdaptor.ViewHolderClass viewHolderClass = new markAdaptor.ViewHolderClass(view);

        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        markAdaptor.ViewHolderClass viewHolderClass = (markAdaptor.ViewHolderClass)holder;

        filemodel fm = list.get(position);

        // [ FOR IMAGE WE NEED TO USE GLIDE ]
        Glide.with(context).load(fm.getvThumb()).into(viewHolderClass.vThumb);
        viewHolderClass.vtitle.setText(fm.getTitle());
        viewHolderClass.vdesc.setText(fm.getVdesc());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        ImageView vThumb;
        TextView vtitle,vdesc;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            vThumb = itemView.findViewById(R.id.recycle_thumb_SD);
            vtitle = itemView.findViewById(R.id.recycle_sub_SD);
            vdesc = itemView.findViewById(R.id.recycle_desc_SD);

            // [CLICK EVENT]
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoItemClickListener.onVideoClick(list.get(getAdapterPosition()),vThumb);
                }
            });
        }
    }
}
