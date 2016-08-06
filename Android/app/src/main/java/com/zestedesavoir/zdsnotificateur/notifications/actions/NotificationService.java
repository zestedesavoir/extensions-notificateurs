package com.zestedesavoir.zdsnotificateur.notifications.actions;

import com.zestedesavoir.zdsnotificateur.notifications.ListNotification;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

/**
 * @author Gerard Paligot
 */
public interface NotificationService {
  @GET("/api/notifications/?expand=sender&expand=subscription") Call<ListNotification> list(
      @Header("Authorization") String authorization,
      @Query("page") int page,
      @Query("page_size") int pageSize
  );
}
