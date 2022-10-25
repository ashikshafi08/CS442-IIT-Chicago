package com.example.weatherplay;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DayWeatherViewHolder extends RecyclerView.ViewHolder {

    TextView txtDateId,txtHighLowId,txtUvindexId,txtDescriptionId,txtperciprobId,txtMorningId,txtAfternoonId,txtEveningId,txtNightId;
    ImageView imgWeatherIconId;

    public DayWeatherViewHolder(@NonNull View itemView) {
        super(itemView);

        txtHighLowId = itemView.findViewById(R.id.txtHighLow);
        txtUvindexId = itemView.findViewById(R.id.txtUvindex);
        txtDescriptionId = itemView.findViewById(R.id.txtDescription);
        txtperciprobId = itemView.findViewById(R.id.txtperciprob);
        txtMorningId = itemView.findViewById(R.id.txtMorning);
        txtAfternoonId = itemView.findViewById(R.id.txtAfternoon);
        txtEveningId = itemView.findViewById(R.id.txtEvening);
        txtNightId = itemView.findViewById(R.id.txtNight);
        //imgWeatherIconId = itemView.findViewById(R.id.imgWeatherIcon);

    }
}