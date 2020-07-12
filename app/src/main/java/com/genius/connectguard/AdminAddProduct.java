package com.genius.connectguard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.genius.constants.constants;
import com.genius.models.productModel;
import com.genius.models.userModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class AdminAddProduct extends Fragment
{
    private View view ;
    private EditText roomName ;
    private EditText productModel ;
    private EditText productDescription ;
    private EditText productPrice ;
    private ImageView postImage ;
    private Uri selectedPostImage;
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
        postImage = view.findViewById(R.id.new_post_image);

        postImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CropImage.activity()
                        .start(requireContext(), AdminAddProduct.this);
            }
        });


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

                if (selectedPostImage == null)
                {
                    constants.showToast(requireContext(),"please select image");
                    constants.dissmisProgress();
                    return;


                }else
                    {

                        uploadImage(name , model , description , price , selectedPostImage  , 1);
                    }


            }
        });


    }

    private void uploadImage(final String name, final String modell, final String description, final String price, final Uri selectedPostImage, final int type)
    {
        // set file place into storage and file name
        final StorageReference userImageRef = constants.getStorageReference().child("posts_images/"+selectedPostImage.getLastPathSegment());

        // put file into upload task
        UploadTask uploadTask = userImageRef.putFile(selectedPostImage);

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

                    String uId = task.getResult().toString();

                    saveNewRoom(name,modell , description , price , imageUrl , uId , type );

                }
            }
        });
    }



    private void saveNewRoom(final String name , final String modell , final String description , final String price , final String imageUri,final String uId , final int type)
    {
        constants.getDatabaseReference().child("Users").child(constants.getUId(requireActivity())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                final String roomId = constants.getDatabaseReference().child("products").push().getKey();

                userModel userModel = dataSnapshot.getValue(userModel.class);
                productModel model = new productModel(name,modell,description , price , imageUri ,userModel.getuId(), type );

                if (roomId != null)
                {

                    constants.getDatabaseReference().child("products").child(roomId).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            constants.dissmisProgress();

                            constants.replaceFragment(AdminAddProduct.this,new MainFragment(),true);


                        }
                    });
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)
            {
                selectedPostImage = result.getUri();

                postImage.setVisibility(View.VISIBLE);

                Picasso
                        .get()
                        .load(selectedPostImage)
                        .into(postImage);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
                constants.showToast(requireContext(), error.getMessage());
            }
        }
    }

}
