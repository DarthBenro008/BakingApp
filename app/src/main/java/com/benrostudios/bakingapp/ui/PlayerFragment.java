package com.benrostudios.bakingapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benrostudios.bakingapp.R;
import com.benrostudios.bakingapp.network.response.StepsBean;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerFragment extends Fragment implements Player.EventListener {
    private StepsBean steps;
    private SimpleExoPlayer player;
    private boolean hasVideoUrl;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean isPlayerReady = true;
    private Boolean isTwoPane;
    private final String savedpos = "SAVEDPOS";
    private final String savedwin = "SAVEDWINDOW";
    private final String savedStates = "ISPPLAYERREDAY";
    private static final String STEPS_LIST = "stepsList";
    private static final String IS_TWO_PANE = "isTwoPane";


    @BindView(R.id.player_view)
    PlayerView playerView;
    @BindView(R.id.steps_desc)
    TextView desc;
    @BindView(R.id.no_vid_image)
    ImageView imageView;

    public PlayerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        steps = (StepsBean) getArguments().getSerializable(STEPS_LIST);
        isTwoPane = getArguments().getBoolean(IS_TWO_PANE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, v);
        UriParsing();
        if (savedInstanceState != null) {
            mCurrentWindow = savedInstanceState.getInt(savedwin);
            mPlaybackPosition = savedInstanceState.getLong(savedpos);
            isPlayerReady = savedInstanceState.getBoolean(savedStates);
        } else {
            mCurrentWindow = C.INDEX_UNSET;
            mPlaybackPosition = C.TIME_UNSET;
        }
        populateDesc();
        if (!isTwoPane) {
            hideSystemUi();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        // Before API level 24 we wait as long as possible until we grab resources, so we wait until
        // onResume() before initializing the player.
        if (Util.SDK_INT <= Build.VERSION_CODES.M || player == null) {
            // Initialize the player if the step of the recipe has a video URL
            initializePlayer(hasVideoUrl);
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int flagFullScreen = View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

            // Enable full-screen mode on PlayerView, empty ImageView
            playerView.setSystemUiVisibility(flagFullScreen);
            imageView.setSystemUiVisibility(flagFullScreen);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        updateCurrentPosition();
        outState.putInt(savedwin, mCurrentWindow);
        outState.putLong(savedpos, mPlaybackPosition);
        outState.putBoolean(savedStates, isPlayerReady);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer(hasVideoUrl);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Before API level 24 there is no guarantee of onStop() being called. So we have to release
        // the player as early as possible in onPause().
        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    private void UriParsing() {
        mVideoUrl = steps.getVideoURL();
        mThumbnailUrl = steps.getThumbnailURL();
        if (mThumbnailUrl.contains(getResources().getString(R.string.mp4))) {
            mVideoUrl = mThumbnailUrl;
        }

        if (!mVideoUrl.isEmpty()) {
            hasVideoUrl = true;
        } else if (!mThumbnailUrl.isEmpty()) {
            hasVideoUrl = false;
            playerView.setVisibility(View.GONE);
            Log.d("PlayerFragment", mThumbnailUrl);
            Picasso.with(getContext())
                    .load(mThumbnailUrl)
                    .placeholder(R.drawable.nothing)
                    .error(R.drawable.nothing)
                    .centerCrop()
                    .into(imageView);
        } else {
            hasVideoUrl = false;
            playerView.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.nothing);
        }

    }

    private void populateDesc() {
        desc.setText(steps.getDescription());
    }

    private void initializePlayer(boolean hasVideoUrl) {
        // Check if the step of the recipe has a video
        if (hasVideoUrl) {
            if (player == null) {
                DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(getContext());
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                player = ExoPlayerFactory.newSimpleInstance(
                        defaultRenderersFactory, trackSelector, loadControl);

                // Set the ExoPlayer to the playerView
                playerView.setPlayer(player);

                player.setPlayWhenReady(isPlayerReady);
            }

            // Set the Player.EventListener to this fragment
            player.addListener(this);

            // Prepare the MediaSource
            Uri mediaUri = Uri.parse(mVideoUrl);
            MediaSource mediaSource = buildMediaSource(mediaUri);


            boolean haveStartPosition = mCurrentWindow != C.INDEX_UNSET;

            if (haveStartPosition) {

                player.seekTo(mCurrentWindow, mPlaybackPosition);

            }
            // The boolean flags indicate whether to reset position and state of the player
            player.prepare(mediaSource, !haveStartPosition, false);
        }
    }

    private MediaSource buildMediaSource(Uri mediaUri) {
        String userAgent = Util.getUserAgent(this.getContext(), getString(R.string.app_name));
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(mediaUri);
    }


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

    public static PlayerFragment newInstance(StepsBean steps, Boolean isTwoPane) {
        PlayerFragment myFragment = new PlayerFragment();

        Bundle args = new Bundle();
        args.putSerializable(STEPS_LIST, steps);
        args.putBoolean(IS_TWO_PANE, isTwoPane);
        myFragment.setArguments(args);
        return myFragment;
    }


    private void releasePlayer() {
        if (player != null) {
            updateCurrentPosition();
            player.release();
            player = null;
        }
    }
    private void updateCurrentPosition() {
        if (player != null) {
            mPlaybackPosition = player.getCurrentPosition();
            mCurrentWindow = player.getCurrentWindowIndex();
            isPlayerReady = player.getPlayWhenReady();
        }
    }
}
