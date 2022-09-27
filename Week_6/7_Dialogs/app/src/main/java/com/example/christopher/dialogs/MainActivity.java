package com.example.christopher.dialogs;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.textView1);
        tv2 = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        tv4 = findViewById(R.id.textView4);
        tv5 = findViewById(R.id.textView5);
        tv6 = findViewById(R.id.textView6);
    }

    public void click1(View v) {
        // Single input value dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create an edittext and set it to be the builder's view
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);

        builder.setIcon(R.drawable.icon1);

        // lambda can be used here (as is below)
        builder.setPositiveButton("OK", (dialog, id) -> tv1.setText(et.getText()));

        // lambda can be used here (as is below)
        builder.setNegativeButton("NO WAY", (dialog, id) -> {
            Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            tv1.setText(getString(R.string.no_way));
        });

        builder.setMessage("Please enter a value:");
        builder.setTitle("Single Input");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void click2(View v) {
        // List selection dialog

        //ake an array of strings
        final CharSequence[] sArray = new CharSequence[20];
        for (int i = 0; i < 20; i++)
            sArray[i] = "Choice " + i;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make a selection");
        builder.setIcon(R.drawable.icon2);

        // Set the builder to display the string array as a selectable
        // list, and add the "onClick" for when a selection is made
        builder.setItems(sArray, (dialog, which) -> tv2.setText(sArray[which]));

        builder.setNegativeButton("Nevermind", (dialog, id) -> {
            Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            tv2.setText(getString(R.string.nevermind_selected));
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void click3(View v) {
        // Dialog with a layout

        // Inflate the dialog's layout
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enter the values:");
        builder.setTitle("Dialog Layout");
        builder.setIcon(R.drawable.icon3);

        // Set the inflated view to be the builder's view
        builder.setView(view);

        builder.setPositiveButton("OK", (dialog, id) -> {

            // Multiply the 2 values together and display the results
            EditText et1 = view.findViewById(R.id.textL);
            EditText et2 = view.findViewById(R.id.textW);
            double l = 0.0;
            if (et1.getText().length() != 0)
                l = Double.parseDouble(et1.getText().toString());
            double w = 0.0;
            if (et2.getText().length() != 0)
                w = Double.parseDouble(et2.getText().toString());
            tv3.setText(String.format(Locale.US, "%.2f", l * w));
        });
        builder.setNegativeButton("CANCEL", (dialog, id) -> {
            Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            tv3.setText(getString(R.string.cxl_selected));

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void click4(View v) {
        // Dialog with a seek bar - just so you see a seekbar example!
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Make the seekbar
        final SeekBar sb = new SeekBar(this);
        sb.setMax(100);

        // Set the seekbar to be the builder's view
        builder.setView(sb);
        builder.setIcon(R.drawable.icon4);

        builder.setPositiveButton("OK", (dialog, id) -> tv4.setText(String.format(Locale.US, "%d%%", sb.getProgress())));
        builder.setNegativeButton("CANCEL", (dialog, id) -> {
            Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            tv4.setText(getString(R.string.cxl_selected));

        });
        builder.setMessage("Drag to Set Value");
        builder.setTitle("Seek Bar");

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void click5(View v) {
        // Simple Ok & Cancel dialog - no view used.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.icon1);

        builder.setPositiveButton("OK", (dialog, id) -> tv5.setText(getString(R.string.ok_selected)));
        builder.setNegativeButton("CANCEL", (dialog, id) -> tv5.setText(getString(R.string.cxl_selected)));

        builder.setMessage("Do some task?");
        builder.setTitle("Yes/No Dialog");

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void click6(View v) {
        // Simple dialog - ok button.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.icon1);

        builder.setPositiveButton("OK", (dialog, id) -> tv6.setText(getString(R.string.ok_selected)));

        builder.setMessage("Here is useful info.");
        builder.setTitle("Ok Dialog");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void click7(View v) {
        // Simple dialog - no buttons.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.icon1);

        builder.setMessage("Here is useful info.");
        builder.setTitle("Info Dialog");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
