package com.udacity.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetails extends Fragment
    implements ExoPlayer.EventListener, View.OnClickListener {

    TextView stepTitle;
    TextView stepDescription;
    Button nextButton;
    Button previous;
    ImageView stepPicture;

    public static final String JSON = "json";
    public static final String INDEX = "index";
    public static final String STEP = "step";
    public static final String PLAYBACK = "playback";
    public static final String WINDOW = "window";
    private static final String TAG = StepDetails.class.getSimpleName();

    private String[][] specificSteps;

    private String json;
    private int recipeNumber;
    private int step;
    private int currentWindow;

    private long playbackPosition;

    private SimpleExoPlayer player;
    private SimpleExoPlayerView playerView;

    protected Context context;

    static interface PreviousOrNextListener {
        void ingredientsClicked();
    }

    private PreviousOrNextListener listener;

    public StepDetails()
    {
        // empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (savedInstanceState != null) {
            // get something, maybe image ID's
            json = savedInstanceState.getString(JSON);
            recipeNumber = savedInstanceState.getInt(INDEX);
            step = savedInstanceState.getInt(STEP);
            specificSteps = NetworkingUtils.getSpecificSteps(json, recipeNumber);
            playbackPosition = savedInstanceState.getLong(PLAYBACK);
            currentWindow = savedInstanceState.getInt(WINDOW);
        }

        View rootView = layoutInflater.inflate(R.layout.step_details, container, false);

        return rootView;


    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        View rootView = getView();
        if (rootView != null) {
            stepTitle = (TextView) rootView.findViewById(R.id.step_title);
            stepDescription = (TextView) rootView.findViewById(R.id.detailedStep);
            nextButton = (Button) rootView.findViewById(R.id.next_step);
            nextButton.setOnClickListener(this);
            previous = (Button) rootView.findViewById(R.id.previous_step);
            previous.setOnClickListener(this);
            playerView = (SimpleExoPlayerView) rootView.findViewById(R.id.video_player);
            playerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.exo_controls_play));
            stepPicture = (ImageView) rootView.findViewById(R.id.step_picture);
            Glide.with(this).clear(stepPicture);
            releasePlayer();
            getStepDetails();
        }

    }

    private void initializePlayer(String mediaUriString)
    {
        if (mediaUriString.equals("")) {
            playerView.setVisibility(View.INVISIBLE);
        } else
            playerView.setVisibility(View.VISIBLE);

        Uri mediaUri;
        try {
            mediaUri = Uri.parse(mediaUriString);
        } catch (Exception e) {
            Log.i(StepDetails.class.getSimpleName(), "There's a problem with the Uri");
            mediaUri = null;
        }

        if (player == null)
        {
            // Create the instance of the SimpleExoPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            playerView.setPlayer(player);

            // set the listener to this activity
            player.addListener(this);    // implemented ExoPlayer.EventListener first

            // resume playback position
            player.seekTo(currentWindow, playbackPosition);

            // Prepare the MediaSource
            String userAgent = Util.getUserAgent(context, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(context, userAgent),
                    new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void onDestroy(){
        releasePlayer();
        super.onDestroy();
    }

    void releasePlayer()
    {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        playbackPosition = player.getCurrentPosition();
        currentWindow = player.getCurrentWindowIndex();
        savedInstanceState.putInt(STEP, step);
        savedInstanceState.putString(JSON, json);
        savedInstanceState.putInt(INDEX, recipeNumber);
        savedInstanceState.putLong(PLAYBACK, playbackPosition);
        savedInstanceState.putInt(WINDOW, currentWindow);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
            Log.i(TAG, "playing");
        }
        else if (playbackState == ExoPlayer.STATE_READY) {
            Log.i(TAG, "paused");
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    // setter methods to set the values in the fragment
    public void setSpecificSteps(String jsonText, int selectedRecipe)
    {
        json = jsonText;
        recipeNumber = selectedRecipe;
        specificSteps = NetworkingUtils.getSpecificSteps(json, recipeNumber);
    }

    public void setStep(int currentStep)
    {
        step = currentStep;
    }

    public void getStepDetails()
    {
        stepTitle.setText(specificSteps[1][step]);
        stepDescription.setText(specificSteps[2][step]);

        if (step == NetworkingUtils.getNumberOfSteps(json, recipeNumber) - 1)
            nextButton.setVisibility(View.INVISIBLE);
        else
            nextButton.setVisibility(View.VISIBLE);
        if (step == 0)
        {
            previous.setText(R.string.Ingredients);
            stepDescription.setText("");
        }
        else if (step == 1)
            previous.setText(R.string.Introduction);
        else
            previous.setText(R.string.previous_step);

        initializePlayer(specificSteps[3][step]);
        playerView.setVisibility(View.VISIBLE);
        handlePicture();
    }

    // onClick for the two buttons at the bottom of the fragment (previous and next)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous_step:
                if (step > 0) {
                    step = step - 1;
                    stepTitle.setText(specificSteps[1][step]);
                    stepDescription.setText(specificSteps[2][step]);
                    if (step == 0)
                        previous.setText(R.string.Ingredients);
                    else if (step == 1)
                        previous.setText(R.string.Introduction);

                    releasePlayer();
                    playbackPosition = 0;
                    currentWindow = 0;
                    initializePlayer(specificSteps[3][step]);
                }
                else {
                    player.stop();
                    player.release();
                    listener.ingredientsClicked();
                }

                if (step < NetworkingUtils.getNumberOfSteps(json, recipeNumber) - 1)
                    nextButton.setVisibility(View.VISIBLE);

                handlePicture();
                break;
            case R.id.next_step:
                if (step < NetworkingUtils.getNumberOfSteps(json, recipeNumber) - 1) {
                    step = step + 1;
                    if (step < NetworkingUtils.getNumberOfSteps(json, recipeNumber) - 1) {
                        nextButton.setVisibility(View.VISIBLE);
                    } else {
                        nextButton.setVisibility(View.INVISIBLE);
                    }
                    stepTitle.setText(specificSteps[1][step]);
                    stepDescription.setText(specificSteps[2][step]);

                    releasePlayer();
                    playbackPosition = 0;
                    currentWindow = 0;
                    initializePlayer(specificSteps[3][step]);
                }

                if (step > 1)
                    previous.setText(R.string.previous_step);
                else if (step == 1)
                    previous.setText(R.string.Introduction);

                handlePicture();
                break;
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            listener = (PreviousOrNextListener) context;
        } catch (ClassCastException ce) {
            Log.i("DetailsActivity", ce.getMessage());
        }
    }

    private void handlePicture()
    {
        if (specificSteps[3][step].isEmpty() && !specificSteps[4][step].isEmpty())
        {
            String pictureUrl = specificSteps[4][step];
            Glide.with(this).load(pictureUrl).into(stepPicture);
            stepPicture.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);

        } else {
            stepPicture.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
            if (specificSteps[3][step].isEmpty()) {
                playerView.setVisibility(View.GONE);
            }
        }
    }

}
