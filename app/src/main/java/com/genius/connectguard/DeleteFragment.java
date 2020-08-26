package com.genius.connectguard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.genius.constants.constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class DeleteFragment extends Fragment {

    Toolbar deleteToolbar;
    Spinner selectCategorySpinner;
    Spinner selectSubcategorySpinner;
    Spinner selectProductSpinner;
    String mainCategory;
    Button deleteProductButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_delete, container, false);

        addCategoriesToCategoriesSpinner(view);
        selectCategorySpinner = view.findViewById(R.id.select_category_spinner_in_delete);
        selectCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

                mainCategory = selectCategorySpinner.getSelectedItem().toString();
                addSubcategoriesToSubcategoriesSpinner(view, selectCategorySpinner.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectSubcategorySpinner = view.findViewById(R.id.select_model_spinner_in_delete);
        selectSubcategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

                addProductsToProductsSpinner(view, mainCategory, selectSubcategorySpinner.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        deleteToolbar = view.findViewById(R.id.delete_toolbar);
        deleteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
                fragmentTransaction.replace(R.id.register_framelayout, new MainFragment());
                fragmentTransaction.commit();

            }
        });

        deleteProductButton = view.findViewById(R.id.delete_product_button);
        deleteProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeProduct(selectCategorySpinner.getSelectedItem().toString(), selectSubcategorySpinner.getSelectedItem().toString(), selectProductSpinner.getSelectedItem().toString());
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
                fragmentTransaction.replace(R.id.register_framelayout, new MainFragment());
                fragmentTransaction.commit();

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

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    categoriesSpinnerArray.add(dataSnapshot.getKey());

                }

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectCategorySpinner = (Spinner) view.findViewById(R.id.select_category_spinner_in_delete);
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

    public void addSubcategoriesToSubcategoriesSpinner(final View view, final String mainCategory){

        if (!mainCategory.equals(getResources().getString(R.string.please_add_category))){

            final List<String> subcategoriesSpinnerArray = new ArrayList<String>();

            constants.getDatabaseReference().child("Categories").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    subcategoriesSpinnerArray.clear();

                    for (DataSnapshot dataSnapshot : snapshot.child(mainCategory).getChildren()){

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                            subcategoriesSpinnerArray.add(dataSnapshot.child("modelName").getValue(String.class));

                        }

                    }

                    LinkedHashSet<String> hashSet = new LinkedHashSet<String>(subcategoriesSpinnerArray);

                    ArrayList<String> spinnerWithoutDuplicates = new ArrayList<>(hashSet);

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, spinnerWithoutDuplicates);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selectSubcategorySpinner = (Spinner) view.findViewById(R.id.select_model_spinner_in_delete);
                    if (spinnerWithoutDuplicates.size() == 0){

                        spinnerWithoutDuplicates.add(0, getResources().getString(R.string.please_add_subcategory));

                    }else if (spinnerWithoutDuplicates.size() > 0){

                        if (isAdded() || isVisible()){

                            spinnerWithoutDuplicates.remove(getResources().getString(R.string.please_add_subcategory));

                        }

                    }
                    selectSubcategorySpinner.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    public void addProductsToProductsSpinner(final View view, final String mainCategory, final String mainSubcategory){

        if (!mainCategory.equals(getResources().getString(R.string.please_add_category)) || !mainSubcategory.equals(getResources().getString(R.string.please_add_subcategory))){

            final List<String> productsSpinnerArray = new ArrayList<String>();

            constants.getDatabaseReference().child("Categories").child(mainCategory).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    productsSpinnerArray.clear();

                    for (DataSnapshot dataSnapshot : snapshot.child(mainSubcategory).getChildren()){

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                            productsSpinnerArray.add(dataSnapshot.child("productStandardName").getValue(String.class));

                        }

                    }

                    LinkedHashSet<String> hashSet = new LinkedHashSet<String>(productsSpinnerArray);

                    ArrayList<String> spinnerWithoutDuplicates = new ArrayList<>(hashSet);

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, spinnerWithoutDuplicates);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selectProductSpinner = (Spinner) view.findViewById(R.id.select_product_spinner_in_delete);
                    if (spinnerWithoutDuplicates.size() == 0){

                        spinnerWithoutDuplicates.add(0, getResources().getString(R.string.please_add_product));

                    }else if (spinnerWithoutDuplicates.size() > 0){

                        if (isAdded() || isVisible()){

                            spinnerWithoutDuplicates.remove(getResources().getString(R.string.please_add_product));

                        }

                    }
                    selectProductSpinner.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    public void removeProduct(String mainCategory, String mainSubcategory, String product){

        constants.getDatabaseReference().child("Categories").child(mainCategory).child(mainSubcategory).child(product).removeValue();

    }

}