package com.example.testvoteapp;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeViewAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private static final String TAG = "HomeViewAdapter: ";
    private final ArrayList<Civic> civicArrayList;
    private final MainActivity mainActivity;
    private final String placeholderUrl = "https://pdtxar.com/wp-content/uploads/2019/04/person-placeholder.jpg";

    public HomeViewAdapter(ArrayList<Civic> civicArrayList , MainActivity mainActivity) {
        this.civicArrayList = civicArrayList;
        this.mainActivity = mainActivity;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_view , parent ,
                false);

        itemView.setOnClickListener(mainActivity);

        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

        Civic newCivicObj = civicArrayList.get(position);
        holder.officeText.setText(newCivicObj.getOfficeName());

        String officialName = newCivicObj.getOfficialName();
        String partyName = newCivicObj.getParty();


        String officialPrevImageUrl = newCivicObj.getPhotoUrl();
//        if (officialPrevImageUrl != null ){
//            Log.d(TAG , officialPrevImageUrl);
//        }else{
//           Log.d(TAG , "Null or Broken");
//
//        }

        Picasso picasso = Picasso.get();
        picasso.load(officialPrevImageUrl).error(R.drawable.brokenimage).placeholder(R.drawable.missing).into(holder.officialImgPrev);



        String officialPartyName = officialName + "(" +  partyName + ")";
        holder.namePartyText.setText(officialPartyName);

        // Setting the image
//        int iconID = mainActivity.getResources().getIdentifier("brokenimage", "drawable", mainActivity.getPackageName());
//        holder.officialImgPrev.setImageResource(iconID);


    }

    @Override
    public int getItemCount() {
        return civicArrayList.size();
    }
}
