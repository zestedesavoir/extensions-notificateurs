package com.zestedesavoir.android.login.managers;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zestedesavoir.android.BuildConfig;
import com.zestedesavoir.android.login.models.Query;
import com.zestedesavoir.android.login.models.Token;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AutoLoginManagerImpl implements AutoLoginManager {
    private final Context context;
    private final TokenService service;

    public AutoLoginManagerImpl(Context context) {
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        final OkHttpClient okhttp = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okhttp)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.service = retrofit.create(TokenService.class);
        this.context = context;
    }

    @Override
    public Call<Token> authenticateByToken() {
        return service.refreshToken(new TokenService.RefreshTokenParameter(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, new Query(context).get().refreshToken()));
    }
}
