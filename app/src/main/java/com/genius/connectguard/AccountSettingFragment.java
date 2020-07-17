package com.genius.connectguard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.genius.constants.constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.app.Activity.RESULT_OK;

public class AccountSettingFragment extends Fragment {

    private Uri userImage;
    private String spareUserImage;
    private ImageView profile_image_in_account_setting;
    private ImageView edit_profile_image_icon_in_account_setting;
    private EditText name_in_account_setting;
    private EditText password_in_account_setting;
    private EditText old_password_in_account_setting;
    private EditText address_in_account_setting;
    private EditText phone_number_in_account_setting;
    private EditText car_model_in_account_setting;
    private Button done_button_in_account_setting;
    private Button cancel_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_account_setting, container, false);


        edit_profile_image_icon_in_account_setting = view.findViewById(R.id.edit_profile_image_icon_in_account_setting);
        edit_profile_image_icon_in_account_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .start(requireContext(), AccountSettingFragment.this);

            }
        });

        profile_image_in_account_setting = view.findViewById(R.id.profile_image_in_account_setting);
        name_in_account_setting = view.findViewById(R.id.change_user_name);
        address_in_account_setting = view.findViewById(R.id.change_user_address);
        phone_number_in_account_setting = view.findViewById(R.id.change_user_phone_number);
        password_in_account_setting = view.findViewById(R.id.change_user_password_new_password);
        old_password_in_account_setting = view.findViewById(R.id.change_user_password_old_password);
        car_model_in_account_setting = view.findViewById(R.id.change_user_car_model);

        constants.getDatabaseReference().child("Users").child(constants.getUId(getActivity())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Picasso.get().load(snapshot.child("userImage").getValue().toString()).into(profile_image_in_account_setting);
                name_in_account_setting.setText(snapshot.child("name").getValue().toString());
                address_in_account_setting.setText(snapshot.child("adress").getValue().toString());
                phone_number_in_account_setting.setText(snapshot.child("mobile").getValue().toString());
                car_model_in_account_setting.setText(snapshot.child("carModel").getValue().toString());
                spareUserImage = snapshot.child("userImage").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        done_button_in_account_setting = view.findViewById(R.id.done_button_in_account_setting);
        done_button_in_account_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userImage == null){

                    changeData(view,spareUserImage,name_in_account_setting.getText().toString(), address_in_account_setting.getText().toString(), phone_number_in_account_setting.getText().toString(), car_model_in_account_setting.getText().toString(), old_password_in_account_setting.getText().toString(),password_in_account_setting.getText().toString(), spareUserImage);

                }else {

                    changeData(view,userImage.toString(),name_in_account_setting.getText().toString(), address_in_account_setting.getText().toString(), phone_number_in_account_setting.getText().toString(), car_model_in_account_setting.getText().toString(), old_password_in_account_setting.getText().toString(),password_in_account_setting.getText().toString(), spareUserImage);

                }


            }
        });

        cancel_button = view.findViewById(R.id.cancel_button_in_account_setting);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new SettingsFragment());

            }
        });

        return view;
    }


    private void setFragemnt(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
        fragmentTransaction.replace(R.id.account_settings_framelayout, fragment);
        fragmentTransaction.commit();

    }

    private void changeData(final View view, final String image, final String name, final String address, final String mobile, final String car_model, final String old_password, final String new_password, final String spare_user_image){

        final boolean[] isDone = {false};

        constants.getDatabaseReference().child("Users").child(constants.getUId(getActivity())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (old_password.equals(snapshot.child("password").getValue().toString())){

                        if (new_password.equals("")){

                            snapshot.getRef().child("password").setValue(snapshot.child("password").getValue().toString());

                        }else{

                            snapshot.getRef().child("password").setValue(new_password);

                        }
                        snapshot.getRef().child("userImage").setValue(image);
                        snapshot.getRef().child("name").setValue(name);
                        snapshot.getRef().child("adress").setValue(address);
                        snapshot.getRef().child("mobile").setValue(mobile);
                        snapshot.getRef().child("carModel").setValue(car_model);
                        Toast.makeText(view.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                        setFragemnt(new SettingsFragment());
                        isDone[0] = true;
                        return;

                    }else{

                        if (!isDone[0]){

                            Toast.makeText(view.getContext(), "Please Enter Your Old Password Correctly!", Toast.LENGTH_SHORT).show();
                            snapshot.getRef().child("userImage").setValue(image);
                            snapshot.getRef().child("name").setValue(name);
                            snapshot.getRef().child("adress").setValue(address);
                            snapshot.getRef().child("mobile").setValue(mobile);
                            snapshot.getRef().child("carModel").setValue(car_model);
                            return;

                        }

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                userImage = result.getUri();

                Picasso
                        .get()
                        .load(userImage)
                        .into(profile_image_in_account_setting);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                constants.showToast(requireContext(), error.getMessage());
            }
        }
    }

}