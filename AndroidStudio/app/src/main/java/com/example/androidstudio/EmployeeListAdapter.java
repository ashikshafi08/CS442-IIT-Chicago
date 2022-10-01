package com.example.androidstudio;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeListAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final List<Employee> employeeList;
    private final MainActivity mainAct;

    public EmployeeListAdapter(List<Employee> employeeList, MainActivity mainAct) {
        this.employeeList = employeeList;
        this.mainAct = mainAct;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_entry ,
                parent , false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Employee employee = employeeList.get(position);
        holder.name.setText(employee.getName());
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }
}
