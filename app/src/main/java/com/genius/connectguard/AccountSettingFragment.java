package com.genius.connectguard;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.genius.constants.constants;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    private EditText name_in_account_setting;
    private TextView reset_password_in_account_setting;
    private EditText address_in_account_setting;
    private EditText phone_number_in_account_setting;
    private Button done_button_in_account_setting;
    private Button cancel_button;
    private boolean wrong;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_account_setting, container, false);


        name_in_account_setting = view.findViewById(R.id.change_user_name);
        address_in_account_setting = view.findViewById(R.id.change_user_address);
        phone_number_in_account_setting = view.findViewById(R.id.change_user_phone_number);

        constants.getDatabaseReference().child("Users").child(constants.getUId(getActivity())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                email = snapshot.child("email").getValue().toString();
                name_in_account_setting.setText(snapshot.child("name").getValue().toString());
                address_in_account_setting.setText(snapshot.child("adress").getValue().toString());
                phone_number_in_account_setting.setText(snapshot.child("mobile").getValue().toString());
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

                    changeData(view,spareUserImage,name_in_account_setting.getText().toString(), address_in_account_setting.getText().toString(), phone_number_in_account_setting.getText().toString(), spareUserImage);

                }else {

                    changeData(view,userImage.toString(),name_in_account_setting.getText().toString(), address_in_account_setting.getText().toString(), phone_number_in_account_setting.getText().toString(),  spareUserImage);

                }


            }
        });

        cancel_button = view.findViewById(R.id.cancel_button_in_account_setting);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("MainActivity", "popping backstack");
                    fm.popBackStack();
                }

            }
        });

        reset_password_in_account_setting = view.findViewById(R.id.reset_password_in_account_settings);
        reset_password_in_account_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    constants.getAuth().signOut();
                                    constants.saveUId(requireActivity(),"empty");
                                    constants.replaceFragment(AccountSettingFragment.this,new EmailSentFragment(),false);

                                }
                            }
                        });
            }
        });

        return view;
    }


    private void changeData(final View view, final String image, final String name, final String address, final String mobile, final String spare_user_image){

        constants.getDatabaseReference().child("Users").child(constants.getUId(getActivity())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                snapshot.getRef().child("userImage").setValue(image);
                snapshot.getRef().child("name").setValue(name);
                snapshot.getRef().child("adress").setValue(address);
                snapshot.getRef().child("mobile").setValue(mobile);
                Toast.makeText(view.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                restartApp(view.getContext());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void restartApp(Context context) {

        Intent intent = new Intent(context, RegisterActivity.class);
        getActivity().overridePendingTransition(R.anim.fade_out_anim, R.anim.fade_in_anim);
        startActivity(intent);
        getActivity().finish();
    }

}