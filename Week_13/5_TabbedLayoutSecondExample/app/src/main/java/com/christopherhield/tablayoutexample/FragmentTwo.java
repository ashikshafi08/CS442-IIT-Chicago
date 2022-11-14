package com.christopherhield.tablayoutexample;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentTwo extends Fragment 
implements View.OnClickListener {

    private static final String TAG = "FragmentTwo";

    private final List<Person> personList = new ArrayList<>();
    private final List<Person> personFilterList = new ArrayList<>();

    private RecyclerView recyclerView; // Layout's recyclerview
    private ListAdapter mAdapter; // Data to recyclerview adapter
    private EditText toFindText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_two, container, false);

        personList.addAll(DataContainer.getPeopleList());

        if (getActivity() != null)
            getActivity().setTitle("Directory (" + personList.size() + " entries)");

        personFilterList.addAll(personList);

        recyclerView = fragmentView.findViewById(R.id.recycler);
        mAdapter = new ListAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        toFindText = fragmentView.findViewById(R.id.toFind);

        fragmentView.findViewById(R.id.imageView).setOnClickListener(v -> clearSearch());

        toFindText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: ");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: ");
                Log.d(TAG, "afterTextChanged: " + s.length());
                doFilter(s.toString());
            }
        });
        Log.d(TAG, "onCreateView: DATA " + DataContainer.getPeopleList().size());

        return fragmentView;

    }

    private void clearSearch() {
        toFindText.setText("");
    }

    private void doFilter(String filter) {

        personFilterList.clear();
        for (Person p : personList) {
            if (p.getLastFirst().toLowerCase().contains(filter.toLowerCase())) {
                personFilterList.add(p);
            }
        }
        Collections.sort(personFilterList);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Person p = personFilterList.get(pos);

        Log.d(TAG, "onClick: ");

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(p.getfName() + " " + p.getlName() + ",  " + p.getDepartment());
        builder.setMessage(p.getLocation() + ",  " + p.getPhone());


        builder.setPositiveButton("OK", (dialog, id) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    static class ListEntryViewHolder extends RecyclerView.ViewHolder {
        public TextView name;


        ListEntryViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
        }
    }

    ////////////////////////////
    class ListAdapter extends RecyclerView.Adapter<ListEntryViewHolder> {

        @NonNull
        @Override
        public ListEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_entry, parent, false);

            itemView.setOnClickListener(FragmentTwo.this);

            return new ListEntryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ListEntryViewHolder holder, int position) {

            Person p = personFilterList.get(position);

            holder.name.setText(p.getLastFirst());

        }

        @Override
        public int getItemCount() {
            return personFilterList.size();
        }
    }
}
