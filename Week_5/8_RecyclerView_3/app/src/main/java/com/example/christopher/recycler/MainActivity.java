package com.example.christopher.recycler;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity // NOTE the interfaces here!
        implements View.OnClickListener {

    // The above lines are important - them make this class a listener
    // for click and long click events in the ViewHolders (in the recycler

    private final List<String> digits = new ArrayList<>();
    private RecyclerView recyclerView; // Layout's recyclerview
    private static final String piStr =
            "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.hourlyRecycler);
        // Data to recyclerview adapter
        DigitAdapter mAdapter = new DigitAdapter(digits, this);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        //Make some data - not always needed - just used to fill list
        for (int i = 0; i < piStr.length(); i++) {
            digits.add("" + piStr.charAt(i));
        }
    }

    // From OnClickListener
    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        String m = digits.get(pos);
        Toast.makeText(v.getContext(),
                String.format(Locale.getDefault(), "Digit %d of PI is %s", pos, m),
                        Toast.LENGTH_SHORT).show();
    }

}