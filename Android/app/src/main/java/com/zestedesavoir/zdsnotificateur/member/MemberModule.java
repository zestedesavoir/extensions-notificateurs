package com.zestedesavoir.zdsnotificateur.member;

import com.zestedesavoir.zdsnotificateur.auth.Session;
import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;
import com.zestedesavoir.zdsnotificateur.member.actions.MemberActionModule;
import com.zestedesavoir.zdsnotificateur.member.database.MemberDaoModule;
import com.zestedesavoir.zdsnotificateur.member.queries.CreateMember;
import com.zestedesavoir.zdsnotificateur.member.queries.CreateMemberQueryParameter;
import com.zestedesavoir.zdsnotificateur.member.queries.MemberQueryModule;
import com.zestedesavoir.zdsnotificateur.member.queries.SearchMember;
import com.zestedesavoir.zdsnotificateur.member.queries.SearchMemberQueryParameter;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Gerard Paligot
 */
@Module(
    library = true,
    complete = false,
    includes = {
        MemberDaoModule.class,
        MemberActionModule.class,
        MemberQueryModule.class
    }
)
public final class MemberModule {
  @Provides @Singleton MemberManager providesMemberManager(
      Session session,
      @SearchMember QueryParameter<List<Member>, SearchMemberQueryParameter> searchMemberQuery,
      @CreateMember QueryParameter<Member, CreateMemberQueryParameter> createMember) {
    return new MemberManagerImpl(session, searchMemberQuery, createMember);
  }
}