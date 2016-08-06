package com.zestedesavoir.zdsnotificateur.member.actions;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;

/**
 * @author Gerard Paligot
 */
@Module(library = true, complete = false)
public final class MemberActionModule {
  @Provides @Singleton MemberService providesMemberService(Retrofit retrofit) {
    return retrofit.create(MemberService.class);
  }
}
