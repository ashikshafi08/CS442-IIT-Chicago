package com.christopherhield.viewpager2;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ZodiacAdapter extends
        RecyclerView.Adapter<ZodiacViewHolder> {

    private final MainActivity mainActivity;
    private final ArrayList<Zodiac> zodiacList;

    public ZodiacAdapter(MainActivity mainActivity, ArrayList<Zodiac> zodiacList) {
        this.mainActivity = mainActivity;
        this.zodiacList = zodiacList;
    }

    @NonNull
    @Override
    public ZodiacViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ZodiacViewHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.zodiac_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ZodiacViewHolder holder, int position) {
        Zodiac zodiac = zodiacList.get(position);

        final int resourceId = mainActivity.getResources().
                getIdentifier(zodiac.getName(), "drawable", mainActivity.getPackageName());

        holder.zodiacName.setText(
                MessageFormat.format("{0} {1}", zodiac.getSymbol(), zodiac.getName().toUpperCase()));
        holder.zodiacImage.setImageResource(resourceId);
        holder.zodiacDates.setText(zodiac.getDates());
    }

    @Override
    public int getItemCount() {
        return zodiacList.size();
    }
}