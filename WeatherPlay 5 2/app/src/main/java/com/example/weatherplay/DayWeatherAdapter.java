package com.example.weatherplay;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DayWeatherAdapter extends RecyclerView.Adapter<DayWeatherViewHolder> {

    private final List<DayWeather> dayWeatherList;
    private DayWeatherActivity dayWeatherActivity;
    private MainActivity mainActivity;

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

        holder.txtHighLowId.setText(dayWeather.getTxtHighLow());
        holder.txtDateId.setText(dayWeather.getDatetimeEpoch());
        holder.txtUvindexId.setText(String.format("UV Index: %s", dayWeather.getUvindex()));
        holder.txtDescriptionId.setText(dayWeather.getDescription());
        holder.txtperciprobId.setText("(" + dayWeather.getPrecipprob() + "%" + "percip."+  ")");

        // holder.txtDateId

        holder.txtMorningId.setText(dayWeather.getMorningTemp());
        holder.txtAfternoonId.setText(dayWeather.getNoonTemp());
        holder.txtEveningId.setText(dayWeather.getEveTemp());
        holder.txtNightId.setText(dayWeather.getNightTemp());
//
        int iconID = returnIcon(dayWeather.getIcon());
        if (iconID != 0) {
            holder.imgWeatherIconId.setImageResource(iconID);
        }




       // holder.firstView.setText(dayWeather.getPrecipprob());
        // holder.secondView.setText(dayWeather.getUvindex());


    }

    @Override
    public int getItemCount() {
        return dayWeatherList.size();
    }

    private int returnIcon(String iconName) {
        String icon = iconName;
        icon = icon.replace("-", "_"); // Replace all dashes with underscores
        int iconID = dayWeatherActivity.getResources().getIdentifier(icon, "drawable", dayWeatherActivity.getPackageName());
        if (iconID == 0) {
            String TAG = "DayWeatherAdapter";
            Log.d(TAG, "Icon Error: CANNOT FIND ICON " + icon);
            return 0;
        }

        return iconID;
    }
}