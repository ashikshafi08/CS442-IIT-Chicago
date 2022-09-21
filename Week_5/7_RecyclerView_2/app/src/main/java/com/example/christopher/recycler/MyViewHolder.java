package com.example.christopher.recycler;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    TextView empId;
    TextView department;
    TextView dateTime;

    MyViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.name);
        empId = view.findViewById(R.id.empId);
        department = view.findViewById(R.id.department);
        dateTime = view.findViewById(R.id.dateTime);
    }

}
