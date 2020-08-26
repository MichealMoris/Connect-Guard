package com.genius.connectguard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.constants.constants;
import com.genius.models.CategoryModel;
import com.genius.models.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {

    private Toolbar orderToolbar;
    private RecyclerView recyclerView;
    private OrdersRecyclerViewAdapter ordersRecyclerViewAdapter;
    private List<OrderModel> ordersList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        recyclerView = view.findViewById(R.id.orders_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        addOrdersToRecyclerView();

        orderToolbar = view.findViewById(R.id.orders_toolbar);
        orderToolbar.setOnClickListener(new View.OnClickListener() {
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

    public void addOrdersToRecyclerView(){

        Query query = constants.getDatabaseReference().child("Orders");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ordersList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    OrderModel orderModel = new OrderModel();

                    orderModel.setOrderUserImage(dataSnapshot.child("order_user_image").getValue().toString());
                    orderModel.setOrderUserName(dataSnapshot.child("order_username").getValue().toString());
                    orderModel.setOrderUserEmail(dataSnapshot.child("order_user_email").getValue().toString());
                    orderModel.setOrderUserPhoneNumber(dataSnapshot.child("order_user_phone_number").getValue().toString());
                    orderModel.setOrderUserAddress(dataSnapshot.child("order_user_address").getValue().toString());
                    orderModel.setOrderProductNumber(dataSnapshot.child("order_number").getValue().toString());
                    orderModel.setOrderProductName(dataSnapshot.child("order_product_name").getValue().toString());
                    orderModel.setOrderProductModel(dataSnapshot.child("order_product_model").getValue().toString());
                    orderModel.setOrderProductAmount(dataSnapshot.child("order_product_amount").getValue().toString());
                    orderModel.setOrderProductPrice(dataSnapshot.child("order_price").getValue().toString());
                    orderModel.setOrderProductTotalPrice(dataSnapshot.child("order_total_price").getValue().toString());

                    ordersList.add(orderModel);

                }

                ordersRecyclerViewAdapter = new OrdersRecyclerViewAdapter(ordersList);
                recyclerView.setAdapter(ordersRecyclerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.OrdersRecyclerViewViewHolder>{

        List<OrderModel> ordersList = new ArrayList<>();
        FragmentActivity fragmentActivity;

        public OrdersRecyclerViewAdapter(List<OrderModel> ordersList) {
            this.ordersList = ordersList;
        }

        @NonNull
        @Override
        public OrdersRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_requests, parent, false);
            return new OrdersRecyclerViewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final OrdersRecyclerViewViewHolder holder, final int position) {

            Picasso.get().load(ordersList.get(position).getOrderUserImage()).into(holder.orderUserImage);
            holder.orderUserName.setText(ordersList.get(position).getOrderUserName());
            holder.orderUserEmail.setText(ordersList.get(position).getOrderUserEmail());
            holder.orderUserPhoneNumber.setText(ordersList.get(position).getOrderUserPhoneNumber());
            holder.orderUserAddress.setText(ordersList.get(position).getOrderUserAddress());
            holder.orderNumber.setText(ordersList.get(position).getOrderProductNumber());
            holder.orderName.setText(ordersList.get(position).getOrderProductName());
            holder.orderModel.setText(ordersList.get(position).getOrderProductModel());
            holder.orderAmount.setText(ordersList.get(position).getOrderProductAmount());
            holder.orderPrice.setText(ordersList.get(position).getOrderProductPrice());
            holder.orderTotalPrice.setText(ordersList.get(position).getOrderProductTotalPrice());
            holder.done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    constants.getDatabaseReference().child("Orders").child(ordersList.get(position).getOrderProductNumber()).removeValue();
                    getFragmentManager().beginTransaction().detach(RequestsFragment.this).attach(RequestsFragment.this).commit();

                }
            });

        }

        @Override
        public int getItemCount() {
            return ordersList.size();
        }

        public class OrdersRecyclerViewViewHolder extends RecyclerView.ViewHolder{

            ImageView orderUserImage;
            TextView orderUserName;
            TextView orderUserEmail;
            TextView orderUserPhoneNumber;
            TextView orderUserAddress;
            TextView orderNumber;
            TextView orderName;
            TextView orderModel;
            TextView orderAmount;
            TextView orderPrice;
            TextView orderTotalPrice;
            Button done;

            public OrdersRecyclerViewViewHolder(@NonNull View itemView) {
                super(itemView);

                orderUserImage = itemView.findViewById(R.id.order_user_image);
                orderUserName = itemView.findViewById(R.id.order_user_name);
                orderUserEmail = itemView.findViewById(R.id.order_user_email);
                orderUserPhoneNumber = itemView.findViewById(R.id.order_user_phone_number);
                orderUserAddress = itemView.findViewById(R.id.order_user_address);
                orderNumber = itemView.findViewById(R.id.order_number);
                orderName = itemView.findViewById(R.id.order_name);
                orderModel = itemView.findViewById(R.id.order_model);
                orderAmount = itemView.findViewById(R.id.order_amount);
                orderPrice = itemView.findViewById(R.id.order_price);
                orderTotalPrice = itemView.findViewById(R.id.order_total_price);
                done = itemView.findViewById(R.id.order_done_button);

            }

        }

    }

}