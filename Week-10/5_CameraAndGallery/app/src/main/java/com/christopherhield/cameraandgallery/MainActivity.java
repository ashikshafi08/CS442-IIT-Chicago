package com.christopherhield.cameraandgallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Remember uses-feature in manifest
    // Remember INTERNET permission
    // Remember Manifest queries section
    // Add images to gallery via Android Studio Device File Explorer

    private final String TAG = getClass().getSimpleName();

    private File currentImageFile;
    private ImageView imageView;

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private ActivityResultLauncher<Intent> thumbActivityResultLauncher;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleCameraResult);

        thumbActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleThumbResult);

        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleGalleryResult);

        imageView = findViewById(R.id.imageView);
    }

    public void doCamera(View v) {
        try {
            currentImageFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = FileProvider.getUriForFile(
                this, "com.christopherhield.cameraandgallery.fileprovider2", currentImageFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        cameraActivityResultLauncher.launch(takePictureIntent);
    }

    public void doThumb(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        thumbActivityResultLauncher.launch(takePictureIntent);
    }

    public void doGallery(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        galleryActivityResultLauncher.launch(photoPickerIntent);
    }


    public void getJPGValue(View v) {
        // Dialog with a seek bar - just so you see a seekbar example!
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Make the seekbar
        final SeekBar sb = new SeekBar(this);
        sb.setMax(100);
        sb.setProgress(100);

        // Set the seekbar to be the builder's view
        builder.setView(sb);

        builder.setPositiveButton("OK", (dialog, id) -> doConvert(sb.getProgress()));
        builder.setNegativeButton("CANCEL", (dialog, id) -> {
        });

        builder.setMessage("Drag to Set Value (0-100)");
        builder.setTitle("Seek Bar");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void handleCameraResult(ActivityResult result) {
        if (result == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }

        if (result.getResultCode() == RESULT_OK) {
            try {
                processFullCameraImage();
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public void handleThumbResult(ActivityResult result) {
        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }

        if (result.getResultCode() == RESULT_OK) {
            try {
                Intent data = result.getData();
                processCameraThumb(data.getExtras());
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public void handleGalleryResult(ActivityResult result) {
        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }

        if (result.getResultCode() == RESULT_OK) {
            try {
                Intent data = result.getData();
                processGallery(data);
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private void doConvert(int jpgQuality) {
        if (imageView.getDrawable() == null)
            return;

        Bitmap origBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        ByteArrayOutputStream bitmapAsByteArrayStream = new ByteArrayOutputStream();
        origBitmap.compress(Bitmap.CompressFormat.JPEG, jpgQuality, bitmapAsByteArrayStream);

        makeCustomToast(this,
                String.format(Locale.getDefault(), "JPG Quality: %d%% %nImage Size: %,d",
                        jpgQuality, bitmapAsByteArrayStream.size()));
        byte[] byteArray = bitmapAsByteArrayStream.toByteArray();
        Bitmap bitmap =
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        imageView.setImageBitmap(bitmap);

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "image+";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",    /* suffix */
                storageDir      /* directory */
        );
    }

    private void processFullCameraImage() {

        Uri selectedImage = Uri.fromFile(currentImageFile);
        imageView.setImageURI(selectedImage);

        /// The below is not necessary - it's only done for example purposes
        Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        makeCustomToast(this, String.format(Locale.getDefault(),
                "Camera Image Size:%n%,d bytes", bm.getByteCount()));
    }

    private void processGallery(Intent data) {
        Uri galleryImageUri = data.getData();
        if (galleryImageUri == null)
            return;

        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(galleryImageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        imageView.setImageBitmap(selectedImage);
        makeCustomToast(this, String.format(Locale.getDefault(),
                "Gallery Image Size:%n%,d bytes", selectedImage.getByteCount()));

    }

    private void processCameraThumb(Bundle extras) {

        Bitmap imageBitmap = (Bitmap) extras.get("data");
        imageView.setImageBitmap(imageBitmap);

        /// The below is not necessary - it's only done for example purposes
        Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        makeCustomToast(this, String.format(Locale.getDefault(),
                "Camera Image Size:%n%,d bytes", bm.getByteCount()));
    }


    public static void makeCustomToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);

        TextView tv = new TextView(context);
        tv.setText(message);
        tv.setTextSize(18.0f);

        tv.setPadding(50, 25, 50, 25);
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundColor(Color.WHITE);
        toast.setView(tv);

        toast.show();
    }
}
