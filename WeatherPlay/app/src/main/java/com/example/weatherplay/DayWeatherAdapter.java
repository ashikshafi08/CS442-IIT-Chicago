package com.example.weatherplay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DayWeatherAdapter extends RecyclerView.Adapter<DayWeatherViewHolder> {

    private final List<DayWeather> dayWeatherList;
    private DayWeatherActivity dayWeatherActivity;

    public DayWeatherAdapter(List<DayWeather> dayWeatherList, DayWeatherActivity dayWeatherActivity){
            this.dayWeatherList = dayWeatherList;
            this.dayWeatherActivity = dayWeatherActivity;

    }

    @NonNull
    @Override
    public DayWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dailyforecast_layout ,
                parent , false);

        return new DayWeatherViewHolder(itemView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull DayWeatherViewHolder holder, int position) {

        DayWeather dayWeather = dayWeatherList.get(position);

        String txtHighLow = dayWeather.getTempMax().toString() + "/" + dayWeather.getTempMin().toString();

        holder.txtHighLowId.setText(dayWeather.getIcon());
        holder.txtUvindexId.setText(dayWeather.getUvindex());
        holder.txtDescriptionId.setText(dayWeather.getDescription());
        holder.txtperciprobId.setText(dayWeather.getPrecipprob());
        // holder.txtDateId
        holder.txtMorningId.setText(dayWeather.getMorningTemp());
        holder.txtAfternoonId.setText(dayWeather.getNoonTemp());
        holder.txtEveningId.setText(dayWeather.getEveTemp());
        holder.txtNightId.setText(dayWeather.getNightTemp());



    }

    @Override
    public int getItemCount() {
        return dayWeatherList.size();
    }
}