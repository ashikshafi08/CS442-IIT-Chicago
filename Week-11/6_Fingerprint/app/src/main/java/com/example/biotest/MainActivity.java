package com.example.biotest;

import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import com.example.biotest.databinding.ActivityMainBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    //This needs USE_BIOMETRIC permission!

    private final String TAG = getClass().getSimpleName();

    private ActivityResultLauncher<Intent> settingsResultLauncher;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        settingsResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);

        tryFingerprint();

    }

    // Called when fingerprint icon is clicked
    public void doFingerprint(View v) {
        tryFingerprint();
    }


    private void tryFingerprint() {
        if (checkBiometricSupport()) {
            fingerprintReady();
        } else {
            fingerprintNotReady();
        }
    }


    private Boolean checkBiometricSupport() {

        BiometricManager biometricManager = BiometricManager.from(this);
        int canAuth = biometricManager.canAuthenticate(DEVICE_CREDENTIAL);

        if (canAuth == BiometricManager.BIOMETRIC_SUCCESS) {
            return true;
        } else if (canAuth == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            binding.results.setText(R.string.no_hw);
            return false;
        } else if (canAuth == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE) {
            binding.results.setText(R.string.bio_unavail);
            return false;
        } else if (canAuth == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            binding.results.setText(R.string.finger_none);
            return false;
        } else {
            binding.results.setText(R.string.unspec_error);
            return false;
        }
    }

    private void fingerprintNotReady() {
        binding.imageView.setVisibility(View.INVISIBLE);
        binding.promptText.setVisibility(View.INVISIBLE);
        binding.button.setVisibility(View.VISIBLE);
    }

    public void fingerprintReady() {

        //Create a thread pool with a single thread
        Executor newExecutor = Executors.newSingleThreadExecutor();

        //Start listening for authentication events
        //onAuthenticationError is called when a fatal error occurs
        //onAuthenticationSucceeded is called when a fingerprint is matched
        //onAuthenticationFailed is called when the fingerprint does not match

        BiometricPrompt myBiometricPrompt = new BiometricPrompt(this, newExecutor,

                new BiometricPrompt.AuthenticationCallback() {

                    @Override
                    //Called when auth. results in a cancel or error
                    public void onAuthenticationError(final int errorCode, @NonNull final CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        runOnUiThread(() -> {
                            if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                                binding.results.setText(R.string.finger_cxl);
                            } else {
                                binding.results.setText(errString);
                            }
                        });
                    }


                    @Override
                    // Called when a fingerprint is matched successfully
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        runOnUiThread(() -> {
                            binding.results.setText(R.string.finger_ok);
                            binding.button.setVisibility(View.INVISIBLE);
                        });
                    }


                    @Override
                    // Called when the fingerprint does not match
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        runOnUiThread(() -> binding.results.setText(R.string.finger_bad));
                    }
                });

        //Create the BiometricPrompt instance
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint sign in")
                .setSubtitle(" ")
                .setDescription("Sign in using your finger print to see your accounts")
                .setNegativeButtonText("Cancel")
                .build();

        myBiometricPrompt.authenticate(promptInfo);

    }

    public void goToSettings(View v) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
        settingsResultLauncher.launch(intent);
    }

    public void handleResult(ActivityResult result) {

        if (result == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }
        // Nothing is returned here - we'll just try fingerprint checks again
        binding.results.setText("");
        tryFingerprint();
    }

}