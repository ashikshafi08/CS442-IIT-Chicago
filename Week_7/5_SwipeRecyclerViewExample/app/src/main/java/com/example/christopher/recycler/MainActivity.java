package com.example.christopher.recycler;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    // The above lines are important - them make this class a listener
    // for click and long click events in the ViewHolders (in the recycler)

    // You need:
    //      implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'

    private final List<Employee> employeeList = new ArrayList<>();  // Main content is here

    private RecyclerView recyclerView; // Layout's recyclerview
    private SwipeRefreshLayout swiper; // The SwipeRefreshLayout

    private EmployeesAdapter mAdapter; // Data to recyclerview adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);

        mAdapter = new EmployeesAdapter(employeeList, this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swiper = findViewById(R.id.swiper);
        swiper.setOnRefreshListener(this::doRefresh);

        // Make some data - just used to fill list
        for (int i = 0; i < 20; i++) {
            employeeList.add(new Employee());
        }
    }

    private void doRefresh() {
        Collections.shuffle(employeeList);
        mAdapter.notifyItemRangeChanged(0, employeeList.size());
        swiper.setRefreshing(false);
        Toast.makeText(this, "List content shuffled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks

        int pos = recyclerView.getChildLayoutPosition(v);
        Employee m = employeeList.get(pos);

        Toast.makeText(v.getContext(), m.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        Employee m = employeeList.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Entry");
        builder.setMessage("Delete '" + m.getName() + "'");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            employeeList.remove(pos);
            mAdapter.notifyItemRemoved(pos);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {});

        builder.create().show();

        return false;
    }

}