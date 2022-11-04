package com.christopherhield.geography;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CountryViewHolder extends RecyclerView.ViewHolder {

    TextView country;
    TextView region;
    TextView capital;
    TextView population;
    TextView area;
    TextView citizen;
    TextView borders;
    ImageView flag;
    TextView pageNum;

    public CountryViewHolder(@NonNull View itemView) {
        super(itemView);
        country = itemView.findViewById(R.id.country);
        region = itemView.findViewById(R.id.region);
        capital = itemView.findViewById(R.id.capital);
        population = itemView.findViewById(R.id.population);
        area = itemView.findViewById(R.id.area);
        citizen = itemView.findViewById(R.id.citizens);
        borders = itemView.findViewById(R.id.borders);
        flag = itemView.findViewById(R.id.flagImage);
        pageNum = itemView.findViewById(R.id.page_num);
    }
}
