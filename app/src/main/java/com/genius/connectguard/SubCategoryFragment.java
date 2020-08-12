package com.genius.connectguard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    List<SubcategoryModel> subcategoryModelList = new ArrayList<>();
    RecyclerView subcategoryRecyclerView;
    SubCategoryRecyclerViewAdapter subCategoryRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_category, container, false);


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

    public void addSubcategoriesToRecyclerView(final View view, final String mainCategoryName){

        Query query = constants.getDatabaseReference().child("Categories").child(mainCategoryName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                subcategoryModelList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        subcategoryModelList.clear();
                        SubcategoryModel subcategoryModel = new SubcategoryModel();
                        subcategoryModel.setSubcategoryName(dataSnapshot.child("modelName").getValue().toString());
                        subcategoryModel.setSubcategoryImage(dataSnapshot.child("modelImage").getValue().toString());
                        subcategoryModelList.add(subcategoryModel);

                    }

                }

                subcategoryRecyclerView = view.findViewById(R.id.subcategory_recyclerview);
                subcategoryRecyclerView.setHasFixedSize(true);
                subcategoryRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                subCategoryRecyclerViewAdapter = new SubCategoryRecyclerViewAdapter(getActivity(), mainCategoryName,subcategoryModelList);
                subcategoryRecyclerView.setAdapter(subCategoryRecyclerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        subcategoryModelList.clear();

    }

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