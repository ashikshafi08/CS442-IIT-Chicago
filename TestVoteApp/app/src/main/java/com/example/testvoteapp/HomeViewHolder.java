package com.example.testvoteapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeViewHolder extends RecyclerView.ViewHolder {
    TextView officeText , namePartyText;
    ImageView officialImg;

    public HomeViewHolder(@NonNull View itemView) {
        super(itemView);

        officeText = itemView.findViewById(R.id.officeId);
        namePartyText = itemView.findViewById(R.id.namePartyId);
    }
}
