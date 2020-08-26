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

import com.genius.constants.constants;
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

        productsRecyclerview = view.findViewById(R.id.product_recyclerview);
        productsRecyclerview.setHasFixedSize(true);
        productsRecyclerview.setLayoutManager(new LinearLayoutManager(view.getContext()));

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


    public void addProductsToRecyclerview(final View view, final String mainCategory, final String mainModel){

        Query query = constants.getDatabaseReference().child("Categories").child(mainCategory);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                productList.clear();

                for (DataSnapshot dataSnapshot : snapshot.child(mainModel).getChildren()){

                    productModel productModel = new productModel();
                    productModel.setStandardProductName(dataSnapshot.child("productStandardName").getValue(String.class));
                    productModel.setProductName(dataSnapshot.child("productName").getValue(String.class));
                    productModel.setProductImage(dataSnapshot.child("productImage").getValue(String.class));
                    productModel.setProductDescription(dataSnapshot.child("productDescription").getValue(String.class));
                    productModel.setProductPrice(dataSnapshot.child("productPrice").getValue(String.class));
                    productModel.setProductStock(dataSnapshot.child("productStock").getValue(String.class));
                    productList.add(productModel);

                    for (int i = 0; i < productList.size(); i++){

                        if (productList.get(i).getProductName() == null){

                            productList.remove(i);

                        }

                    }

                    /*for (int i = 0; i < productList.size(); i++){

                        if (Integer.parseInt(productList.get(i).getProductStock()) == 0){

                            productModel.setSoldOut(true);

                        }

                    }*/

                }

                productsRecyclerviewAdapter = new ProductsRecyclerviewAdapter(productList, mainCategory, mainModel);
                productsRecyclerview.setAdapter(productsRecyclerviewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}