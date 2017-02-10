package com.zestedesavoir.zdsnotificateur.member.queries;

import android.content.Context;

import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;
import com.zestedesavoir.zdsnotificateur.internal.query.TokenQueryParameter;
import com.zestedesavoir.zdsnotificateur.internal.utils.NetworkConnectivity;
import com.zestedesavoir.zdsnotificateur.member.Member;
import com.zestedesavoir.zdsnotificateur.member.actions.CreateMemberAction;
import com.zestedesavoir.zdsnotificateur.member.actions.MyProfileAction;
import com.zestedesavoir.zdsnotificateur.member.actions.SearchMemberAction;
import com.zestedesavoir.zdsnotificateur.member.database.AuthenticatedMemberDao;
import com.zestedesavoir.zdsnotificateur.member.database.MemberDao;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Gerard Paligot
 */
@Module(library = true, complete = false)
public final class MemberQueryModule {
  @Provides @CreateMember @Singleton QueryParameter<Member, CreateMemberQueryParameter> providesCreateMemberQuery(
      Context context, CreateMemberAction action, NetworkConnectivity networkConnectivity) {
    return new CreateMemberQuery(context, action, networkConnectivity);
  }

  @Provides @SearchMember @Singleton QueryParameter<List<Member>, SearchMemberQueryParameter> providesSearchMemberQuery(
      Context context, MemberDao memberDao, SearchMemberAction searchMemberAction,
      NetworkConnectivity networkConnectivity) {
    return new SearchMemberQuery(context, memberDao, searchMemberAction, networkConnectivity);
  }

  @Provides @MyProfile @Singleton QueryParameter<Member, TokenQueryParameter> providesAuthenticatedMemberQuery(
      Context context, AuthenticatedMemberDao memberDao, MyProfileAction action,
      NetworkConnectivity networkConnectivity) {
    return new MyProfileQuery(context, memberDao, action, networkConnectivity);
  }
}
