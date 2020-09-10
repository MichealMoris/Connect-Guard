package com.genius.connectguard.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAoZl3JzU:APA91bE__ByXDyGbuNdT3zMqD8W2ejH2QN1_FmDVtngx-W4Wg1E5XK1qO0VbkxH08Vmduqehqu2W2f6MavCR94LwH21D_T5CgttwWjom3wM1D6jxsoCOpNjJxqmxGy4GNnSwN3Eygh6x"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

