package com.genius.connectguard;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.genius.models.CartModel;

@Database(entities = {CartModel.class}, version = 2)
public abstract class CartDatabase extends RoomDatabase {

    public abstract CartDao cartDao();

}
