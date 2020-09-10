package com.genius.connectguard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.genius.constants.constants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_OK;

public class AddNewCategoryFragment extends Fragment {

    private Toolbar addCategoryToolbar;
    private ImageView pickedCategoryImage;
    private ImageView pickCategoryImage;
    private EditText categoryName;
    private EditText categoryContentDescription;
    private Button addNewCategory;
    private Uri selectedCategoryImage;
    private LinearLayout AddNewCategoryProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_new_category, container, false);


        constants.getDatabaseReference().child("Categories").push();

        pickedCategoryImage = view.findViewById(R.id.new_category_image);
        categoryName = view.findViewById(R.id.category_name);
        categoryContentDescription = view.findViewById(R.id.category_content_discription);

        pickCategoryImage = view.findViewById(R.id.pick_category_image);
        pickCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity().start(requireContext(), AddNewCategoryFragment.this);

            }
        });

        addNewCategory = view.findViewById(R.id.add_new_category);
        addNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (categoryName.getText().toString().isEmpty() && categoryContentDescription.getText().toString().isEmpty()){

                    Toast.makeText(view.getContext(), "Please Enter Category Name & Description", Toast.LENGTH_SHORT).show();

                }else if (!categoryName.getText().toString().isEmpty() && !categoryContentDescription.getText().toString().isEmpty()){

                    AddNewCategoryProgress = view.findViewById(R.id.add_category_progress);
                    AddNewCategoryProgress.setVisibility(View.VISIBLE);
                    if (selectedCategoryImage != null){

                        final StorageReference storageReference = constants.getStorageReference().child("category_images/" + selectedCategoryImage.getLastPathSegment());
                        UploadTask uploadTask = storageReference.putFile(selectedCategoryImage);
                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                        {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                            {
                                return storageReference.getDownloadUrl();
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
                                    addNewCategory(view, categoryName.getText().toString(), imageUrl, categoryContentDescription.getText().toString());

                                }
                            }
                        });

                    }


                }
            }
        });

        addCategoryToolbar = view.findViewById(R.id.add_new_category_toolbar);
        addCategoryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    public void addNewCategory(View view, String categoryName, String categoryImage, String categoryContentDescription){

        constants.getDatabaseReference().child("Categories").child(categoryName).push();
        constants.getDatabaseReference().child("Categories").child(categoryName).child("CategoryName").setValue(categoryName);
        constants.getDatabaseReference().child("Categories").child(categoryName).child("CategoryImage").setValue(categoryImage);
        constants.getDatabaseReference().child("Categories").child(categoryName).child("CategoryContentDescription").setValue(categoryContentDescription);
        restartApp(view.getContext());

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){

                selectedCategoryImage = result.getUri();
                Picasso.get().load(selectedCategoryImage).into(pickedCategoryImage);

            }

        }

    }

    private void restartApp(Context context) {

        Intent intent = new Intent(context, RegisterActivity.class);
        getActivity().overridePendingTransition(R.anim.fade_out_anim, R.anim.fade_in_anim);
        startActivity(intent);
        getActivity().finish();
    }

}