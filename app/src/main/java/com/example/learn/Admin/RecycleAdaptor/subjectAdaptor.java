package com.example.learn.Admin.RecycleAdaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learn.R;
import com.example.learn.Helper_class.Interface.VideoItemClickListener;
import com.example.learn.Helper_class.Model.filemodel;

import java.util.ArrayList;

public class subjectAdaptor extends RecyclerView.Adapter {

    Context context;
    ArrayList<filemodel> list;
    VideoItemClickListener videoItemClickListener;

    public subjectAdaptor(Context context, ArrayList<filemodel> list, VideoItemClickListener videoItemClickListener) {
        this.context = context;
        this.list = list;
        this.videoItemClickListener = videoItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_detail,parent,false);
        subjectAdaptor.ViewHolderClass viewHolderClass = new subjectAdaptor.ViewHolderClass(view);

        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        subjectAdaptor.ViewHolderClass viewHolderClass = (subjectAdaptor.ViewHolderClass)holder;

        filemodel fm = list.get(position);

        // [ FOR IAMGE WE NEED TO USE GLIDE ]
        Glide.with(context).load(fm.getvThumb()).into(viewHolderClass.vThumb);
        viewHolderClass.vtitle.setText(fm.getTitle());
        viewHolderClass.vdesc.setText(fm.getVdesc());

        // [CLICK EVENT]
        /*viewHolderClass.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
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


// --------------------------------------------------- ADAPTOR ---------------------------------------------

/*
import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class subjectAdaptor extends RecyclerView.ViewHolder{
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer simpleExoPlayer;
    TextView vtitle,vdesc;
    public subjectAdaptor(@NonNull View itemView)
    {
        super(itemView);
        simpleExoPlayerView = itemView.findViewById(R.id.recycle_video_SD);
        vtitle = itemView.findViewById(R.id.recycle_sub_SD);
        vdesc = itemView.findViewById(R.id.recycle_desc_SD);
    }
    void prepareexoplayer(Application application , String videotitle , String videodesc , String videourl){
        try{

            vtitle.setText(videotitle);
            vdesc.setText(videodesc);
            // bandwidthmeter is used for getting default bandwidth
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            // track selector is used to navigate between  video using a default seekbar.
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            // we are adding our track selector to exoplayer.
            simpleExoPlayer =(SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(application, trackSelector);

            // we are parsing a video url and parsing its video uri.
            Uri videouri = Uri.parse(videourl);

            // we are creating a variable for datasource factory and setting its user agent as 'exoplayer_view'
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");

            // we are creating a variable for extractor factory and setting it to default extractor factory.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // we are creating a media source with above variables and passing our event handler as null,
            MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);

            // inside our exoplayer view we are setting our player
            simpleExoPlayerView.setPlayer(simpleExoPlayer);

            // we are preparing our exoplayer with media source.
            simpleExoPlayer.prepare(mediaSource);

            // we are setting our exoplayer when it is ready.
            simpleExoPlayer.setPlayWhenReady(false);

        }catch(Exception ex){
            Log.d("Exoplayer problem", ex.getMessage().toString());
        }
    }
}
*/