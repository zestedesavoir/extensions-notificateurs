package com.zestedesavoir.zdsnotificateur.member.queries;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;
import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;
import com.zestedesavoir.zdsnotificateur.internal.query.TokenQueryParameter;
import com.zestedesavoir.zdsnotificateur.internal.utils.NetworkConnectivity;
import com.zestedesavoir.zdsnotificateur.member.Member;
import com.zestedesavoir.zdsnotificateur.member.actions.MyProfileAction;
import com.zestedesavoir.zdsnotificateur.member.database.AuthenticatedMemberDao;

import javax.inject.Inject;

/**
 * @author Gerard Paligot
 */
public final class MyProfileQuery implements QueryParameter<Member, TokenQueryParameter> {
  private final Context context;
  private final AuthenticatedMemberDao memberDao;
  private final MyProfileAction action;
  private final NetworkConnectivity networkConnectivity;

  @Inject public MyProfileQuery(Context context, AuthenticatedMemberDao memberDao, MyProfileAction action, NetworkConnectivity networkConnectivity) {
    this.context = context;
    this.memberDao = memberDao;
    this.action = action;
    this.networkConnectivity = networkConnectivity;
  }

  @Override public void execute(TokenQueryParameter parameterQuery, final Callback<Member> callback) {
    final Member member = memberDao.get(0);
    if (member != null) {
      callback.success(member);
    } else if (networkConnectivity.isConnected(context)) {
      action.execute(parameterQuery.token, new Callback<Member>() {
        @Override public void success(Member member) {
          memberDao.save(true, member);
          callback.success(member);
        }

        @Override public void failure(Throwable e) {
          callback.failure(e);
        }
      });
    } else {
      callback.failure(new ZdSException(context.getString(R.string.exception_connection)));
    }
  }
}
