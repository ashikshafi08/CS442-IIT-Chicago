package com.example.weatherplay;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyViewHolder> {

    private static final String TAG = "MainActivity";
    private final List<HourlyWeather> hourlyWeatherList;
    private Context context;
    private MainActivity mainActivity;

    public HourlyWeatherAdapter(List<HourlyWeather> hourlyWeatherArrayList, MainActivity mainActivity) {
        this.hourlyWeatherList =hourlyWeatherArrayList ;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_dataview ,
                parent , false);

        return new HourlyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {

            HourlyWeather hourlyWeather = hourlyWeatherList.get(position); // starting from 0th index

            holder.txtDayId.setText(hourlyWeather.getDay());
            holder.txtTimeid.setText(hourlyWeather.getTime());
            holder.txtTemperatureId.setText(hourlyWeather.getTemp());
            holder.txtDescriptionId.setText(hourlyWeather.getConditions());

            int iconId = returnIcon(hourlyWeather.getIcon());
            if (iconId !=0) {
                holder.imageWeatherIconId.setImageResource(iconId);
            }




    }

    @Override
    public int getItemCount() {
        return hourlyWeatherList.size();
    }

    private int returnIcon(String iconName) {
        String icon = iconName;
        icon = icon.replace("-", "_"); // Replace all dashes with underscores
        int iconID = mainActivity.getResources().getIdentifier(icon, "drawable", mainActivity.getPackageName());
        if (iconID == 0) {
            String TAG = "HourlyWeatherAdapter";
            Log.d(TAG, "Icon Error: CANNOT FIND ICON " + icon);
            return 0;
        }

        return iconID;
    }
}
