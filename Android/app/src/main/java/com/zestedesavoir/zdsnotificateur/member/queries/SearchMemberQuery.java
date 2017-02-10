package com.zestedesavoir.zdsnotificateur.member.queries;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;
import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;
import com.zestedesavoir.zdsnotificateur.internal.utils.NetworkConnectivity;
import com.zestedesavoir.zdsnotificateur.member.Member;
import com.zestedesavoir.zdsnotificateur.member.actions.SearchMemberAction;
import com.zestedesavoir.zdsnotificateur.member.actions.SearchMemberParameter;
import com.zestedesavoir.zdsnotificateur.member.database.MemberDao;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Gerard Paligot
 */
public final class SearchMemberQuery implements QueryParameter<List<Member>, SearchMemberQueryParameter> {
  private final Context context;
  private final MemberDao memberDao;
  private final SearchMemberAction action;
  private final NetworkConnectivity networkConnectivity;

  @Inject public SearchMemberQuery(Context context, MemberDao memberDao, SearchMemberAction action, NetworkConnectivity networkConnectivity) {
    this.context = context;
    this.memberDao = memberDao;
    this.action = action;
    this.networkConnectivity = networkConnectivity;
  }

  @Override public void execute(SearchMemberQueryParameter parameterQuery, final Callback<List<Member>> callback) {
    if (networkConnectivity.isConnected(context)) {
      final SearchMemberParameter param = new SearchMemberParameter(parameterQuery.search, parameterQuery.page, parameterQuery.sizePage);
      action.execute(parameterQuery.token, param, new Callback<List<Member>>() {
        @Override public void success(List<Member> members) {
          memberDao.save(true, members.toArray(new Member[members.size()]));
          callback.success(members);
        }

        @Override public void failure(Throwable e) {
          callback.failure(e);
        }
      });
      return;
    }
    final List<Member> allMembers = memberDao.search(parameterQuery.search);
    if (allMembers != null && allMembers.size() > 0) {
      callback.success(allMembers);
    } else {
      callback.failure(new ZdSException(context.getString(R.string.exception_internal)));
    }
  }
}
