package com.genius.connectguard;

import android.hardware.camera2.params.RggbChannelVector;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.genius.constants.constants;
import com.genius.models.CartModel;
import com.genius.models.productModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment
{

    private View view ;

    private RecyclerView recyclerView ;
    private SearchView searchView ;
    private List<productModel> postModels;
    String key;
    postsAdbtar adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_home,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initViews();
        getPosts();

    }

    private void getPosts()
    {
        constants.getDatabaseReference().child("products").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                postModels.clear();

                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    productModel model = d.getValue(productModel.class);

                    postModels.add(model);

                    key = d.getKey();


                }

                adapter = new postsAdbtar(postModels);
                recyclerView.setAdapter(adapter);

               // recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void initViews()
    {
        recyclerView = view.findViewById(R.id.posts_recycler);
        postModels = new ArrayList<>();

        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


    }

    //commit
   public class postsAdbtar extends RecyclerView.Adapter<postsAdbtar.vh>
    {
        List<productModel> postModelList;
        List<productModel> filteredplannerModels;


        public postsAdbtar(List<productModel> postModelList)
        {
            this.postModelList = postModelList;
            this.filteredplannerModels = new ArrayList<>(postModelList);
        }

        @NonNull
        @Override
        public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_post, parent , false);
            return new vh(view);
        }

        //commit
        @Override
        public void onBindViewHolder(@NonNull final vh holder, final int position)
        {
            productModel model = postModelList.get(position);



                String text = model.getProductName();
                String description = model.getProductDiscreption();
                String image = model.getProductImage();
                String modell = model.getProductModel();
                String price = model.getProductPrice();

                //commit


                holder.postText.setText(text);
                holder.postModell.setText(modell);
                holder.postPrise.setText(price);
                holder.postDescriptiom.setText(description);


                Picasso
                        .get()
                        .load(image)
                        .into(holder.postImage);

                holder.add_to_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        constants.getDatabaseReference().child("products").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                                for (final DataSnapshot child : snapshot.getChildren()){

                                    if (snapshot.child(child.getKey()).child("productName").getValue().toString().equals(postModelList.get(position).getProductName())){

                                        class AddCartOrdersTask extends AsyncTask<Void, Void, Void> {

                                            @Override
                                            protected Void doInBackground(Void... voids) {

                                                CartDatabaseInstance.getInstance(view.getContext()).getAppDatabase()
                                                        .cartDao()
                                                        .addToCart(new CartModel(snapshot.child(child.getKey()).child("productImage").getValue().toString(), snapshot.child(child.getKey()).child("productName").getValue().toString(), snapshot.child(child.getKey()).child("productModel").getValue().toString(), "1", 1));
                                                return null;
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                super.onPostExecute(aVoid);

                                                Toast.makeText(view.getContext(), snapshot.child(child.getKey()).child("productName").getValue().toString() + " " + view.getResources().getString(R.string.item_added_to_cart), Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                        AddCartOrdersTask addCartOrdersTask = new AddCartOrdersTask();
                                        addCartOrdersTask.execute();

                                    }

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        /*constants.getDatabaseReference().child("products").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {



                                *//*for (DataSnapshot child : snapshot.getChildren()){

                                    constants.saveProductId(holder.itemView.getContext(), child.getKey());
                                    Toast.makeText(holder.itemView.getContext(), constants.getProductId(holder.itemView.getContext()), Toast.LENGTH_SHORT).show();
                                    *//**//*setFragemnt(new CartFragment());*//**//*


                                }*//*

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
*/
                    }
                });



        }

        private void setFragemnt(Fragment fragment) {

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
            fragmentTransaction.replace(R.id.home_framlayout, fragment);
            fragmentTransaction.commit();

        }

        @Override
        public int getItemCount() {
            return postModelList.size();
        }
        //commit

        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<productModel> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(filteredplannerModels);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (productModel item : filteredplannerModels) {
                        if (item.getProductName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                postModelList.clear();
                postModelList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };

        public Filter getFilter() {
            return exampleFilter;
        }

//commit
        public class vh extends RecyclerView.ViewHolder
        {

            ImageView postImage ;
            TextView postText ;
            TextView postModell ;
            TextView postPrise ;
            TextView postDescriptiom ;
            CardView add_to_cart;


            public vh(@NonNull View itemView)
            {
                super(itemView);

                postImage = itemView.findViewById(R.id.post_image);
                postText = itemView.findViewById(R.id.post_text);
                postModell = itemView.findViewById(R.id.post_modell);
                postPrise = itemView.findViewById(R.id.post_price);
                postDescriptiom = itemView.findViewById(R.id.post_description);

                add_to_cart = itemView.findViewById(R.id.add_to_cart);


            }
        }
    }

}