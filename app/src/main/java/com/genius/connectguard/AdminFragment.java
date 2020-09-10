package com.genius.connectguard;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.genius.constants.constants;
import com.genius.models.OrderModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import me.leolin.shortcutbadger.ShortcutBadger;

public class AdminFragment extends Fragment {

    LinearLayout add_new_category_button, orders_buttons, add_new_subcategory_button, add_new_product_button, edit_product_button, delete_product_button, delete_category_button, delete_subcategory_button;
    FrameLayout ordersCounterContainer;
    TextView  ordersCounter;
    long orders_number = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin, container, false);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
                    fragmentTransaction.replace(R.id.register_framelayout, new MainFragment());
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        } );

        setOrdersCounter(rootView);

        SharedPreferences preferences = getActivity().getSharedPreferences("appLanguage", Context.MODE_PRIVATE);

        if (preferences.getString("langCode", "en").equals("ar")){

            rootView.setRotationY(180f);

        }

        add_new_category_button = rootView.findViewById(R.id.add_new_category_button);
        add_new_category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new AddNewCategoryFragment());

            }
        });
        orders_buttons = rootView.findViewById(R.id.orders_buttons);
        orders_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new RequestsFragment());

            }
        });
        add_new_subcategory_button = rootView.findViewById(R.id.add_new_subcategory_button);
        add_new_subcategory_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new AddSubCategoryFragment());

            }
        });
        add_new_product_button = rootView.findViewById(R.id.add_new_product_button);
        add_new_product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new AdminAddProduct());

            }
        });
        edit_product_button = rootView.findViewById(R.id.edit_product_button);
        edit_product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new EditProductFragment());

            }
        });
        delete_product_button = rootView.findViewById(R.id.delete_product_button);
        delete_product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new DeleteFragment());

            }
        });
        delete_category_button = rootView.findViewById(R.id.delete_category_button);
        delete_category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new DeleteCategoryFragment());

            }
        });
        delete_subcategory_button = rootView.findViewById(R.id.delete_subcategory_button);
        delete_subcategory_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new DeleteSubcategoryFragment());

            }
        });

        return rootView;


    }

    public void setOrdersCounter(View view){

        ordersCounterContainer = view.findViewById(R.id.orders_counter_container);
        ordersCounter = view.findViewById(R.id.orders_counter);

        Query query = constants.getDatabaseReference().child("Orders");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                orders_number = snapshot.getChildrenCount();


                if (orders_number != 0){

                    ordersCounter.setText(String.valueOf(orders_number));
                    ordersCounterContainer.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setFragemnt(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
        fragmentTransaction.replace(R.id.register_framelayout, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        }
    }


}