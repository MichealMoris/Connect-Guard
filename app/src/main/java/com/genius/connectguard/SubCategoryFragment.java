package com.genius.connectguard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.genius.constants.constants;
import com.genius.models.CategoryModel;
import com.genius.models.SubcategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryFragment extends Fragment {

    Toolbar SubcategoryToolbar;
    RecyclerView subcategoryRecyclerView;
    SubCategoryRecyclerViewAdapter subCategoryRecyclerViewAdapter;
    List<SubcategoryModel> subcategoryModelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_category, container, false);

        subcategoryRecyclerView = view.findViewById(R.id.subcategory_recyclerview);
        subcategoryRecyclerView.setHasFixedSize(true);
        subcategoryRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        addSubcategoriesToRecyclerView(view,getArguments().getString("MainCategoryName"));

        SubcategoryToolbar = view.findViewById(R.id.subcategory_toolbar);
        SubcategoryToolbar.setTitle(getArguments().getString("MainCategoryName"));
        SubcategoryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("MainActivity", "popping backstack");
                    fm.popBackStack();
                }

            }
        });

        return view;
    }

    public void addSubcategoriesToRecyclerView(final View view, final String mainCategoryName) {

        Query query = constants.getDatabaseReference().child("Categories");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                subcategoryModelList.clear();

                for (DataSnapshot categoriesSnapshot : snapshot.child(mainCategoryName).getChildren()) {

                    SubcategoryModel subcategoryModel = new SubcategoryModel();
                    subcategoryModel.setSubcategoryName(categoriesSnapshot.child("modelName").getValue(String.class));
                    subcategoryModel.setSubcategoryImage(categoriesSnapshot.child("modelImage").getValue(String.class));
                    subcategoryModel.setMainCategoryName(categoriesSnapshot.child("modelCategory").getValue(String.class));
                    subcategoryModelList.add(subcategoryModel);
                    for (int i = 0; i < subcategoryModelList.size(); i++){

                        if (subcategoryModelList.get(i).getSubcategoryName() == null){

                            subcategoryModelList.remove(i);

                        }

                    }

                }


                subCategoryRecyclerViewAdapter = new SubCategoryRecyclerViewAdapter(getActivity(), mainCategoryName, subcategoryModelList);
                subcategoryRecyclerView.setAdapter(subCategoryRecyclerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }
}