package com.genius.connectguard;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.genius.models.CartModel;

import java.util.List;

@Dao
public abstract class CartDao {

    @Query("SELECT * FROM cartmodel")
    abstract List<CartModel> getAllCartOrders();

    @Query("SELECT * FROM cartmodel WHERE product_name == :productName")
    abstract CartModel getCartItem(String productName);

    @Insert
    abstract void addToCart(CartModel cartModel);

    @Delete
    abstract void deleteCartItem(CartModel cartModel);

    @Update
    abstract void updateCart(CartModel cartModel);

    public void updateOrderAmount(String productName, String amount) {
        CartModel cartModel = getCartItem(productName);
        cartModel.setProduct_amount(amount);
        updateCart(cartModel);
    }

    public void updateTotal(String productName, String amount) {
        CartModel cartModel = getCartItem(productName);
        cartModel.setTotal(Integer.parseInt(amount));
        updateCart(cartModel);
    }


    @Query("DELETE FROM cartmodel WHERE id NOT IN (SELECT MIN(id) FROM CartModel GROUP BY product_name, product_catgory)")
    abstract void deleteDuplicates();

}

