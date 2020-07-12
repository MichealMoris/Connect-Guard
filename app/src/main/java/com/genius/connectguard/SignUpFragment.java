package com.genius.connectguard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.genius.constants.constants;
import com.genius.models.userModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.app.Activity.RESULT_OK;

public class SignUpFragment extends Fragment {

    //commit

    TextView alreadyHaveAccount;
    private View view;
    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText mobileField;
    private EditText adressField;
    private EditText carModel;
    private CircleImageView circleImageView;
    private Spinner userRegisterChooseLang;
    private Uri userImage;
    private Button registerBtn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        alreadyHaveAccount = view.findViewById(R.id.signInText);
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        return view;
    }


    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
        fragmentTransaction.replace(R.id.register_framelayout, fragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        constants.initProgress(requireContext(), "please wait ...");
    }

    private void initViews() {
        nameField = view.findViewById(R.id.register_name_field);
        emailField = view.findViewById(R.id.register_email_field);
        passwordField = view.findViewById(R.id.register_password_field);
        mobileField = view.findViewById(R.id.register_mobile_field);
        adressField = view.findViewById(R.id.register_adress_field);
        carModel = view.findViewById(R.id.register_model_field);
        circleImageView = view.findViewById(R.id.pick_user_image);
        userRegisterChooseLang = view.findViewById(R.id.userRegisterChooseLang);
        registerBtn = view.findViewById(R.id.register_register_btn);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .start(requireContext(), SignUpFragment.this);

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String mobile = mobileField.getText().toString();
                String adress = adressField.getText().toString();
                String model = carModel.getText().toString();
                String language = userRegisterChooseLang.getSelectedItem().toString();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || mobile.isEmpty() || adress.isEmpty() || model.isEmpty() || language.isEmpty()) {

                    constants.showToast(requireContext(), "invalid data");

                    return;
                }


                if (userImage == null)
                {
                    constants.showToast(requireContext(),"please select image");
                    return;
                }

                constants.showProgress();

                registerFireBase(name, email, password, mobile, adress , model, language);

             /*   setFragment(new SignInFragment());
                constants.dissmisProgress();*/



            }
        });
    }

    private void registerFireBase(final String name , final String email , final String password, final String mobile, final String adress , final String model, final String language) {
        constants.getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            String uId = task.getResult().getUser().getUid();

                            uploadImage(name,email, password,mobile,adress,model , uId, language);


                        } else
                        {
                            constants.dissmisProgress();
                            constants.showToast(requireContext(), task.getException().getMessage());
                        }

                    }
                });
    }

    private void uploadImage(final String name, final String email,final String password , final String mobile, final String adress,final String model,  final String uId, final String language)
    {
        // set file place into storage and file name
        final StorageReference userImageRef = constants.getStorageReference().child("users_images/"+userImage.getLastPathSegment());

        // put file into upload task
        UploadTask uploadTask = userImageRef.putFile(userImage);

        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                return userImageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if (task.isSuccessful())
                {
                    Uri downloadUri = task.getResult();
                    String imageUrl = downloadUri.toString();

                    saveNewUser(name,email,password,mobile,adress,model,uId,imageUrl, language);
                }
            }
        });
    }

    private void saveNewUser(String name, String email, String password , String mobile, String adress,String model, String uId, String imageUrl, String language)
    {
        userModel userModel = new userModel(name , email , password , mobile , adress , model , imageUrl , uId, language);

        constants.getDatabaseReference().child("Users").child(uId).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                constants.dissmisProgress();
                if (task.isSuccessful())
                {
                   constants.replaceFragment(SignUpFragment.this,new SignInFragment(),false);
                }
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
                        .into(circleImageView);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                constants.showToast(requireContext(), error.getMessage());
            }
        }
    }
}