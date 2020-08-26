package com.genius.connectguard;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.constants.constants;
import com.genius.models.CartModel;
import com.genius.models.productModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsRecyclerviewAdapter extends RecyclerView.Adapter<ProductsRecyclerviewAdapter.ProductsRecyclerviewViewHolder> {

    List<productModel> productList;
    String mainModelName;
    String mainCategoryName;

    public ProductsRecyclerviewAdapter(List<productModel> productList, String mainCategoryName, String mainModelName) {
        this.productList = productList;
        this.mainCategoryName = mainCategoryName;
        this.mainModelName = mainModelName;
    }

    @NonNull
    @Override
    public ProductsRecyclerviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductsRecyclerviewViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsRecyclerviewViewHolder holder, final int position) {

        Picasso.get().load(productList.get(position).getProductImage()).into(holder.productImage);
        holder.productName.setText(productList.get(position).getProductName());
        holder.productDescription.setText(productList.get(position).getProductDescription());
        holder.productPrice.setText(productList.get(position).getProductPrice() + " L.E");
        holder.productSoldOut.setVisibility(View.GONE);
        /*if (productList.get(position).isSoldOut()){

            holder.productSoldOut.setVisibility(View.VISIBLE);

        }*/
        constants.getDatabaseReference().child("Categories").child(mainCategoryName).child(mainModelName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               if (Integer.valueOf(snapshot.child(productList.get(position).getStandardProductName()).child("productStock").getValue().toString()) == 0){

                   holder.productSoldOut.setVisibility(View.VISIBLE);
                   holder.addToCartIcon.setVisibility(View.GONE);
                   holder.addToCartButton.setVisibility(View.GONE);
                   holder.productPrice.setVisibility(View.GONE);

               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final boolean[] isAdded = {false};

                class AddToCartTask extends AsyncTask<Void, Void, Void>{

                    @Override
                    protected Void doInBackground(Void... voids) {

                        if (CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().getAllCartOrders().size() == 0){

                            isAdded[0] = false;
                            CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().addToCart(new CartModel(productList.get(position).getProductImage(), productList.get(position).getStandardProductName(),productList.get(position).getProductName(), mainCategoryName, mainModelName, "1", Integer.parseInt(productList.get(position).getProductPrice()), Integer.parseInt(productList.get(position).getProductPrice()) * 1, Integer.parseInt(productList.get(position).getProductPrice()) * 1));
                            CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().deleteDuplicates();

                        }else {

                            for (int i = 0; i < CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().getAllCartOrders().size(); i++) {

                                try {
                                    if (CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().getCartItem(productList.get(position).getProductName()).getProduct_name() != null) {

                                        isAdded[0] = true;
                                        CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().deleteDuplicates();
                                        break;

                                    } else if (!productList.get(position).getProductName().equals(CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().getCartItem(productList.get(position).getProductName()))) {

                                        isAdded[0] = false;
                                        CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().addToCart(new CartModel(productList.get(position).getProductImage(), productList.get(position).getStandardProductName(), productList.get(position).getProductName(), mainCategoryName, mainModelName, "1", Integer.parseInt(productList.get(position).getProductPrice()), Integer.parseInt(productList.get(position).getProductPrice()) * 1, Integer.parseInt(productList.get(position).getProductPrice()) * 1));
                                        CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().deleteDuplicates();
                                        break;

                                    }

                                }catch (Exception e){

                                    isAdded[0] = false;
                                    CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().addToCart(new CartModel(productList.get(position).getProductImage(), productList.get(position).getStandardProductName(), productList.get(position).getProductName(), mainCategoryName, mainModelName, "1", Integer.parseInt(productList.get(position).getProductPrice()), Integer.parseInt(productList.get(position).getProductPrice()) * 1, Integer.parseInt(productList.get(position).getProductPrice()) * 1));
                                    CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().deleteDuplicates();
                                    break;

                                }
                            }

                        }


                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);


                        if (isAdded[0]){

                            Toast.makeText(holder.itemView.getContext(), holder.itemView.getResources().getString(R.string.this_item_already_in_the_cart), Toast.LENGTH_SHORT).show();

                        }else if (!isAdded[0]) {

                            Toast.makeText(holder.itemView.getContext(), productList.get(position).getProductName() + holder.itemView.getResources().getString(R.string.item_add_to_cart), Toast.LENGTH_SHORT).show();

                        }


                    }
                }

                new AddToCartTask().execute();


            }
        });

        holder.addToCartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final boolean[] isAdded = {false};

                class AddToCartTask extends AsyncTask<Void, Void, Void>{

                    @Override
                    protected Void doInBackground(Void... voids) {

                        if (CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().getAllCartOrders().size() == 0){

                            isAdded[0] = false;
                            CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().addToCart(new CartModel(productList.get(position).getProductImage(), productList.get(position).getStandardProductName(), productList.get(position).getProductName(), mainCategoryName, mainModelName, "1", Integer.parseInt(productList.get(position).getProductPrice()), Integer.parseInt(productList.get(position).getProductPrice()) * 1, Integer.parseInt(productList.get(position).getProductPrice()) * 1));
                            CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().deleteDuplicates();

                        }else {

                            for (int i = 0; i < CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().getAllCartOrders().size(); i++) {

                                try {
                                    if (CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().getCartItem(productList.get(position).getProductName()).getProduct_name() != null) {

                                        isAdded[0] = true;
                                        CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().deleteDuplicates();
                                        break;

                                    } else if (!productList.get(position).getProductName().equals(CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().getCartItem(productList.get(position).getProductName()))) {

                                        isAdded[0] = false;
                                        CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().addToCart(new CartModel(productList.get(position).getProductImage(), productList.get(position).getStandardProductName(), productList.get(position).getProductName(), mainCategoryName, mainModelName, "1", Integer.parseInt(productList.get(position).getProductPrice()), Integer.parseInt(productList.get(position).getProductPrice()) * 1, Integer.parseInt(productList.get(position).getProductPrice()) * 1));
                                        CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().deleteDuplicates();
                                        break;

                                    }

                                }catch (Exception e){

                                    isAdded[0] = false;
                                    CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().addToCart(new CartModel(productList.get(position).getProductImage(), productList.get(position).getStandardProductName(), productList.get(position).getProductName(), mainCategoryName, mainModelName, "1", Integer.parseInt(productList.get(position).getProductPrice()), Integer.parseInt(productList.get(position).getProductPrice()) * 1, Integer.parseInt(productList.get(position).getProductPrice()) * 1));
                                    CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().deleteDuplicates();
                                    break;

                                }
                            }

                        }


                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);


                        if (isAdded[0]){

                            Toast.makeText(holder.itemView.getContext(), holder.itemView.getResources().getString(R.string.this_item_already_in_the_cart), Toast.LENGTH_SHORT).show();

                        }else if (!isAdded[0]) {

                            Toast.makeText(holder.itemView.getContext(), productList.get(position).getProductName() + holder.itemView.getResources().getString(R.string.item_add_to_cart), Toast.LENGTH_SHORT).show();

                        }


                    }
                }

                new AddToCartTask().execute();


            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductsRecyclerviewViewHolder extends RecyclerView.ViewHolder{

        ImageView productImage;
        TextView productName;
        TextView productDescription;
        TextView productPrice;
        LinearLayout productSoldOut;
        Button addToCartButton;
        ImageView addToCartIcon;

        public ProductsRecyclerviewViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
            productPrice = itemView.findViewById(R.id.product_price);
            productSoldOut = itemView.findViewById(R.id.product_sold_out);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
            addToCartIcon = itemView.findViewById(R.id.add_to_cart_icon);

        }
    }

}
