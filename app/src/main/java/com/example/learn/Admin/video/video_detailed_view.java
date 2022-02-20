package com.example.learn.Admin.video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.learn.Helper_class.Model.Channel;
import com.example.learn.Helper_class.Model.filemodel;
import com.example.learn.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class video_detailed_view extends AppCompatActivity {

    // [ OTHER ATTRIBUTES ]
    private TextView title,desc;
    private ProgressBar progressBar;
    private ImageView fullscreenButton;
    boolean fullscreen = false;

    // [ VIDEO ]
    private Uri videoUri;

    // [ EXOPLAYER ]
    private PlayerView PlayerView;
    private SimpleExoPlayer Player;

    ImageView bm;
    FirebaseDatabase database;
    DatabaseReference rootref,rootrefList;
    Boolean Bookmark = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detailed_view);

        // [ OTHER ATTRIBUTES ]
        title = (TextView) findViewById(R.id.name_AVDV);
        desc = (TextView) findViewById(R.id.desc_AVDV);
        PlayerView = findViewById(R.id.recycle_video_AVDV);
        progressBar = findViewById(R.id.progress_bar_AVDV);
        fullscreenButton = PlayerView.findViewById(R.id.exo_fullscreen_icon);

        bm = (ImageView) findViewById(R.id.bm_sc);

        // [ GETTING DATA ]
        Intent intent = getIntent();
        String sub_name = getIntent().getExtras().getString("fmtitle");
        String sub_desc = intent.getStringExtra("fmdesc");
        String sub_url = intent.getStringExtra("fmVideo");
        String sub_thumb = intent.getStringExtra("fmThumb");

        // [ SETTING DATA ]
        title.setText(sub_name);
        desc.setText(sub_desc);

        // [ parsing a video url and parsing its video uri ]
        videoUri = Uri.parse(sub_url);

        // Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        database = FirebaseDatabase.getInstance();
        rootref = FirebaseDatabase.getInstance().getReference();

        bookmarkChecker(sub_name);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullscreen) {
                    // [ SETTING IMAGE TYPE ]
                    fullscreenButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));

                    // [ SETTING VISIBILITY TO ACTION BAR  ] --- NOT NECESSARY
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }

                    // [ SETTING SCREEN TYPE ]
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    // [ SETTING SCREEN WIDTH AND HEIGHT ]
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) PlayerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) ( 200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    PlayerView.setLayoutParams(params);

                    // [ SETTING FLAG TO FALSE ]
                    fullscreen = false;
                }else{
                    // [ SETTING IMAGE TYPE ]
                    fullscreenButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));

                    // [ SETTING VISIBILITY TO ACTION BAR  ] --- NOT NECESSARY
                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_FULLSCREEN|
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }

                    // [ SETTING SCREEN TYPE ]
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    // [ SETTING SCREEN WIDTH AND HEIGHT ]
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) PlayerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    PlayerView.setLayoutParams(params);

                    // [ SETTING FLAG TO FALSE ]
                    fullscreen = true;
                }
            }
        });

        try{
            // [ INITIALIZE LOAD CONTROL ]
            LoadControl loadControl = new DefaultLoadControl();

            // [ USED TO GET THE DEFAULT BANDWIDTH ]
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            // [ USED TO NAVIGATE BETWEEN VIDEO IN DEFAULT SEEK_BAR , AND TO GET BANDWIDTH FOR THAT PARTICULAR VIDEO  ]
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            // [ ExoPlayer is an application level media player for Android. ]
            // [ It provides an alternative to Android’s MediaPlayer API for playing audio and video both locally and over the Internet. ]
            Player = ExoPlayerFactory.newSimpleInstance(this, trackSelector,loadControl);

            // [  Extractor factory that returns an empty list of extractors. ]
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // [ creating a variable for data_source_factory and setting its user agent as 'exoplayer_video'
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("LearningApp");

            // [ creating a media source with above variables and passing our event handler as null ]
            MediaSource mediaSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);

            // [ SETTING PLAYER INSIDE PLAYER_VIEW ]
            PlayerView.setPlayer(Player);

            // [ SET SCREEN ON ]
            PlayerView.setKeepScreenOn(true);

            // [ PREPARING PLAYER WITH MEDIA SOURCE ]
            Player.prepare(mediaSource);

            // [ PREPARING PLAYER TO READY ]
            Player.setPlayWhenReady(true);

            // [ CLICK EVENT ]
            Player.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == Player.STATE_BUFFERING){
                        progressBar.setVisibility(View.VISIBLE);
                    }else if(playbackState == Player.STATE_READY){
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });

        }catch (Exception e) {
            Log.e("TAG", "Error : " + e.toString());
        }

        bm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bookmark = true;
                String upload_id = rootref.push().getKey();
                filemodel fm = new filemodel(sub_name, sub_url,sub_desc,sub_thumb);

                rootref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(Bookmark.equals(true)){
                            if(snapshot.child("Bookmarks").child(userId).hasChild(sub_name)){
                                rootref.child("Bookmarks").child(userId).child(sub_name).removeValue();
                                Bookmark = false;
                            }else {
                                rootref.child("Bookmarks").child(userId).child(sub_name).setValue(fm);
                                Bookmark=false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void bookmarkChecker(String sub_name) {
        rootrefList = FirebaseDatabase.getInstance().getReference();
        // Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        rootrefList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Bookmarks").child(userId).hasChild(sub_name)){
                    bm.setImageResource(R.drawable.turnedin);
                }
                else{
                    bm.setImageResource(R.drawable.turned_out);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Player.setPlayWhenReady(false);
        Player.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Player.setPlayWhenReady(true);
        Player.getPlaybackState();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Player.setPlayWhenReady(true);
        Player.release();
    }
}