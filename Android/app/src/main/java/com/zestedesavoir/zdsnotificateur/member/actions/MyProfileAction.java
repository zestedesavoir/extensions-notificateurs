package com.zestedesavoir.zdsnotificateur.member.actions;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.action.AbstractAction;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;
import com.zestedesavoir.zdsnotificateur.member.Member;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Call;
import retrofit.Response;

/**
 * @author Gerard Paligot
 */
@Singleton
public final class MyProfileAction extends AbstractAction<Member> {
  private final Context context;
  private final MemberService service;

  @Inject public MyProfileAction(Context context, MemberService service) {
    this.context = context;
    this.service = service;
  }

  @Override protected void request(Callback<Member> callback) {
    throw new ActionMemberException("Request to get profile must be a request authenticated.");
  }

  @Override protected void requestAuthenticated(String token, final Callback<Member> callback) {
    final Call<Member> search = service.profile("Bearer " + token);
    search.enqueue(new retrofit.Callback<Member>() {
      @Override public void onResponse(Response<Member> response) {
        if (response.isSuccess()) {
          callback.success(response.body());
        } else {
          callback.failure(new ZdSException(context.getString(R.string.exception_internal)));
        }
      }

      @Override public void onFailure(Throwable t) {
        callback.failure(new ZdSException(context.getString(R.string.exception_server), t));
      }
    });
  }
}
