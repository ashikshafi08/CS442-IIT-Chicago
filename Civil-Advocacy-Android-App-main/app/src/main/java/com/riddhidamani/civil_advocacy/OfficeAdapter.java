package com.riddhidamani.civil_advocacy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfficeAdapter extends RecyclerView.Adapter<OfficeViewHolder>{

    private static final String TAG = "OfficeAdapter";
    private List<Office> officialList;
    private MainActivity mainActivity;

    public OfficeAdapter(List<Office> officialList, MainActivity mainActivity){
        this.officialList = officialList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public OfficeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Making a new ViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.office_entry, parent, false);
        view.setOnClickListener(mainActivity);
        view.setOnLongClickListener(mainActivity);
        return new OfficeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficeViewHolder holder, int position) {
        Office o = officialList.get(position);
        holder.office.setText(o.getOffice());
        holder.partyName.setText(o.getName() + " (" + o.getParty() + ")");
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
