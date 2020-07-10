package com.genius.connectguard;

import android.content.Intent;
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
    ImageView profile_image_in_account_setting;
    ImageView edit_profile_image_icon_in_account_setting;
    EditText name_in_account_setting;
    EditText email_in_account_setting;
    EditText password_in_account_setting;
    EditText address_in_account_setting;
    EditText phone_number_in_account_setting;
    EditText car_model_in_account_setting;
    Button done_button_in_account_setting;

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
        email_in_account_setting = view.findViewById(R.id.change_user_email);
        address_in_account_setting = view.findViewById(R.id.change_user_address);
        phone_number_in_account_setting = view.findViewById(R.id.change_user_phone_number);

        constants.getDatabaseReference().child("Users").child(constants.getUId(getActivity())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Picasso.get().load(snapshot.child("userImage").getValue().toString()).into(profile_image_in_account_setting);
                name_in_account_setting.setText(snapshot.child("name").getValue().toString());
                email_in_account_setting.setText(snapshot.child("email").getValue().toString());
                address_in_account_setting.setText(snapshot.child("adress").getValue().toString());
                phone_number_in_account_setting.setText(snapshot.child("mobile").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        done_button_in_account_setting = view.findViewById(R.id.done_button_in_account_setting);
        done_button_in_account_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeData(userImage.toString(),name_in_account_setting.getText().toString(), email_in_account_setting.getText().toString(), address_in_account_setting.getText().toString(), phone_number_in_account_setting.getText().toString());

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

    private void changeData(final String image, final String name, final String email, final String address, final String mobile){

        constants.getDatabaseReference().child("Users").child(constants.getUId(getActivity())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                snapshot.getRef().child("userImage").setValue(image);
                snapshot.getRef().child("name").setValue(name);
                snapshot.getRef().child("email").setValue(email);
                snapshot.getRef().child("adress").setValue(address);
                snapshot.getRef().child("mobile").setValue(mobile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setFragemnt(new SettingsFragment());

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