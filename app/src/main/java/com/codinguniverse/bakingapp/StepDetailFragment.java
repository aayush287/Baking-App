package com.codinguniverse.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codinguniverse.bakingapp.models.Steps;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import static android.content.ContentValues.TAG;


public class StepDetailFragment extends Fragment {

    // All constants
    private static final String KEY_PLAYER_POSITION = "key-position";
    private static final String PLAY_WHEN_READY = "is-ready";

    private Steps mSteps;
    private int stepIndex;
    private int size;
    private boolean mTwoPane;

    // player position
    private long playerPosition = C.TIME_UNSET;
    ;
    // whether the player is playing or paused
    // default true
    private boolean isPlayWhenReady = true;

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoplayer;

    private OnNextClick mOnNextClick;
    private OnPrevClick mOnPrevClick;


    public interface OnNextClick {
        void onNextClick(int position);
    }

    public interface OnPrevClick {
        void onPrevClick(int position);
    }

    public StepDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);


        mPlayerView = view.findViewById(R.id.playerView);

        TextView description = view.findViewById(R.id.tv_step_long_desc);
        Button prevButton = view.findViewById(R.id.btn_prev);
        Button nextButton = view.findViewById(R.id.btn_next);


        if (savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong(KEY_PLAYER_POSITION);
            isPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY, isPlayWhenReady);
        }


        if (mSteps != null) {

            String descriptionText = mSteps.getDescription();
            description.setText(descriptionText);
            if(mTwoPane){
                nextButton.setVisibility(View.GONE);
                prevButton.setVisibility(View.GONE);
            }else if (stepIndex == size - 1) {
                nextButton.setVisibility(View.GONE);
            } else if (stepIndex == 0) {
                prevButton.setVisibility(View.GONE);
            } else {
                nextButton.setVisibility(View.VISIBLE);
                prevButton.setVisibility(View.VISIBLE);
            }
                /*
                Setting button to listen click
             */
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNextStep();
                }
            });

            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPrevStep();
                }
            });


            String videoUrl = mSteps.getVideoURL();

            if (videoUrl.isEmpty()) {
                mPlayerView.setVisibility(View.GONE);
            } else {
                mPlayerView.setVisibility(View.VISIBLE);
                initializePlayer(Uri.parse(videoUrl));
            }


        }


        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnPrevClick = (OnPrevClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnPrevButtonClickListener");
        }

        try {
            mOnNextClick = (OnNextClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnNextButtonClickListener");
        }
    }

    // ___________________________** Exo Player Logic **__________________________________________

    private void initializePlayer(Uri mediaUri) {
        if (mExoplayer == null) {
            if (getContext() == null) {
                throw new NullPointerException("getContext() is null used to play video");
            }

            /*
                Creating instance of Exo player
                and track Selector
             */

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoplayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoplayer);


            /*
                Our player now needs some content to play.
                For this we need to create a MediaSource.
                So preparing a media source
             */
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), "Baking app"), new DefaultExtractorsFactory(), null, null);
            mExoplayer.prepare(mediaSource);


            if (playerPosition != C.TIME_UNSET) {
                mExoplayer.seekTo(playerPosition);
            }
            mExoplayer.setPlayWhenReady(isPlayWhenReady);
        }
    }

    private void releasePlayer() {
        if (mExoplayer != null) {
            // save the ExoPlayer's position in case there's an orientation change
            playerPosition = mExoplayer.getCurrentPosition();
            isPlayWhenReady = mExoplayer.getPlayWhenReady();
            mExoplayer.stop();
            mExoplayer.release();
            mExoplayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(KEY_PLAYER_POSITION, playerPosition);
        outState.putBoolean(PLAY_WHEN_READY, isPlayWhenReady);
    }

    //__________________________** End of Exo Player logic **_______________________________________

    /*
                Below is the code implementation of click handling on
                previous and next button and apart from them there are setters for
                step, size and stepIndex
             */
    private void setNextStep() {
        mOnNextClick.onNextClick(stepIndex);
    }

    private void setPrevStep() {
        mOnPrevClick.onPrevClick(stepIndex);
    }

    public void setSteps(Steps steps) {
        mSteps = steps;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTwoPane(boolean twoPane) {
        mTwoPane = twoPane;
    }
}
