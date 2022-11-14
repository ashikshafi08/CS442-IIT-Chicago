package com.christopherhield.videostreamexample;

import static android.view.View.GONE;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.christopherhield.videostreamexample.databinding.ActivityMainBinding;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements GestureDetector.OnGestureListener {

    private static final String TAG = "MainActivityTag";

    private GestureDetectorCompat mDetector;
    private int duration;
    private int currentPos;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupFullScreen();

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
              // Nothing to do here
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Nothing to do here
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                String t1 = getTimeStamp(binding.videoView.getCurrentPosition());
                String t2 = getTimeStamp(progress);
                binding.videoView.seekTo(progress);
                Toast.makeText(MainActivity.this,
                        "Move from " + t1 + " to " + t2,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this, this);
        startVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentPos = binding.videoView.getCurrentPosition();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("POSITION", currentPos);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentPos = savedInstanceState.getInt("POSITION");
    }

    private String getTimeStamp(int ms) {
        int t = ms;
        int h = ms / 3600000;
        t -= (h * 3600000);
        int m = t / 60000;
        t -= (m * 60000);
        int s = t / 1000;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m ,s);
    }

    private void setupFullScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void startVideo() {

        binding.videoView.setOnPreparedListener(mediaPlayer -> {
            binding.progressBar.setVisibility(GONE);
            mediaPlayer.setLooping(false);

            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                int aMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); //Max Volume 15
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, aMax, AudioManager.FLAG_PLAY_SOUND);   //here you can set custom volume.
            }

            binding.videoView.seekTo(currentPos);
            binding.videoView.start();

            duration = binding.videoView.getDuration();
            binding.seekBar.setMax(duration);
            binding.duration.setText(getTimeStamp(duration));
            timerCounter();
        });

        binding.videoView.setOnCompletionListener(mediaPlayer -> Log.d(TAG, "onCompletion: "));

        Uri uri = Uri.parse(getString(R.string.video_url2));
        binding.videoView.setVideoURI(uri);
    }

    private void timerCounter() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    binding.seekBar.setProgress(binding.videoView.getCurrentPosition());
                    binding.currentPos.setText(getTimeStamp(binding.videoView.getCurrentPosition()));
                });
            }
        };
        timer.schedule(task, 0, 1000);
    }

    ////////////////////////////////////////////////////////////////

    @Override
    // Notified when a tap occurs with the down MotionEvent that triggered it.
    public boolean onDown(MotionEvent motionEvent) {
        Log.d(TAG, "onDown: (" + motionEvent.getX() + ", " + motionEvent.getY() + ")");
        return false;
    }

    @Override
    // The user has performed a down MotionEvent and not performed a move or up yet.
    public void onShowPress(MotionEvent motionEvent) {
        Log.d(TAG, "onShowPress: (" + motionEvent.getX() + ", " + motionEvent.getY() + ")");
    }

    @Override
    // Notified when a tap occurs with the up MotionEvent that triggered it.
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Log.d(TAG, "onSingleTapUp: (" + motionEvent.getX() + ", " + motionEvent.getY() + "\n");
        if (binding.videoView.isPlaying()) {
            binding.videoView.pause();
            Toast.makeText(this, "Video PAUSED", Toast.LENGTH_SHORT).show();
        } else {
            binding.videoView.start();
            Toast.makeText(this, "Video PLAYING", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    // Notified when a scroll occurs with the initial on down MotionEvent and the current move MotionEvent.
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.d(TAG, "onScroll: (" +
                motionEvent.getX() + ", " + motionEvent.getY()
                + ") to (" + motionEvent1.getX() + ", " + motionEvent1.getY() + ")");
        return false;
    }

    @Override
    // Notified when a long press occurs with the initial on down MotionEvent that trigged it.
    public void onLongPress(MotionEvent motionEvent) {
        Log.d(TAG, "onLongPress: (" + motionEvent.getX() + ", " + motionEvent.getY() + ")");
        Toast.makeText(this, "Reset to START", Toast.LENGTH_SHORT).show();
        binding.videoView.seekTo(0);
    }

    @Override
    // Notified of a fling event when it occurs with the initial on down MotionEvent and the matching up MotionEvent.
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.d(TAG, "onFling: (" + motionEvent.getX() + ", " + motionEvent.getY()
                + ") to (" + motionEvent1.getX() + ", " + motionEvent1.getY() + ")");
        float dir = motionEvent1.getX() - motionEvent.getX();

        if (dir > 0) {
            int pos = binding.videoView.getCurrentPosition();
            int to = Math.max(0, pos - 60000);
            binding.videoView.seekTo(to);
            Toast.makeText(this, "Back 60 seconds to " + getTimeStamp(to), Toast.LENGTH_SHORT).show();
        } else if (dir < 0) {
            int pos = binding.videoView.getCurrentPosition();
            int to = Math.min(duration, pos + 60000);
            binding.videoView.seekTo(to);
            Toast.makeText(this, "Ahead 60 seconds to " + getTimeStamp(to), Toast.LENGTH_SHORT).show();
        }
        setupFullScreen();
        return false;
    }
}
