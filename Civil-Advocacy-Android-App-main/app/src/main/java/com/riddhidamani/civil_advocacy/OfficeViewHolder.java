package com.riddhidamani.civil_advocacy;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficeViewHolder extends RecyclerView.ViewHolder {

    TextView office;
    TextView partyName;

    public OfficeViewHolder(@NonNull View view) {
        super(view);
        office = itemView.findViewById(R.id.office);
        partyName = itemView.findViewById(R.id.nameParty);
    }
}