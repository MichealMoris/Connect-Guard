package com.genius.connectguard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class AdminFragment extends Fragment {

    RelativeLayout addProduct , request;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin, container, false);

        request = (RelativeLayout)rootView.findViewById(R.id.bt_request);
        addProduct = (RelativeLayout)rootView.findViewById(R.id.bt_add);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();

                switch (id){
                    case R.id.bt_add:

                        setFragemnt(new AdminAddProduct());
                        break;

                    case R.id.bt_request:

//                        setFragemnt(new AdminAddProduct());
                        break;
                }
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

}