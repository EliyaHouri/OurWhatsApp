package com.example.ourwhatsapp.API.APIs;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ourwhatsapp.API.Entities.CreateUserReq;
import com.example.ourwhatsapp.API.Entities.GetTokenReq;
import com.example.ourwhatsapp.API.WebServiceAPI;
import com.example.ourwhatsapp.Utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private final WebServiceAPI webServiceAPI;
    private final Context context;

    public UserAPI(Context context) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.getURL(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.webServiceAPI = retrofit.create(WebServiceAPI.class);
        this.context = context;
    }

    public void tryLogin(String username, String password, MutableLiveData<String> res) {
        Call<ResponseBody> call = webServiceAPI.getToken(new GetTokenReq(username, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    try {
                        assert responseBody != null;
                        res.postValue(responseBody.string());
                    } catch (Exception exception) {
                        res.postValue(null);
                    }
                } else {
                    Toast.makeText(context, "code:" + response.code(), Toast.LENGTH_SHORT).show();
                    res.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                res.postValue(null);
            }
        });
    }

    public void tryRegister(String username, String password, String displayName, String profilePic, MutableLiveData<Integer> res) {
        Call<Void> call = webServiceAPI.createUser(new CreateUserReq(username, password, displayName, profilePic));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                res.postValue(response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                res.postValue(500);
            }
        });
    }
}