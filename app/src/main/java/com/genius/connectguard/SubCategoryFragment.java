package com.genius.connectguard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
                fragmentTransaction.replace(R.id.register_framelayout, new MainFragment());
                fragmentTransaction.commit();

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


    /*public void addCategoriesToRecyclerView(final String mainCategoryName){

        Query query = constants.getDatabaseReference().child("Categories").child("Apple");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                SubcategoryModel subcategoryModel = new SubcategoryModel();
                subcategoryModel.setSubcategoryImage(snapshot.child("CategoryImage").getValue().toString());
                subcategoryModel.setSubcategoryName(snapshot.child("CategoryName").getValue().toString());
                subcategoryModels.add(subcategoryModel);
                subCategoryRecyclerViewAdapter = new SubCategoryRecyclerViewAdapter(subcategoryModels);
                subcategoryRecyclerView.setAdapter(subCategoryRecyclerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */
    /*query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                subcategoryModels.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    SubcategoryModel subcategoryModel = new SubcategoryModel();
                    subcategoryModel.setSubcategoryImage(dataSnapshot.child("modelImage").getValue().toString());
                    subcategoryModel.setSubcategoryName(dataSnapshot.child("modelName").getValue().toString());
                    subcategoryModels.add(subcategoryModel);

                }

                subCategoryRecyclerViewAdapter = new SubCategoryRecyclerViewAdapter(subcategoryModels);
                subcategoryRecyclerView.setAdapter(subCategoryRecyclerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*//*

    }*/

    }
}