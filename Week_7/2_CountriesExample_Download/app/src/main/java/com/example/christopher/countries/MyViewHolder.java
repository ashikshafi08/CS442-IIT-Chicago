package com.example.christopher.countries;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

 class MyViewHolder extends RecyclerView.ViewHolder {

     TextView country;
     TextView capital;
     TextView population;
     TextView region;
     TextView subRegion;

     MyViewHolder(View view) {
        super(view);
        country = view.findViewById(R.id.country);
        capital = view.findViewById(R.id.capital);
        population = view.findViewById(R.id.population);
        region = view.findViewById(R.id.region);
        subRegion = view.findViewById(R.id.subRegion);
    }

}
