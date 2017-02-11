package com.zestedesavoir.android.notification.managers;

import com.zestedesavoir.android.notification.models.ListNotification;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

public interface NotificationService {
    @GET("/api/notifications/?expand=sender&expand=subscription&ordering=-pubdate")
    Observable<ListNotification> list(
            @Header("Authorization") String authorization,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );
}