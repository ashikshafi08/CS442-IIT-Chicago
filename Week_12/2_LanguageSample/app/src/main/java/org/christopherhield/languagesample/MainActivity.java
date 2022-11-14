package org.christopherhield.languagesample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String lang = Locale.getDefault().getDisplayLanguage();
        ((TextView) findViewById(R.id.lang_text)).setText(lang);

        String date = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
        NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());

        ((TextView) findViewById(R.id.date_number)).
                setText(String.format("%s (%s)", date, nf.format(1234567.89)));

    }

    public void buttonClick(View v) {
        Toast.makeText(this, getString(R.string.button_click), Toast.LENGTH_SHORT).show();
    }
}
