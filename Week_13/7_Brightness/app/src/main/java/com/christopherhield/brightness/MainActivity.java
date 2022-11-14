package com.christopherhield.brightness;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private TextView textView;
    private SeekBar seekBar;
    private Integer originalBrightness;
    private ActivityResultLauncher<Intent> settingsResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(255);
        seekBar.setEnabled(false);
        textView = findViewById(R.id.textView);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, i);
                textView.setText(MessageFormat.format("{0} {1}", getString(R.string.brightness), i));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        askPermission();

    }

    public void askPermission() {
        if (Settings.System.canWrite(this)) {
            try {
                seekBar.setEnabled(true);
                int brightness =
                        Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                if (originalBrightness == null) {
                    originalBrightness = brightness;
                }
                textView.setText(MessageFormat.format("{0} {1}", getString(R.string.brightness), brightness));
                seekBar.setProgress(brightness);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.perm_needed);
            builder.setMessage(R.string.perm_message);
            builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                settingsResultLauncher.launch(intent);
            });
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            });

            builder.show();
        }
    }

    public void handleResult(ActivityResult result) {

        if (result == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }
        // Nothing is returned here - we'll just try again
        askPermission();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Settings.System.canWrite(this)) {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, originalBrightness);
        }
    }
}