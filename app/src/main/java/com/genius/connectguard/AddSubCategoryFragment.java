package com.genius.connectguard;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.genius.constants.constants;
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

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddSubCategoryFragment extends Fragment {

    Toolbar addSubcategoryToolbar;
    ImageView pickNewModelImage;
    ImageView pickedModelImage;
    Spinner sItems;
    EditText modelName;
    Button addSubCategoryButton;
    Uri selectedModelImage;
    LinearLayout AddSubCategoryProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_sub_category, container, false);

        pickedModelImage = view.findViewById(R.id.new_sub_category_image);
        addCategoriesToSpinner(view);

        addSubcategoryToolbar = view.findViewById(R.id.add_new_subcategory_toolbar);
        addSubcategoryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("MainActivity", "popping backstack");
                    fm.popBackStack();
                }
            }
        });

        pickNewModelImage = view.findViewById(R.id.pick_sub_category_image);
        pickNewModelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity().start(requireContext(), AddSubCategoryFragment.this);

            }
        });

        modelName = view.findViewById(R.id.sub_category_name);
        addSubCategoryButton = view.findViewById(R.id.add_new_sub_category);
        addSubCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (modelName.getText().toString().isEmpty()){

                    Toast.makeText(view.getContext(), "Please Enter Subcategory Name Or Subcategory Image", Toast.LENGTH_SHORT).show();

                }else if (selectedModelImage == null){

                    Toast.makeText(view.getContext(), "Please Enter Subcategory Image", Toast.LENGTH_SHORT).show();

                }else if (!modelName.getText().toString().isEmpty()){

                    AddSubCategoryProgress = view.findViewById(R.id.add_subcategory_progress);
                    AddSubCategoryProgress.setVisibility(View.VISIBLE);
                    if (selectedModelImage != null){

                            final StorageReference storageReference = constants.getStorageReference().child("subcategory_images/" + selectedModelImage.getLastPathSegment());
                            UploadTask uploadTask = storageReference.putFile(selectedModelImage);
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
                                        addModel(view, modelName.getText().toString(), sItems.getSelectedItem().toString(), imageUrl);

                                    }
                                }
                            });

                        }

                    }

                }
        });


        return view;
    }


    public void addCategoriesToSpinner(final View view){

        final List<String> spinnerArray = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, spinnerArray);

        constants.getDatabaseReference().child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    spinnerArray.add(dataSnapshot.getKey());

                }

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sItems = (Spinner) view.findViewById(R.id.choose_category_spinner);
                if (spinnerArray.size() == 0){

                    spinnerArray.add(0, getResources().getString(R.string.please_add_category));

                }else if (spinnerArray.size() > 0){

                    if (isAdded() || isVisible()){

                        spinnerArray.remove(getResources().getString(R.string.please_add_category));

                    }

                }
                sItems.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void addModel(View view, String modelName, String categoryName, String modelImage){

        constants.getDatabaseReference().child("Categories").child(categoryName).child(modelName).push();
        constants.getDatabaseReference().child("Categories").child(categoryName).child(modelName).child("modelName").setValue(modelName);
        constants.getDatabaseReference().child("Categories").child(categoryName).child(modelName).child("modelImage").setValue(modelImage);
        constants.getDatabaseReference().child("Categories").child(categoryName).child(modelName).child("modelCategory").setValue(categoryName);
        restartApp(view.getContext());

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){

                selectedModelImage = result.getUri();
                Picasso.get().load(selectedModelImage).into(pickedModelImage);

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