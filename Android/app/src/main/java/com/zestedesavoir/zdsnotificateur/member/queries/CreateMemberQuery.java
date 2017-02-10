package com.zestedesavoir.zdsnotificateur.member.queries;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;
import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;
import com.zestedesavoir.zdsnotificateur.internal.utils.NetworkConnectivity;
import com.zestedesavoir.zdsnotificateur.member.Member;
import com.zestedesavoir.zdsnotificateur.member.actions.CreateMemberAction;
import com.zestedesavoir.zdsnotificateur.member.actions.CreateMemberParameter;

import javax.inject.Inject;

/**
 * @author Gerard Paligot
 */
public final class CreateMemberQuery implements QueryParameter<Member, CreateMemberQueryParameter> {
  private final Context context;
  private final CreateMemberAction action;
  private final NetworkConnectivity networkConnectivity;

  @Inject public CreateMemberQuery(Context context, CreateMemberAction action, NetworkConnectivity networkConnectivity) {
    this.context = context;
    this.action = action;
    this.networkConnectivity = networkConnectivity;
  }

  @Override public void execute(CreateMemberQueryParameter parameterQuery, final Callback<Member> callback) {
    if (networkConnectivity.isConnected(context)) {
      final CreateMemberParameter param = new CreateMemberParameter(parameterQuery.username, parameterQuery.email, parameterQuery.password);
      action.request(param, new Callback<Member>() {
        @Override public void success(Member members) {
          callback.success(members);
        }

        @Override public void failure(Throwable e) {
          callback.failure(e);
        }
      });
    } else {
      callback.failure(new ZdSException("You must to be connected to create a new member."));
    }
  }
}
