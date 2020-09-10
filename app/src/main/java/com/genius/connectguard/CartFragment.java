package com.genius.connectguard;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.genius.connectguard.SendNotificationPack.APIService;
import com.genius.connectguard.SendNotificationPack.Client;
import com.genius.connectguard.SendNotificationPack.Data;
import com.genius.connectguard.SendNotificationPack.MyResponse;
import com.genius.connectguard.SendNotificationPack.NotificationSender;
import com.genius.connectguard.SendNotificationPack.Token;
import com.genius.constants.constants;
import com.genius.models.CartModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private RecyclerView cart_recyclerView;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    private List<CartModel> cartModelList = new ArrayList<>();
    private String key;
    private TextView decreaseAmount;
    private TextView increaseAmount;
    private TextView amount;
    private List<CartModel> cartList = new ArrayList<>();
    private CartRecyclerViewAdapter adapter;
    private ElegantNumberButton elegantNumberButton;
    private TextView total;
    private Button checkOut;
    private APIService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_cart, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
                    fragmentTransaction.replace(R.id.register_framelayout, new MainFragment());
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        } );

        SharedPreferences preferences = getActivity().getSharedPreferences("appLanguage", Context.MODE_PRIVATE);

        if (preferences.getString("langCode", "en").equals("ar")){

            view.setRotationY(180f);

        }

        checkOut = view.findViewById(R.id.btn_checkOut);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (!constants.getUId(getActivity()).equals("empty")){


                            apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
                            String UserTB = "qvp6t42nXvadCmafyt5v7iXTJKO2";

                            FirebaseDatabase.getInstance().getReference().child("Tokens").child(UserTB.trim()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String usertoken=dataSnapshot.getValue(String.class);
                                    sendNotifications(usertoken, "New Order!", "We Have Received A New Order Check Your Orders");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            new CheckOutTask().execute();

                        }else if (CartDatabaseInstance.getInstance(getActivity().getApplicationContext()).getAppDatabase().cartDao().getAllCartOrders().size() != 0){

                            setFragment(new SignInFragment());

                        }

                    }
                }).start();

            }
        });

        setRecyclerView(view);
        new TotalCounterTask().execute();

        UpdateToken();

        return view;
    }

    private void setFragment(Fragment mainFragment) {

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.register_framelayout,mainFragment)
                .commit();

    }

    public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.CartViewHolder>{

        List<CartModel> cartModelList;

        public CartRecyclerViewAdapter(List<CartModel> cartModelList) {
            this.cartModelList = cartModelList;
            notifyDataSetChanged();
        }

        public CartRecyclerViewAdapter() {
        }

        @NonNull
        @Override
        public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_cart_item, parent , false);

            return new CartViewHolder(view);


        }

        @Override
        public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {

            Picasso.get().load(cartModelList.get(position).getProduct_image()).into(holder.product_image);
            holder.product_name.setText(cartModelList.get(position).getProduct_name());
            holder.product_model.setText(cartModelList.get(position).getProduct_subcatgory());
            constants.getDatabaseReference().child("Categories").child(cartModelList.get(position).getProduct_catgory()).child(cartModelList.get(position).getProduct_subcatgory()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    holder.elegantNumberButton.setRange(0, Integer.parseInt(snapshot.child(cartModelList.get(position).getProduct_standard_name()).child("productStock").getValue().toString()));
                    if (Integer.parseInt(holder.elegantNumberButton.getNumber()) == Integer.parseInt(snapshot.child(cartModelList.get(position).getProduct_standard_name()).child("productStock").getValue().toString())){

                        Toast.makeText(holder.itemView.getContext(), holder.itemView.getResources().getString(R.string.no_more_stock), Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            holder.elegantNumberButton.setNumber(String.valueOf(Integer.valueOf(cartModelList.get(position).getProduct_amount())));
            holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton v, int oldValue, final int newValue) {

                    class UpdateOrderAmount extends AsyncTask<Void, Void, Void>{

                        @Override
                        protected Void doInBackground(Void... voids) {

                            CartModel cartModel = new CartModel();
                            cartModel.setId(cartModelList.get(position).getId());

                            CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().updateOrderAmount(cartModelList.get(position).getProduct_name(), String.valueOf(newValue));
                            CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().updateOrderTotal(cartModelList.get(position).getProduct_name(), String.valueOf(newValue * cartModelList.get(position).getProduct_price()));

                            if (cartModelList.get(position).getProduct_amount().equals("0") || newValue == 0){

                                CartDatabaseInstance.getInstance(holder.itemView.getContext()).getAppDatabase().cartDao().deleteCartItem(cartModel);

                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            getFragmentManager().beginTransaction().detach(CartFragment.this).attach(CartFragment.this).commit();

                        }
                    }
                    new UpdateOrderAmount().execute();

                }
            });
            holder.orderTotal.setText(String.valueOf(cartModelList.get(position).getOrder_total()) + " L.E");

        }

        @Override
        public int getItemCount() {
            return cartModelList.size();
        }

        public int getCartListSize(){return cartModelList.size();}

        public List<CartModel> getCartModelList(){return cartModelList;}

        public class CartViewHolder extends RecyclerView.ViewHolder{

            ImageView product_image;
            TextView product_name;
            TextView product_model;
            ElegantNumberButton elegantNumberButton;
            TextView orderTotal;

            public CartViewHolder(@NonNull View itemView) {
                super(itemView);

                product_image = itemView.findViewById(R.id.iv_product);
                product_name = itemView.findViewById(R.id.tv_productName);
                product_model = itemView.findViewById(R.id.tv_category);
                elegantNumberButton = itemView.findViewById(R.id.order_amount);
                orderTotal = itemView.findViewById(R.id.order_item_total_price);

            }
        }


    }


    public void setRecyclerView(final View view){

        class GetCartOrdersTask extends AsyncTask<Void, Void, List<CartModel>> {

            @Override
            protected List<CartModel> doInBackground(Void... voids) {
                List<CartModel> cartOrdersList = CartDatabaseInstance
                        .getInstance(view.getContext())
                        .getAppDatabase()
                        .cartDao()
                        .getAllCartOrders();
                CartDatabaseInstance.getInstance(view.getContext()).getAppDatabase().cartDao().deleteDuplicates();
                return cartOrdersList;
            }

            @Override
            protected void onPostExecute(List<CartModel> cartModelList) {
                super.onPostExecute(cartModelList);

                cart_recyclerView = view.findViewById(R.id.cart_items);
                adapter = new CartRecyclerViewAdapter(cartModelList);
                cart_recyclerView.setAdapter(adapter);
                cart_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

            }
        }


        GetCartOrdersTask getCartOrdersTask = new GetCartOrdersTask();
        getCartOrdersTask.execute();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        }
    }

    class TotalCounterTask extends AsyncTask<Void, Void, Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {

            List<CartModel> getTotalAndAmount = CartDatabaseInstance.getInstance(getActivity().getApplicationContext()).getAppDatabase().cartDao().getAllCartOrders();
            int totalNum = 0;
            for (int i = 0; i < getTotalAndAmount.size(); i++){

                totalNum = totalNum + (getTotalAndAmount.get(i).getProduct_price() * Integer.parseInt(getTotalAndAmount.get(i).getProduct_amount()));

            }

            return totalNum;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            try {

                total = getActivity().findViewById(R.id.tv_total);
                total.setText(integer.toString()+" L.E");

            }catch (Exception e){



            }

        }
    }

    class CheckOutTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            constants.getDatabaseReference().child("Orders").push();
            final List<CartModel> cartModelList = CartDatabaseInstance.getInstance(getActivity().getApplicationContext()).getAppDatabase().cartDao().getAllCartOrders();
            constants.getDatabaseReference().child("Users").child(constants.getUId(getActivity())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (int i = 0; i < cartModelList.size(); i++){

                        Random rand = new Random();
                        final String orderNum = String.valueOf(rand.nextInt(3000));
                        constants.getDatabaseReference().child("Orders").child(orderNum).child("order_number").setValue(orderNum);
                        constants.getDatabaseReference().child("Orders").child(orderNum).child("order_user_image").setValue(snapshot.child("userImage").getValue().toString());
                        constants.getDatabaseReference().child("Orders").child(orderNum).child("order_username").setValue(snapshot.child("name").getValue().toString());
                        constants.getDatabaseReference().child("Orders").child(orderNum).child("order_user_email").setValue(snapshot.child("email").getValue().toString());
                        constants.getDatabaseReference().child("Orders").child(orderNum).child("order_user_address").setValue(snapshot.child("adress").getValue().toString());
                        constants.getDatabaseReference().child("Orders").child(orderNum).child("order_user_phone_number").setValue(snapshot.child("mobile").getValue().toString());
                        constants.getDatabaseReference().child("Orders").child(orderNum).child("order_product_name").setValue(cartModelList.get(i).getProduct_name());
                        constants.getDatabaseReference().child("Orders").child(orderNum).child("order_product_amount").setValue(cartModelList.get(i).getProduct_amount());
                        constants.getDatabaseReference().child("Orders").child(orderNum).child("order_product_model").setValue(cartModelList.get(i).getProduct_subcatgory());
                        constants.getDatabaseReference().child("Orders").child(orderNum).child("order_total_price").setValue(cartModelList.get(i).getOrder_total());
                        constants.getDatabaseReference().child("Orders").child(orderNum).child("order_price").setValue(cartModelList.get(i).getProduct_price());
                        constants.getDatabaseReference().child("Orders").child(orderNum).child("order_step").setValue("new order");

                        final int finalI = i;

                        constants.getDatabaseReference().child("Categories").child(cartModelList.get(i).getProduct_catgory()).child(cartModelList.get(i).getProduct_subcatgory()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                snapshot.getRef().child(cartModelList.get(finalI).getProduct_standard_name()).child("productStock").setValue(String.valueOf(Integer.valueOf(snapshot.child(cartModelList.get(finalI).getProduct_standard_name()).child("productStock").getValue().toString()) - Integer.parseInt(cartModelList.get(finalI).getProduct_amount())));

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            CartDatabaseInstance.getInstance(getActivity().getApplicationContext()).getAppDatabase().cartDao().deleteAll();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.done_ordering_icon);

            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "NewOrder";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Order Notification", NotificationManager.IMPORTANCE_MAX);

                notificationChannel.setDescription("New Order Received!");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.BLUE);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);

                notificationManager.createNotificationChannel(notificationChannel);

            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), NOTIFICATION_CHANNEL_ID);

            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setLargeIcon(icon)
                    .setSmallIcon(R.drawable.done_ordering_icon)
                    .setTicker("Fixed")
                    .setContentTitle("Thank You For Ordering!")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Your Order Has Been Sent And We Will Contact You As Soon As Possible"))
                    .setContentText("Your Order Has Been Sent And We Will Contact You As Soon As Possible")
                    .setContentInfo("sent");

            notificationManager.notify(1, builder.build());

            Intent intent = new Intent(getContext(), RegisterActivity.class);
            getActivity().overridePendingTransition(R.anim.fade_out_anim, R.anim.fade_in_anim);
            getActivity().finish();
            getActivity().startActivity(intent);

        }

    }

    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){

            String refreshToken= FirebaseInstanceId.getInstance().getToken();
            Token token= new Token(refreshToken);
            FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);

        }
    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(getContext(), "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

}