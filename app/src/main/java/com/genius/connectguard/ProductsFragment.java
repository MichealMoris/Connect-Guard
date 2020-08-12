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
import com.genius.models.SubcategoryModel;
import com.genius.models.productModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    Toolbar productsToolbar;
    List<productModel> productList = new ArrayList<>();
    RecyclerView productsRecyclerview;
    ProductsRecyclerviewAdapter productsRecyclerviewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        addProductsToRecyclerview(view, getArguments().getString("MainCategoryName2"), getArguments().getString("MainSubCategoryName"));

        productsToolbar = view.findViewById(R.id.products_toolbar);
        productsToolbar.setTitle(getArguments().getString("MainSubCategoryName"));
        productsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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


    public void addProductsToRecyclerview(final View view, String mainCategory, final String mainModel){

        Query query = constants.getDatabaseReference().child("Categories").child(mainCategory).child(mainModel);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                productList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        productList.clear();
                        productModel productModel = new productModel();
                        productModel.setProductName(dataSnapshot.child("productName").getValue().toString());
                        productModel.setProductImage(dataSnapshot.child("productImage").getValue().toString());
                        productModel.setProductDescription(dataSnapshot.child("productDescription").getValue().toString());
                        productModel.setProductPrice(dataSnapshot.child("productPrice").getValue().toString());
                        productModel.setProductStock(Integer.parseInt(dataSnapshot.child("productStock").getValue().toString()));
                        if (Integer.parseInt(dataSnapshot.child("productStock").getValue().toString()) == 0){

                            productModel.setSoldOut(true);

                        }
                        productList.add(productModel);

                    }

                }

                productsRecyclerview = view.findViewById(R.id.product_recyclerview);
                productsRecyclerview.setHasFixedSize(true);
                productsRecyclerview.setLayoutManager(new LinearLayoutManager(view.getContext()));
                productsRecyclerviewAdapter = new ProductsRecyclerviewAdapter(productList, mainModel);
                productsRecyclerview.setAdapter(productsRecyclerviewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        productList.clear();


    }

}