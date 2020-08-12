package com.genius.connectguard;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.genius.constants.constants;

public class AdminFragment extends Fragment {

    LinearLayout add_new_category_button, orders_buttons, add_new_subcategory_button, add_new_product_button, edit_product_button, delete_product_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin, container, false);

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

        return rootView;


    }


    private void setFragemnt(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
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