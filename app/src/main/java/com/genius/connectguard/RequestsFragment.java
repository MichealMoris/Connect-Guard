package com.genius.connectguard;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
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
    private LinearLayout newRecordedOrdersButton, inProgressOrdersButton, shippedOrdersButton, deliveredOrdersButtons;
    private FrameLayout newOrdersCounterContainer, inProgressOrdersCounterContainer, shippedOrdersCounterContainer, deliveredOrdersCounterContainer;
    private TextView newOrdersCounter, inProgressOrdersCounter, shippedOrdersCounter, deliveredOrdersCounter;
    int new_orders_number, in_progress_orders_number, shipped_orders_number, delivered_orders_number;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        newOrdersCounterContainer = view.findViewById(R.id.new_orders_counter_container);
        inProgressOrdersCounterContainer = view.findViewById(R.id.in_progress_orders_counter_container);
        shippedOrdersCounterContainer = view.findViewById(R.id.shipped_orders_counter_container);
        deliveredOrdersCounterContainer = view.findViewById(R.id.deliverd_orders_counter_container);

        newOrdersCounter = view.findViewById(R.id.new_orders_counter);
        inProgressOrdersCounter = view.findViewById(R.id.in_progress_orders_counter);
        shippedOrdersCounter = view.findViewById(R.id.shipped_orders_counter);
        deliveredOrdersCounter = view.findViewById(R.id.delivered_orders_counter);

        Query query = constants.getDatabaseReference().child("Orders");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                new_orders_number = 0;
                in_progress_orders_number = 0;
                shipped_orders_number = 0;
                delivered_orders_number = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    switch (dataSnapshot.child("order_step").getValue().toString()){

                        case "new order":
                            new_orders_number = new_orders_number + 1;
                            break;
                        case "in progress order":
                            in_progress_orders_number = in_progress_orders_number + 1;
                            break;
                        case "shipped order":
                            shipped_orders_number = shipped_orders_number + 1;
                            break;
                        case "delivered order":
                            delivered_orders_number = delivered_orders_number + 1;
                            break;

                    }

                    if (new_orders_number != 0){

                        newOrdersCounter.setText(String.valueOf(new_orders_number));
                        newOrdersCounterContainer.setVisibility(View.VISIBLE);

                    }else {

                        newOrdersCounterContainer.setVisibility(View.GONE);

                    }
                    if (in_progress_orders_number != 0){

                        inProgressOrdersCounter.setText(String.valueOf(in_progress_orders_number));
                        inProgressOrdersCounterContainer.setVisibility(View.VISIBLE);

                    }else {

                        inProgressOrdersCounterContainer.setVisibility(View.GONE);

                    }
                    if (shipped_orders_number != 0){

                        shippedOrdersCounter.setText(String.valueOf(shipped_orders_number));
                        shippedOrdersCounterContainer.setVisibility(View.VISIBLE);

                    }else {

                        shippedOrdersCounterContainer.setVisibility(View.GONE);

                    }
                    if (delivered_orders_number != 0){

                        deliveredOrdersCounter.setText(String.valueOf(delivered_orders_number));
                        deliveredOrdersCounterContainer.setVisibility(View.VISIBLE);

                    }else {

                        deliveredOrdersCounterContainer.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        newRecordedOrdersButton = view.findViewById(R.id.new_recorded_orders_button);
        newRecordedOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new NewOrdersFragment());

            }
        });

        inProgressOrdersButton = view.findViewById(R.id.in_progress_orders_button);
        inProgressOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new InProgressOrderFragment());

            }
        });

        shippedOrdersButton = view.findViewById(R.id.shipped_orders_button);
        shippedOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new ShippedFragment());

            }
        });

        deliveredOrdersButtons = view.findViewById(R.id.delivered_orders_buttons);
        deliveredOrdersButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new DeliveredOrderFragment());

            }
        });

        orderToolbar = view.findViewById(R.id.orders_toolbar);
        orderToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("MainActivity", "popping backstack");
                    fm.popBackStack();
                }

            }
        });

        return view;
    }

    private void setFragemnt(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
        fragmentTransaction.replace(R.id.register_framelayout, fragment);
        fragmentTransaction.commit();

    }

}