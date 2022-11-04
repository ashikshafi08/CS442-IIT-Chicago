package com.christopherhield.geography;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class CountryAdapter extends
        RecyclerView.Adapter<CountryViewHolder> {

    private final MainActivity mainActivity;
    private final ArrayList<Country> countryList;

    public CountryAdapter(MainActivity mainActivity, ArrayList<Country> countryList) {
        this.mainActivity = mainActivity;
        this.countryList = countryList;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CountryViewHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.country_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country c = countryList.get(position);

        holder.country.setText(c.getName());
        holder.region.setText(String.format(Locale.getDefault(),
                "%s (%s)", c.getRegion(), c.getSubRegion()));
        holder.capital.setText(c.getCapital());
        holder.population.setText(String.format(Locale.getDefault(), "%,d", c.getPopulation()));
        holder.area.setText(String.format(Locale.getDefault(),"%,d sq km", c.getArea()));
        holder.citizen.setText(c.getCitizen());
        holder.borders.setText(c.getBorders());
        holder.flag.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        holder.flag.setImageDrawable(c.getDrawable());
        holder.flag.setOnClickListener(v -> clickFlag(c.getName()));

        holder.pageNum.setText(String.format(
                Locale.getDefault(),"%d of %d", (position+1), countryList.size()));
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    private void clickFlag(String name) {
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(name));
        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
        mainActivity.startActivity(intent);
    }
}