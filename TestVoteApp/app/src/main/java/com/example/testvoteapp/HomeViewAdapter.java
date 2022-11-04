package com.example.testvoteapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeViewAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private final List<Civic> officialList;
    private MainActivity mainActivity;

    public HomeViewAdapter(List<Civic> officialList , MainActivity mainActivity) {
        this.officialList = officialList;
        this.mainActivity = mainActivity;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_view , parent ,
                false);

        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

        Civic newCiviObj = officialList.get(position);
        holder.officeText.setText(newCiviObj.getOfficeName());

        String officialName = newCiviObj.getOfficialName();
        String partyName = newCiviObj.getParty();

        String officialPartyName = officialName + "(" +  partyName + ")";
        holder.namePartyText.setText(officialPartyName);


    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
