package com.christopherhield.tablayoutexample;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class FragmentOne extends Fragment {

    private static final String TAG = "FragmentOne";
    private View view;
    private AutoCompleteTextView toFindText;
    private ArrayList<Person> fullPeople;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_one, container, false);

        view.findViewById(R.id.cardView).setVisibility(View.INVISIBLE);

        view.findViewById(R.id.button).setOnClickListener(v -> doSearch());
        view.findViewById(R.id.imageView).setOnClickListener(v -> clearSearch());
        toFindText = view.findViewById(R.id.toFind);
        toFindText.setThreshold(2);
        toFindText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: ");
                if (before == 0)
                    view.findViewById(R.id.cardView).setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: ");
            }
        });
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setPeopleList(DataContainer.getPeopleList());
        Log.d(TAG, "onViewCreated: ");
    }

    private void setPeopleList(ArrayList<Person> pList) {
        fullPeople = new ArrayList<>(pList);

        String[] justNames = new String[fullPeople.size()];
        for (int i = 0; i < fullPeople.size(); i++) {
            justNames[i] = fullPeople.get(i).getLastFirst();
        }

        if (getActivity() == null) return;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, justNames);

        toFindText.setAdapter(adapter);

        toFindText.setOnItemClickListener((parent, view, position, id) -> {
            hideKeyboard();
            doSearch();
        });
    }

    private void clearSearch() {
        toFindText.setText("");
    }

    private void doSearch() {

        String toFind = toFindText.getText().toString();
        if (toFind.trim().isEmpty())
            return;

        Person results = null;

        for (Person p : fullPeople) {
            if (p.getLastFirst().toLowerCase().contains(toFind.trim().toLowerCase())) {
                results = p;
                break;
            }
        }

        if (results != null)
            setContent(results);

        toFindText.setText("");

    }

    public void hideKeyboard() {
        if (getActivity() == null) return;

        InputMethodManager imm = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setContent(Person p) {
        view.findViewById(R.id.cardView).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.name)).setText(String.format("%s %s", p.getfName(), p.getlName()));
        ((TextView) view.findViewById(R.id.dept)).setText(p.getDepartment());
        ((TextView) view.findViewById(R.id.loc)).setText(p.getLocation());
        ((TextView) view.findViewById(R.id.phone)).setText(p.getPhone());
    }


}
