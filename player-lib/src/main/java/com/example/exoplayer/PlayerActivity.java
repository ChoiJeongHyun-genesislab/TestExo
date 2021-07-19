/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.exoplayer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

/**
 * A fullscreen activity to play audio or video streams.
 */
public class PlayerActivity extends AppCompatActivity {

  private PlaybackStateListener playbackStateListener;
  private static final String TAG = PlayerActivity.class.getName();

  private PlayerView playerView;
  private SimpleExoPlayer player;
  private boolean playWhenReady = true;
  private int currentWindow = 0;
  private long playbackPosition = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);

    playerView = findViewById(R.id.video_view);

    playbackStateListener = new PlaybackStateListener();
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23) {
      initializePlayer();
//      testInitializedPlayer();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
//    hideSystemUi();
    if ((Util.SDK_INT <= 23 || player == null)) {
//      initializePlayer();
//      testInitializedPlayer();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {
      releasePlayer();
    }
  }

  private void testInitializedPlayer() {

    int minBufferMs = DefaultLoadControl.DEFAULT_MIN_BUFFER_MS;
    int maxBufferMs = DefaultLoadControl.DEFAULT_MIN_BUFFER_MS;
    int bufferForPlayBackMs = DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS;
    int bufferForPlayBackAfterMs = DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS;

//    int minBufferMs = 2000;
//    int maxBufferMs = 5000;
//    int bufferForPlayBackMs = 1500;
//    int bufferForPlayBackAfterMs = 2000;


    if (player == null){

//      TrackSelection.Factory t = new AdaptiveTrackSelection.Factory();
//      TrackSelection.Factory t = new AdaptiveTrackSelection.Factory();

      //version 2.14.1
      DefaultTrackSelector defaultTrackSelector = new DefaultTrackSelector(this);
      defaultTrackSelector.setParameters(
              defaultTrackSelector.buildUponParameters()
              .setMaxVideoSizeSd()
      );

      DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter.Builder(this)
              .build();


      DefaultLoadControl defaultLoadControl = new DefaultLoadControl.Builder()
              .setAllocator(new DefaultAllocator(true , C.DEFAULT_BUFFER_SEGMENT_SIZE))
              .setBufferDurationsMs(
                      DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                      DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                      DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                      DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
              )
              .setTargetBufferBytes(-1)
              .setPrioritizeTimeOverSizeThresholds(true)
              .build();


      player = new SimpleExoPlayer.Builder(this)
              .setTrackSelector(defaultTrackSelector)
              .setBandwidthMeter(defaultBandwidthMeter)
              .setLoadControl(defaultLoadControl)
              .build();
      player.setPlaybackParameters(new PlaybackParameters(1f,1f));

      playerView.setPlayer(player);

      MediaItem mediaItem = new MediaItem.Builder()
              .setUri(getString(R.string.test_mp4))
              .setMimeType(MimeTypes.APPLICATION_MP4)
              .build();

      player.setMediaItem(mediaItem);

      //Or I tried setting it as a media source.
      String userAgent = Util.getUserAgent(this , this.getApplicationInfo().name);
      DefaultDataSourceFactory defaultHttpDataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
      MediaSource mediaSource = new ProgressiveMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem);
      player.setMediaSource(mediaSource);


    }

    playerView.setPlayer(player);

//    MediaItem mediaItem = new MediaItem.Builder()
//            .setUri(getString(R.string.test_mp44))
//            .setMimeType(MimeTypes.APPLICATION_MP4)
//            .build();
//    MediaItem mediaItem = new MediaItem.Builder().setUri(getString(R.string.test_mp4)).build();


//    player.setMediaItem(mediaItem);

    String userAgent = Util.getUserAgent(this , this.getApplicationInfo().name);
    DefaultDataSourceFactory defaultHttpDataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
//    MediaSource mediaSource = new ProgressiveMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem);

//    DataSource.Factory d = new DefaultDataSourceFactory(this , userAgent , new DefaultBandwidthMeter.Builder(this)
//            .setInitialBitrateEstimate(AdaptiveTrackSelection.DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS)
//            .setResetOnNetworkTypeChange(true).build() );

//    MediaSource m = new ExtractorMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(Uri.parse(getString(R.string.test_mp4)));




    MediaItem mediaItem = new MediaItem.Builder()
            .setUri(getString(R.string.test_mp4))
            .setMimeType(MimeTypes.APPLICATION_MP4)
//            .setMimeType(MimeTypes.APPLICATION_MPD)
            .build();
//    player.setMediaItem(mediaItem);
    player.addMediaItem(mediaItem);

    player.setPlayWhenReady(playWhenReady);
    player.seekTo(currentWindow, playbackPosition);
//    player.addListener(playbackStateListener);

//    player.prepare(m);
    player.prepare();


  }

  private void initializePlayer() {
    if (player == null) {
      DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);
      trackSelector.setParameters(
              trackSelector.buildUponParameters().setMaxVideoSizeSd());
      player = new SimpleExoPlayer.Builder(this)
              .setTrackSelector(trackSelector)
              .build();
    }

    playerView.setPlayer(player);
    MediaItem mediaItem = new MediaItem.Builder()
            .setUri(getString(R.string.test_mp4))
            .setMimeType(MimeTypes.APPLICATION_MP4)
            .build();
    player.setMediaItem(mediaItem);

    player.setPlayWhenReady(playWhenReady);
    player.seekTo(currentWindow, playbackPosition);
    player.addListener(playbackStateListener);
    player.prepare();
  }

  private void releasePlayer() {
    if (player != null) {
      playbackPosition = player.getCurrentPosition();
      currentWindow = player.getCurrentWindowIndex();
      playWhenReady = player.getPlayWhenReady();
//      player.removeListener(playbackStateListener);
      player.release();
      player = null;
    }
  }

  @SuppressLint("InlinedApi")
  private void hideSystemUi() {
    playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
  }

  private class PlaybackStateListener implements Player.EventListener{

    @Override
    public void onPlaybackStateChanged(int playbackState) {
      String stateString;
      switch (playbackState) {
        case ExoPlayer.STATE_IDLE:
          stateString = "ExoPlayer.STATE_IDLE      -";
          break;
        case ExoPlayer.STATE_BUFFERING:
          stateString = "ExoPlayer.STATE_BUFFERING -";
          break;
        case ExoPlayer.STATE_READY:
          stateString = "ExoPlayer.STATE_READY     -";
          break;
        case ExoPlayer.STATE_ENDED:
          stateString = "ExoPlayer.STATE_ENDED     -";
          break;
        default:
          stateString = "UNKNOWN_STATE             -";
          break;
      }
      Log.d(TAG, "changed state to " + stateString);
    }
  }

}
