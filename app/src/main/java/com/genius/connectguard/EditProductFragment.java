package com.genius.connectguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ScrollingView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.genius.constants.constants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class EditProductFragment extends Fragment {

    Toolbar editProductToolbar;
    Spinner selectCategorySpinner;
    Spinner selectSubcategorySpinner;
    Spinner selectProductSpinner;
    String mainCategory;
    Uri selectedEditedProductImage;
    ImageView pickEditedProductImage;
    ImageView pickedEditedProductImage;
    EditText editedProductName;
    EditText editedProductDescription;
    EditText editedProductPrice;
    EditText editedProductStock;
    Button editButton;
    Button searchToEditButton;
    ScrollView editDetailsScrollView;
    LinearLayout searchToEditLinearLayout;
    LinearLayout editProductProgressBar;
    String spareProductImage;
    String editedProductNameValue;
    String editedProductDescriptionValue;
    String editedProductPriceValue;
    String editedProductStockValue;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_edit_product, container, false);

        final SharedPreferences data = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = data.edit();

        pickedEditedProductImage = view.findViewById(R.id.edit_product_image);

        addCategoriesToCategoriesSpinner(view);
        selectCategorySpinner = view.findViewById(R.id.select_category_spinner_in_edit);
        selectCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

                mainCategory = selectCategorySpinner.getSelectedItem().toString();
                addSubcategoriesToSubcategoriesSpinner(view, selectCategorySpinner.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectSubcategorySpinner = view.findViewById(R.id.select_model_spinner_in_edit);
        selectSubcategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

                addProductsToProductsSpinner(view, mainCategory, selectSubcategorySpinner.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editedProductName = view.findViewById(R.id.edit_product_name);
        editedProductDescription = view.findViewById(R.id.edit_product_description);
        editedProductPrice = view.findViewById(R.id.edit_product_price);
        editedProductStock = view.findViewById(R.id.edit_product_stock);
        selectProductSpinner = view.findViewById(R.id.select_product_spinner_in_edit);

        searchToEditButton = view.findViewById(R.id.search_product_to_edit_button);
        searchToEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchToEditLinearLayout = view.findViewById(R.id.search_to_edit_linear_layout);
                searchToEditLinearLayout.setVisibility(View.GONE);

                editDetailsScrollView = view.findViewById(R.id.edit_details_scrollview);
                editDetailsScrollView.setVisibility(View.VISIBLE);

                constants.getDatabaseReference().child("Categories").child(selectCategorySpinner.getSelectedItem().toString()).child(selectSubcategorySpinner.getSelectedItem().toString()).child(selectProductSpinner.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Picasso.get().load(Uri.parse(snapshot.child("productImage").getValue().toString())).into(pickedEditedProductImage);
                        spareProductImage = snapshot.child("productImage").getValue().toString();
                        editedProductName.setText(snapshot.child("productName").getValue().toString());
                        editedProductDescription.setText(snapshot.child("productDescription").getValue().toString());
                        editedProductPrice.setText(snapshot.child("productPrice").getValue().toString());
                        editedProductStock.setText(snapshot.child("productStock").getValue().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        editProductToolbar = view.findViewById(R.id.edit_product_toolbar);
        editProductToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
                fragmentTransaction.replace(R.id.register_framelayout, new MainFragment());
                fragmentTransaction.commit();

            }
        });


        pickEditedProductImage = view.findViewById(R.id.pick_edited_product_image);
        pickEditedProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity().start(view.getContext(), EditProductFragment.this);

            }
        });

        editButton = view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editedProductName.getText().toString().isEmpty() || editedProductDescription.getText().toString().isEmpty() || editedProductPrice.getText().toString().isEmpty() || editedProductStock.getText().toString().isEmpty()){

                    Toast.makeText(view.getContext(), getResources().getString(R.string.please_enter_full_data), Toast.LENGTH_SHORT).show();

                }else if (!editedProductName.getText().toString().isEmpty() && !editedProductDescription.getText().toString().isEmpty() && !editedProductPrice.getText().toString().isEmpty() && !editedProductStock.getText().toString().isEmpty()){

                    editProductProgressBar = view.findViewById(R.id.edit_product_progress);
                    editProductProgressBar.setVisibility(View.VISIBLE);

                    if (selectedEditedProductImage == null){

                        editProduct(view,selectCategorySpinner.getSelectedItem().toString(), selectSubcategorySpinner.getSelectedItem().toString(), selectProductSpinner.getSelectedItem().toString(), spareProductImage, editedProductName.getText().toString(), editedProductDescription.getText().toString(), editedProductPrice.getText().toString(), editedProductStock.getText().toString());

                    }else {

                        final StorageReference storageReference = constants.getStorageReference().child("product_images/" + editedProductName.getText().toString());
                        UploadTask uploadTask = storageReference.putFile(selectedEditedProductImage);
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
                                    editProduct(view,selectCategorySpinner.getSelectedItem().toString(), selectSubcategorySpinner.getSelectedItem().toString(), selectProductSpinner.getSelectedItem().toString(), imageUrl, editedProductName.getText().toString(), editedProductDescription.getText().toString(), editedProductPrice.getText().toString(), editedProductStock.getText().toString());

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
                selectCategorySpinner = (Spinner) view.findViewById(R.id.select_category_spinner_in_edit);
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
                    selectSubcategorySpinner = (Spinner) view.findViewById(R.id.select_model_spinner_in_edit);
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

    public void addProductsToProductsSpinner(final View view, final String mainCategory, final String mainSubcategory){

        if (!mainCategory.equals(getResources().getString(R.string.please_add_category)) || !mainSubcategory.equals(getResources().getString(R.string.please_add_subcategory))){

            final List<String> productsSpinnerArray = new ArrayList<String>();

            constants.getDatabaseReference().child("Categories").child(mainCategory).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    productsSpinnerArray.clear();

                    for (DataSnapshot dataSnapshot : snapshot.child(mainSubcategory).getChildren()){

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                            productsSpinnerArray.add(dataSnapshot.child("productStandardName").getValue(String.class));

                        }

                    }

                    LinkedHashSet<String> hashSet = new LinkedHashSet<String>(productsSpinnerArray);

                    ArrayList<String> spinnerWithoutDuplicates = new ArrayList<>(hashSet);

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, spinnerWithoutDuplicates);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selectProductSpinner = (Spinner) view.findViewById(R.id.select_product_spinner_in_edit);
                    if (spinnerWithoutDuplicates.size() == 0){

                        spinnerWithoutDuplicates.add(0, getResources().getString(R.string.please_add_product));

                    }else if (spinnerWithoutDuplicates.size() > 0){

                        if (isAdded() || isVisible()){

                            spinnerWithoutDuplicates.remove(getResources().getString(R.string.please_add_product));

                        }

                    }
                    selectProductSpinner.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    public void editProduct(View view, final String mainCategory, final String mainSubcategory, final String mainProduct, final String editedProductImage, final String editedProductName, final String editedProductDescription, final String editedProductPrice, final String editedProductStock){

        constants.getDatabaseReference().child("Categories").child(mainCategory).child(mainSubcategory).child(mainProduct).child("productImage").setValue(editedProductImage);
        constants.getDatabaseReference().child("Categories").child(mainCategory).child(mainSubcategory).child(mainProduct).child("productName").setValue(editedProductName);
        constants.getDatabaseReference().child("Categories").child(mainCategory).child(mainSubcategory).child(mainProduct).child("productDescription").setValue(editedProductDescription);
        constants.getDatabaseReference().child("Categories").child(mainCategory).child(mainSubcategory).child(mainProduct).child("productPrice").setValue(editedProductPrice);
        constants.getDatabaseReference().child("Categories").child(mainCategory).child(mainSubcategory).child(mainProduct).child("productStock").setValue(editedProductStock);

        restartApp(view.getContext());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){

                selectedEditedProductImage = result.getUri();
                Picasso.get().load(selectedEditedProductImage).into(pickedEditedProductImage);

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