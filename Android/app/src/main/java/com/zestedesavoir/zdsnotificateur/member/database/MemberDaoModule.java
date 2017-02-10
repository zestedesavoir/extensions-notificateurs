package com.zestedesavoir.zdsnotificateur.member.database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Gerard Paligot
 */
@Module(library = true, complete = false)
public final class MemberDaoModule {
  @Provides @Singleton MemberDao provideMemberDao(MemberDaoImpl dao) {
    return dao;
  }

  @Provides @Singleton AuthenticatedMemberDao provideAuthenticatedMemberDao(AuthenticatedMemberDaoImpl dao) {
    return dao;
  }
}
