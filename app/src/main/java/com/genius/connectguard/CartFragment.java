package com.genius.connectguard;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.genius.constants.constants;
import com.genius.models.CartModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView cart_recyclerView;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    //private List<CartModel> cartModelList = new ArrayList<>();
    private String key;
    private ImageView decreaseAmount;
    private ImageView increaseAmount;
    private TextView amount;
    private CartRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_cart, container, false);

        setRecyclerView(view);

        return view;
    }


    private void getOrders(final View view/*, final int amount*/)
    {
        constants.getDatabaseReference().child("products").child(constants.getProductId(view.getContext())).addValueEventListener(new ValueEventListener()
        {



            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                /*Cart cart = new Cart(dataSnapshot.child("productImage").toString(), dataSnapshot.child("productName").toString(), dataSnapshot.child("productModel").toString(), 0);

                cartList.add(cart);

                cartRecyclerViewAdapter = new CartFragment.CartRecyclerViewAdapter(cartList);
                recyclerView.setAdapter(cartRecyclerViewAdapter);*/

                Toast.makeText(view.getContext(), dataSnapshot.child("productName").toString(), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    /*public int productAmount(final View view){

        decreaseAmount = view.findViewById(R.id.tv_minus);
        increaseAmount = view.findViewById(R.id.tv_plus);
        amount = view.findViewById(R.id.tv_amount);

        constants.getDatabaseReference().child("products").child(constants.getProductId(view.getContext())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                constants.saveProductName(view.getContext(), snapshot.child("productName").getValue().toString());
                constants.saveProductAmount(view.getContext(), snapshot.child("productName").getValue().toString(), Integer.parseInt(amount.getText().toString()));
                decreaseAmount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(Integer.parseInt(amount.getText().toString()) > 0){

                            if (constants.getProductAmount(view.getContext(), snapshot.child("productName").getValue().toString()) != 0){

                                amount.setText(constants.getProductAmount(view.getContext(), snapshot.child("productName").getValue().toString()) - 1);

                            }

                        }

                    }
                });

                increaseAmount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        amount.setText(constants.getProductAmount(view.getContext(), snapshot.child("productName").getValue().toString() + 1));

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return constants.getProductAmount(view.getContext(), constants.getProductName(view.getContext()));

    }
*/
    public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.CartViewHolder>{

        List<CartModel> cartModelList;

        public CartRecyclerViewAdapter(List<CartModel> cartModelList) {
            this.cartModelList = cartModelList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_cart, parent , false);
            return new CartViewHolder(view);


        }

        @Override
        public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

            Picasso.get().load(cartModelList.get(position).getProduct_image()).into(holder.product_image);
            holder.product_name.setText(cartModelList.get(position).getProduct_name());
            holder.product_model.setText(cartModelList.get(position).getProduct_catgory());

        }

        @Override
        public int getItemCount() {
            return cartModelList.size();
        }

        public class CartViewHolder extends RecyclerView.ViewHolder{

            ImageView product_image;
            TextView product_name;
            TextView product_model;

            public CartViewHolder(@NonNull View itemView) {
                super(itemView);

                product_image = itemView.findViewById(R.id.iv_product);
                product_name = itemView.findViewById(R.id.tv_productName);
                product_model = itemView.findViewById(R.id.tv_category);

            }
        }


    }

    public void setRecyclerView(final View view){

        class GetTasks extends AsyncTask<Void, Void, List<CartModel>> {

            @Override
            protected List<CartModel> doInBackground(Void... voids) {
                List<CartModel> cartOrders = CartDatabaseInstance
                        .getInstance(view.getContext())
                        .getAppDatabase()
                        .cartDao()
                        .getAllCartOrders();
                return cartOrders;
            }

            @Override
            protected void onPostExecute(List<CartModel> cartModels) {
                super.onPostExecute(cartModels);

                cart_recyclerView = view.findViewById(R.id.cart_items);
                CartRecyclerViewAdapter adapter = new CartRecyclerViewAdapter(cartModels);
                cart_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
                cart_recyclerView.setAdapter(adapter);

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

}
