package com.example.christopher.countries;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public class CountryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);

        Intent intent = getIntent();
        if (intent.hasExtra(Country.class.getName())) {

            Country c = (Country) intent.getSerializableExtra(Country.class.getName());
            if (c == null)
                return;

            TextView country = findViewById(R.id.country);
            country.setText(c.getName());

            TextView region = findViewById(R.id.region);
            region.setText(String.format("%s (%s)", c.getRegion(), c.getSubRegion()));

            TextView capital = findViewById(R.id.capital);
            capital.setText(c.getCapital());

            TextView population = findViewById(R.id.population);
            population.setText(String.format(Locale.US, "%,d", c.getPopulation()));

            TextView area = findViewById(R.id.area);
            area.setText(String.format(Locale.US, "%,d sq km", c.getArea()));

            TextView citizen = findViewById(R.id.citizens);
            citizen.setText(c.getCitizen());

            TextView codes = findViewById(R.id.codes);
            codes.setText(c.getCallingCodes());

            TextView borders = findViewById(R.id.borders);
            borders.setText(c.getBorders());

        }

    }
}
