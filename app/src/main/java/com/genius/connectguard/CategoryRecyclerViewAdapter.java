package com.genius.connectguard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.constants.constants;
import com.genius.models.CategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder> {

    SubCategoryFragment secondFragmentName;
    List<CategoryModel> categoryModels = new ArrayList<>();
    FragmentActivity fragmentActivity;

    public CategoryRecyclerViewAdapter(FragmentActivity fragmentActivity, List<CategoryModel> categoryModels) {
        this.fragmentActivity = fragmentActivity;
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent,false);
        return new CategoryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {

        holder.CategoryName.setText(categoryModels.get(position).getCategoryName());
        Picasso.get().load(categoryModels.get(position).getCategoryImage()).into(holder.CategoryImage);
        holder.CategoryContentDescription.setText(categoryModels.get(position).getCategoryContentDescription());
        holder.CategoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                constants.getDatabaseReference().child("Categories").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            if (snapshot.child(dataSnapshot.getKey()).child("CategoryName").getValue().toString().equals(categoryModels.get(position).getCategoryName())){

                                secondFragmentName = new SubCategoryFragment();
                                Bundle args = new Bundle();
                                args.putString("MainCategoryName", snapshot.child(dataSnapshot.getKey()).child("CategoryName").getValue().toString());
                                secondFragmentName.setArguments(args);

                            }

                        }

                        try {

                            fragmentActivity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_framelayout, secondFragmentName).commit();

                        }catch (Exception e){


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
        return categoryModels.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{

        ImageView CategoryImage;
        TextView CategoryName;
        TextView CategoryContentDescription;
        LinearLayout CategoryItem;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            CategoryImage = itemView.findViewById(R.id.category_image);
            CategoryName = itemView.findViewById(R.id.category_name);
            CategoryContentDescription = itemView.findViewById(R.id.category_content_description);
            CategoryItem = itemView.findViewById(R.id.category_item);

        }

    }

}
