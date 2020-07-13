package com.genius.connectguard;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.genius.constants.constants;
import com.genius.models.CartModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView cart_recyclerView;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    private List<CartModel> cartModelList = new ArrayList<>();
    private String key;
    private TextView decreaseAmount;
    private TextView increaseAmount;
    private TextView amount;
    private List<CartModel> cartList = new ArrayList<>();
    private CartRecyclerViewAdapter adapter;
    private ElegantNumberButton elegantNumberButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_cart, container, false);

        setRecyclerView(view);

        return view;
    }

    public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.CartViewHolder>{

        List<CartModel> cartModelList;

        public CartRecyclerViewAdapter(List<CartModel> cartModelList) {
            this.cartModelList = cartModelList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_cart_item, parent , false);
            return new CartViewHolder(view);


        }

        @Override
        public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {

            Picasso.get().load(cartModelList.get(position).getProduct_image()).into(holder.product_image);
            holder.product_name.setText(cartModelList.get(position).getProduct_name());
            holder.product_model.setText(cartModelList.get(position).getProduct_catgory());
            holder.elegantNumberButton.setNumber(String.valueOf(cartModelList.get(position).getProduct_amount()));
            holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton v, int oldValue, final int newValue) {

                    class UpdateOrderAmount extends AsyncTask<Void, Void, Void>{

                        @Override
                        protected Void doInBackground(Void... voids) {
                            CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().updateOrderAmount(cartModelList.get(position).getProduct_name(), String.valueOf(newValue));
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            getFragmentManager().beginTransaction().detach(CartFragment.this).attach(CartFragment.this).commit();

                        }
                    }

                    UpdateOrderAmount updateOrderAmount = new UpdateOrderAmount();
                    updateOrderAmount.execute();

                }
            });


        }

        @Override
        public int getItemCount() {
            return cartModelList.size();
        }

        public class CartViewHolder extends RecyclerView.ViewHolder{

            ImageView product_image;
            TextView product_name;
            TextView product_model;
            ElegantNumberButton elegantNumberButton;

            public CartViewHolder(@NonNull View itemView) {
                super(itemView);

                product_image = itemView.findViewById(R.id.iv_product);
                product_name = itemView.findViewById(R.id.tv_productName);
                product_model = itemView.findViewById(R.id.tv_category);
                elegantNumberButton = itemView.findViewById(R.id.order_amount);


            }
        }


    }


    public void setRecyclerView(final View view){

        class GetCartOrdersTask extends AsyncTask<Void, Void, List<CartModel>> {

            @Override
            protected List<CartModel> doInBackground(Void... voids) {
                List<CartModel> cartOrdersList = CartDatabaseInstance
                        .getInstance(view.getContext())
                        .getAppDatabase()
                        .cartDao()
                        .getAllCartOrders();
                return cartOrdersList;
            }

            @Override
            protected void onPostExecute(List<CartModel> cartModelList) {
                super.onPostExecute(cartModelList);

                cart_recyclerView = view.findViewById(R.id.cart_items);
                adapter = new CartRecyclerViewAdapter(cartModelList);
                cart_recyclerView.setAdapter(adapter);
                cart_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

            }
        }

        GetCartOrdersTask getCartOrdersTask = new GetCartOrdersTask();
        getCartOrdersTask.execute();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        }
    }



}