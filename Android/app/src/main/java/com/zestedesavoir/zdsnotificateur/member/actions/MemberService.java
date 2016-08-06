package com.zestedesavoir.zdsnotificateur.member.actions;

import com.zestedesavoir.zdsnotificateur.member.ListMember;
import com.zestedesavoir.zdsnotificateur.member.Member;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * @author Gerard Paligot
 */
public interface MemberService {
  @GET("/api/membres/") Call<ListMember> list(
      @Header("Authorization") String authorization,
      @Query("search") String search,
      @Query("page") int page,
      @Query("page_size") int pageSize
  );

  @FormUrlEncoded @POST("/api/membres/") Call<Member> create(
      @Field("username") String username,
      @Field("email") String email,
      @Field("password") String password
  );

  @GET("/api/membres/mon-profil/") Call<Member> profile(
      @Header("Authorization") String authorization
  );
}
