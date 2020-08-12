package com.genius.connectguard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.constants.constants;
import com.genius.models.SubcategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubCategoryRecyclerViewAdapter extends RecyclerView.Adapter<SubCategoryRecyclerViewAdapter.SubCategoryViewHolder> {

    FragmentActivity fragmentActivity;
    String mainCategoryName;
    List<SubcategoryModel> subcategoryModels;

    public SubCategoryRecyclerViewAdapter(FragmentActivity fragmentActivity, String mainCategoryName,List<SubcategoryModel> subcategoryModels) {
        this.fragmentActivity = fragmentActivity;
        this.mainCategoryName = mainCategoryName;
        this.subcategoryModels = subcategoryModels;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_item, parent, false);
        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, final int position) {

        holder.SubcategoryName.setText(subcategoryModels.get(position).getSubcategoryName());
        Picasso.get().load(subcategoryModels.get(position).getSubcategoryImage()).into(holder.SubcategoryImage);
        holder.SubcategoryContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                constants.getDatabaseReference().child("Categories").child(mainCategoryName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                if (snapshot.child(dataSnapshot.getKey()).child("modelName").getValue().toString().equals(subcategoryModels.get(position).getSubcategoryName())){

                                    ProductsFragment secondFragmentName = new ProductsFragment();
                                    Bundle args = new Bundle();
                                    args.putString("MainSubCategoryName", snapshot.child(dataSnapshot.getKey()).child("modelName").getValue().toString());
                                    args.putString("MainCategoryName2", mainCategoryName);
                                    secondFragmentName.setArguments(args);
                                    fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.register_framelayout, secondFragmentName).commit();

                                }

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return subcategoryModels.size();
    }

    public class SubCategoryViewHolder extends RecyclerView.ViewHolder{

        ImageView SubcategoryImage;
        TextView SubcategoryName;
        LinearLayout SubcategoryContainer;

        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            SubcategoryImage = itemView.findViewById(R.id.subcategory_model_image);
            SubcategoryName = itemView.findViewById(R.id.subcategory_model_name);
            SubcategoryContainer = itemView.findViewById(R.id.subcategory_item_container);

        }
    }

}
