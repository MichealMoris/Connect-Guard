package com.genius.connectguard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.genius.constants.constants;
import com.genius.models.productModel;
import com.genius.models.userModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdminAddProduct extends Fragment
{
    private View view ;
    private EditText roomName ;
    private EditText productModel ;
    private EditText productDescription ;
    private EditText productPrice ;
    private Button addBtn ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_admin_add_product,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        constants.initProgress(requireContext(), "please wait");
        initViews();

    }




    private void initViews()
    {
        roomName = view.findViewById(R.id.product_name);
        productModel = view.findViewById(R.id.product_model);
        productDescription = view.findViewById(R.id.product_description);
        productPrice = view.findViewById(R.id.product_price);
        addBtn = view.findViewById(R.id.add_btn);

        addBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = roomName.getText().toString();
                String model = productModel.getText().toString();
                String description = productDescription.getText().toString();
                String price = productPrice.getText().toString();

                if (name.isEmpty() || model.isEmpty() || description.isEmpty() || price.isEmpty())
                {
                    constants.showToast(requireContext(),"invalid data");
                    return;
                }
                constants.showProgress();

                saveNewRoom(name , model , description , price);


            }
        });


    }



    private void saveNewRoom(final String name , final String modell , final String description , final String price )
    {
        constants.getDatabaseReference().child("Users").child(constants.getUId(requireActivity())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                final String roomId = constants.getDatabaseReference().child("rooms").push().getKey();

                userModel userModel = dataSnapshot.getValue(userModel.class);
                productModel model = new productModel(name,modell,description , price );

                if (roomId != null)
                {
                    constants.getDatabaseReference().child("rooms").child(roomId).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            constants.dissmisProgress();
                            constants.replaceFragment(AdminAddProduct.this,new MainFragment() , true);


                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
