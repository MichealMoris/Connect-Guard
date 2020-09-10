package com.genius.connectguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AdminAddProduct extends Fragment
{
    private View view ;
    private ImageView select_new_product_image;
    private ImageView selected_product_image;
    private EditText product_name;
    private EditText product_description;
    private EditText product_price;
    private EditText product_stock;
    private Toolbar addNewProductToolbar;
    private Uri selectedProductImage;
    private Button addBtn ;
    private Spinner selectCategorySpinner;
    private Spinner selectSubcategorySpinner;
    private LinearLayout AddProductProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_admin_add_product,null);

        selected_product_image = view.findViewById(R.id.new_product_image);
        product_name = view.findViewById(R.id.enter_product_name);
        product_description = view.findViewById(R.id.enter_product_description);
        product_price = view.findViewById(R.id.enter_product_price);
        product_stock = view.findViewById(R.id.enter_product_stock);

        addCategoriesToCategoriesSpinner(view);
        selectCategorySpinner = (Spinner) view.findViewById(R.id.select_category_spinner);
        selectCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

                addSubcategoriesToSubcategoriesSpinner(view, selectCategorySpinner.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        select_new_product_image = view.findViewById(R.id.pick_product_image);
        select_new_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity().start(requireContext(), AdminAddProduct.this);

            }
        });


        addNewProductToolbar = view.findViewById(R.id.add_new_product_toolbar);
        addNewProductToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("MainActivity", "popping backstack");
                    fm.popBackStack();
                }

            }
        });


        addBtn = view.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (product_name.getText().toString().isEmpty() || product_description.getText().toString().isEmpty() || product_price.getText().toString().isEmpty() || product_stock.getText().toString().isEmpty()){

                    Toast.makeText(view.getContext(), "Please Enter All The Data", Toast.LENGTH_SHORT).show();

                }else if (!product_name.getText().toString().isEmpty() || !product_description.getText().toString().isEmpty() || !product_price.getText().toString().isEmpty() || !product_stock.getText().toString().isEmpty()){

                    AddProductProgress = view.findViewById(R.id.add_product_progress);
                    AddProductProgress.setVisibility(View.VISIBLE);
                    if (selectedProductImage != null){

                        final StorageReference storageReference = constants.getStorageReference().child("product_images/" + selectedProductImage.getLastPathSegment());
                        UploadTask uploadTask = storageReference.putFile(selectedProductImage);
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
                                    addNewProduct(view, selectCategorySpinner.getSelectedItem().toString(), selectSubcategorySpinner.getSelectedItem().toString(), imageUrl, product_name.getText().toString(), product_name.getText().toString(), product_description.getText().toString(), product_price.getText().toString(), product_stock.getText().toString());

                                }
                            }
                        });

                    }


                }

            }
        });

        return view;
    }


    public void addCategoriesToCategoriesSpinner(final View view){

        final List<String> categoriesSpinnerArray = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categoriesSpinnerArray);

        constants.getDatabaseReference().child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    categoriesSpinnerArray.add(dataSnapshot.getKey());

                }

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectCategorySpinner = (Spinner) view.findViewById(R.id.select_category_spinner);
                if (categoriesSpinnerArray.size() == 0){

                    categoriesSpinnerArray.add(0, getResources().getString(R.string.please_add_category));

                }else if (categoriesSpinnerArray.size() > 0){

                    if (isAdded() || isVisible()){

                        categoriesSpinnerArray.remove(getResources().getString(R.string.please_add_category));

                    }

                }
                selectCategorySpinner.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void addSubcategoriesToSubcategoriesSpinner(final View view, final String mainCategory){

        if (!mainCategory.equals(getResources().getString(R.string.please_add_category))){

            final List<String> subcategoriesSpinnerArray = new ArrayList<String>();

            constants.getDatabaseReference().child("Categories").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    subcategoriesSpinnerArray.clear();

                    for (DataSnapshot dataSnapshot : snapshot.child(mainCategory).getChildren()){

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                            subcategoriesSpinnerArray.add(dataSnapshot.child("modelName").getValue(String.class));

                        }

                    }

                    LinkedHashSet<String> hashSet = new LinkedHashSet<String>(subcategoriesSpinnerArray);

                    ArrayList<String> spinnerWithoutDuplicates = new ArrayList<>(hashSet);

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, spinnerWithoutDuplicates);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selectSubcategorySpinner = (Spinner) view.findViewById(R.id.select_model_spinner);
                    if (spinnerWithoutDuplicates.size() == 0){

                        spinnerWithoutDuplicates.add(0, getResources().getString(R.string.please_add_subcategory));

                    }else if (spinnerWithoutDuplicates.size() > 0){

                        if (isAdded() || isVisible()){

                            spinnerWithoutDuplicates.remove(getResources().getString(R.string.please_add_subcategory));

                        }

                    }
                    selectSubcategorySpinner.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    public void addNewProduct(View view, String mainCategoryName, String mainSubcategoryName, String productImage, String standardProductName,String productName, String productDescription, String productPrice, String productStock){

        constants.getDatabaseReference().child("Categories").child(mainCategoryName).child(mainSubcategoryName).child(standardProductName).push();
        constants.getDatabaseReference().child("Categories").child(mainCategoryName).child(mainSubcategoryName).child(standardProductName).child("productImage").setValue(productImage);
        constants.getDatabaseReference().child("Categories").child(mainCategoryName).child(mainSubcategoryName).child(standardProductName).child("productName").setValue(productName);
        constants.getDatabaseReference().child("Categories").child(mainCategoryName).child(mainSubcategoryName).child(standardProductName).child("productStandardName").setValue(standardProductName);
        constants.getDatabaseReference().child("Categories").child(mainCategoryName).child(mainSubcategoryName).child(standardProductName).child("productDescription").setValue(productDescription);
        constants.getDatabaseReference().child("Categories").child(mainCategoryName).child(mainSubcategoryName).child(standardProductName).child("productPrice").setValue(productPrice);
        constants.getDatabaseReference().child("Categories").child(mainCategoryName).child(mainSubcategoryName).child(standardProductName).child("productStock").setValue(productStock);
        restartApp(view.getContext());

    }

    /*private void saveNewRoom(final String name , final String modell , final String description , final String price , final String imageUri,final String uId , final int type)
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


    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){

                selectedProductImage = result.getUri();
                Picasso.get().load(selectedProductImage).into(selected_product_image);

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
