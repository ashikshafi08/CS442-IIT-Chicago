package com.example.christopher.recycler;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView empId;
    TextView department;

    MyViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.name);
        empId = view.findViewById(R.id.empId);
        department = view.findViewById(R.id.department);
    }

}
