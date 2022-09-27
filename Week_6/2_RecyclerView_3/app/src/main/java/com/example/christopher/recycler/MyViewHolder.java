package com.example.christopher.recycler;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView value;

    MyViewHolder(View view) {
        super(view);
        value = view.findViewById(R.id.value);
    }

}
