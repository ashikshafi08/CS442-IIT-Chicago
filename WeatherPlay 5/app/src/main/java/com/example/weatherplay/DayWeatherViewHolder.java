package com.example.weatherplay;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DayWeatherViewHolder extends RecyclerView.ViewHolder {

    TextView txtDateId,txtHighLowId,txtUvindexId,txtDescriptionId,txtperciprobId,txtMorningId,txtAfternoonId,txtEveningId,txtNightId;
    ImageView imgWeatherIconId;
    TextView firstView, secondView;

    public DayWeatherViewHolder(@NonNull View itemView) {
        super(itemView);

        txtHighLowId = itemView.findViewById(R.id.highLowTxt);
        txtUvindexId = itemView.findViewById(R.id.uvIndexTxt);
        txtDescriptionId = itemView.findViewById(R.id.descTxt);
        txtperciprobId = itemView.findViewById(R.id.precipProbTxt);
        txtMorningId = itemView.findViewById(R.id.morningTempTxt);
        txtAfternoonId = itemView.findViewById(R.id.noonTempTxt);
        txtEveningId = itemView.findViewById(R.id.eveTempTxt);
        txtNightId = itemView.findViewById(R.id.nightTempTxt);

       //  txtNightId = itemView.findViewById(R.id.nightTempTxt);


       // firstView = itemView.findViewById(R.id.firstViewId);
        // secondView = itemView.findViewById(R.id.secondViewId);

    }
}