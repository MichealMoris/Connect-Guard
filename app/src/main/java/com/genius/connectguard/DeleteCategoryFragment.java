package com.genius.connectguard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.genius.constants.constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeleteCategoryFragment extends Fragment {

    Spinner selectCategorySpinner;
    Toolbar toolbar;
    Button deleteButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_delete_category, container, false);

        toolbar = view.findViewById(R.id.delete_category_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("MainActivity", "popping backstack");
                    fm.popBackStack();
                }

            }
        });

        addCategoriesToCategoriesSpinner(view);
        selectCategorySpinner = view.findViewById(R.id.select_category_spinner_in_delete_category);

        deleteButton = view.findViewById(R.id.delete_category_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeProduct(selectCategorySpinner.getSelectedItem().toString());
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                getActivity().overridePendingTransition(R.anim.fade_out_anim, R.anim.fade_in_anim);
                startActivity(intent);
                getActivity().finish();

            }
        });

        return view;

    }

    public void addCategoriesToCategoriesSpinner(final View view){

        final List<String> categoriesSpinnerArray = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categoriesSpinnerArray);

        constants.getDatabaseReference().child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                categoriesSpinnerArray.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    categoriesSpinnerArray.add(dataSnapshot.getKey());

                }

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectCategorySpinner = (Spinner) view.findViewById(R.id.select_category_spinner_in_delete_category);
                if (categoriesSpinnerArray.size() == 0){

                    categoriesSpinnerArray.add(0, getResources().getString(R.string.please_add_category));

                }else if (categoriesSpinnerArray.size() > 0){

                    if (isAdded() || isVisible()){

                        categoriesSpinnerArray.remove(getResources().getString(R.string.please_add_category));

                    }

                }
                selectCategorySpinner.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void removeProduct(String mainCategory){

        constants.getDatabaseReference().child("Categories").child(mainCategory).removeValue();

    }

}