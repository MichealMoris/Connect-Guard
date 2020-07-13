package com.genius.connectguard;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.genius.models.CartModel;

import java.util.List;

@Dao
public interface CartDao {

    @Query("SELECT * FROM cartmodel")
    List<CartModel> getAllCartOrders();

    @Insert
    void addToCart(CartModel cartModel);

    @Delete
    void deleteCartItem(CartModel cartModel);

    @Update
    void updateCart(CartModel cartModel);

}
