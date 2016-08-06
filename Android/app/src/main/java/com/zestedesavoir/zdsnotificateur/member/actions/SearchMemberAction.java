package com.zestedesavoir.zdsnotificateur.member.actions;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.action.AbstractActionParameter;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;
import com.zestedesavoir.zdsnotificateur.member.ListMember;
import com.zestedesavoir.zdsnotificateur.member.Member;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Call;
import retrofit.Response;

/**
 * @author Gerard Paligot
 */
@Singleton
public final class SearchMemberAction extends AbstractActionParameter<List<Member>, String, SearchMemberParameter> {
  private final Context context;
  private final MemberService service;

  @Inject public SearchMemberAction(Context context, MemberService service) {
    this.context = context;
    this.service = service;
  }

  @Override protected void request(final SearchMemberParameter param, final Callback<List<Member>> callback) {
    throw new ZdSException("Request to get search members must be a request authenticated.");
  }

  @Override protected void requestAuthenticated(String token, final SearchMemberParameter param, final Callback<List<Member>> callback) {
    final Call<ListMember> search = service.list("Bearer " + token, param.search, param.page, param.pageSize);
    search.enqueue(new retrofit.Callback<ListMember>() {
      @Override public void onResponse(Response<ListMember> response) {
        if (response.isSuccess()) {
          new CacheCallback(callback, param).success(response.body().results);
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
