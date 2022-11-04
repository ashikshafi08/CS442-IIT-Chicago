package com.christopherhield.viewpager2;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ZodiacViewHolder extends RecyclerView.ViewHolder {

    TextView zodiacName;
    ImageView zodiacImage;
    TextView zodiacDates;

    public ZodiacViewHolder(@NonNull View itemView) {
        super(itemView);
        zodiacName = itemView.findViewById(R.id.zodiacName);
        zodiacImage = itemView.findViewById(R.id.zodiacImage);
        zodiacDates = itemView.findViewById(R.id.zodiacDates);
    }
}
