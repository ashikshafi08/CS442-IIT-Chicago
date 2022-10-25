package com.example.weatherplay;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HourlyViewHolder extends RecyclerView.ViewHolder {

    TextView txtDayId,txtTimeid,txtTemperatureId,txtDescriptionId, txtIconId;
    // ImageView imgWeather;
    LinearLayout linearLayout;

    public HourlyViewHolder(@NonNull View itemView) {
        super(itemView);

        txtDayId = itemView.findViewById(R.id.txtDay);
        txtTemperatureId=itemView.findViewById(R.id.txtTemperature);
        txtDescriptionId=itemView.findViewById(R.id.txtDescription);
        txtTimeid=itemView.findViewById(R.id.txtTime);
        txtIconId=itemView.findViewById(R.id.imgWeather);




    }

}
