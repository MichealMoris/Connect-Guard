package com.genius.constants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.genius.connectguard.R;
import com.genius.models.productModel;
import com.genius.models.userModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class constants
{

    public static userModel userModels ;
    public static productModel productModel ;

    //progress dialog
    private static ProgressDialog progressDialog ;

    private static int pageNum;

    //firebase Auth
    private static FirebaseAuth auth ;

    //firebase database
    private static FirebaseDatabase firebaseDatabase;

    //databas refrence
    private static DatabaseReference databaseReference;

    //firebase stroage
    private static FirebaseStorage firebaseStorage;

    //storage refrences

    private static StorageReference storageReference ;

    //shared preferances
    private static SharedPreferences sharedPreferences ;

    private static SharedPreferences.Editor editor ;


    public  static void replaceFragment(Fragment from , Fragment to ,Boolean save)
    {
        if (save)
        {
            from
                    .requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.register_framelayout,to)
                    .addToBackStack(null)
                    .commit();
        }else
        {


            from
                    .requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.register_framelayout,to)
                    .disallowAddToBackStack()
                    .commit();
        }

    }

    public  static void showToast(Context context , String body)
    {
        Toast.makeText(context,body, Toast.LENGTH_SHORT).show();
    }

    public  static void initProgress(Context context , String body)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(body);
        progressDialog.setCancelable(false);
    }

    public  static void showProgress()
    {
        progressDialog.show();
    }

    public  static void dissmisProgress()
    {
        progressDialog.dismiss();
    }

    public  static void initFireBase()
    {
        auth = FirebaseAuth.getInstance();

        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseStorage = firebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

    }

    public  static FirebaseAuth getAuth()
    {
        return auth;
    }

    public  static DatabaseReference getDatabaseReference()
    {
        return databaseReference;
    }

    public  static StorageReference getStorageReference()
    {
        return storageReference;
    }

    public static void initPref (Activity activity)
    {
        sharedPreferences = activity.getSharedPreferences("gruad2",Context.MODE_PRIVATE);
    }

    public static void saveUId(Activity activity,String id)
    {
        sharedPreferences = activity.getSharedPreferences("gruad2",Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.putString("uId",id);
        editor.apply();
    }

    public static String getUId (Activity activity)
    {
        sharedPreferences = activity.getSharedPreferences("gruad2",Context.MODE_PRIVATE);
        return sharedPreferences.getString("uId","empty");
    }

    public static void saveProductId(Context context,String id)
    {
        sharedPreferences = context.getSharedPreferences("gruad3",Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.putString("uId",id);
        editor.apply();
    }

    public static String getProductId (Context context)
    {
        sharedPreferences = context.getSharedPreferences("gruad3",Context.MODE_PRIVATE);
        return sharedPreferences.getString("uId","empty");
    }

    public static String getLanguage(Activity activity){

        final String[] language = new String[1];

        getDatabaseReference().child("Users").child(constants.getUId(activity)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                language[0] = snapshot.child("language").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return language[0];
    }

    public static void setLanguage(Activity activity,String language){

        getDatabaseReference().child("Users").child(constants.getUId(activity)).child("language").setValue(language);

    }

    public static void saveProductAmount(Context context, String productName, int id)
    {
        sharedPreferences = context.getSharedPreferences("gruad4",Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.putInt(productName,id);
        editor.apply();
    }

    public static int getProductAmount (Context context, String productName)
    {
        sharedPreferences = context.getSharedPreferences("gruad4",Context.MODE_PRIVATE);
        return sharedPreferences.getInt(productName,0);
    }

    public static void saveProductName(Context context, String productName)
    {
        sharedPreferences = context.getSharedPreferences("gruad5",Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.putString("productName", productName);
        editor.apply();
    }

    public static String getProductName (Context context)
    {
        sharedPreferences = context.getSharedPreferences("gruad5",Context.MODE_PRIVATE);
        return sharedPreferences.getString("productName","empty");
    }
    public static long getTime()
    {
        return System.currentTimeMillis();
    }


}
