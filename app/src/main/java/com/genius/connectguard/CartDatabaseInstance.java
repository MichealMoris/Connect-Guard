package com.genius.connectguard;

import android.content.Context;

import androidx.room.Room;

public class CartDatabaseInstance {

    private Context mCtx;
    private static CartDatabaseInstance mInstance;

    //our app database object
    private CartDatabase cartDatabase;

    private CartDatabaseInstance(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        cartDatabase = Room.databaseBuilder(mCtx, CartDatabase.class, "CartOrders").fallbackToDestructiveMigration().build();
    }

    public static synchronized CartDatabaseInstance getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new CartDatabaseInstance(mCtx);
        }
        return mInstance;
    }

    public CartDatabase getAppDatabase() {
        return cartDatabase;
    }

}
