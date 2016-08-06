package com.zestedesavoir.zdsnotificateur.member;

import com.zestedesavoir.zdsnotificateur.auth.Session;
import com.zestedesavoir.zdsnotificateur.auth.Token;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ZdSException;
import com.zestedesavoir.zdsnotificateur.internal.query.QueryParameter;
import com.zestedesavoir.zdsnotificateur.member.queries.CreateMember;
import com.zestedesavoir.zdsnotificateur.member.queries.CreateMemberQueryParameter;
import com.zestedesavoir.zdsnotificateur.member.queries.SearchMember;
import com.zestedesavoir.zdsnotificateur.member.queries.SearchMemberQueryParameter;

import java.util.List;

import javax.inject.Inject;

import static com.zestedesavoir.zdsnotificateur.internal.utils.Util.checkArgument;
import static com.zestedesavoir.zdsnotificateur.internal.utils.Util.checkNotNull;

/**
 * @author Gerard Paligot
 */
public final class MemberManagerImpl implements MemberManager {
  private final Session session;
  private QueryParameter<List<Member>, SearchMemberQueryParameter> searchMemberQuery;
  private QueryParameter<Member, CreateMemberQueryParameter> createMember;

  @Inject public MemberManagerImpl(
      Session session,
      @SearchMember QueryParameter<List<Member>, SearchMemberQueryParameter> searchMemberQuery,
      @CreateMember QueryParameter<Member, CreateMemberQueryParameter> createMember) {
    this.session = session;
    this.searchMemberQuery = searchMemberQuery;
    this.createMember = createMember;
  }

  @Override public void register(String username, String password, String email, Callback<Member> callback) {
    checkArgument(username != null && !username.isEmpty(), "You must give an username not empty.");
    checkArgument(password != null && !username.isEmpty(), "You must give a password not empty.");
    checkArgument(email != null && !username.isEmpty(), "You must give an email not empty.");
    checkNotNull(callback, "Callback can't be null.");

    createMember.execute(new CreateMemberQueryParameter(username, email, password), callback);
  }

  @Override public void search(final String username, final int page, final int pageSize, final Callback<List<Member>> callback) {
    checkArgument(username != null && !username.isEmpty(), "You must give an username not empty.");
    checkArgument(page > 0, "Page number can't be negative.");
    checkArgument(pageSize > 0 && pageSize <= 50, "Size of the page must be between 1 and 50.");
    checkNotNull(callback, "Callback can't be null.");

    session.authenticateByToken(new Callback<Token>() {
      @Override public void success(Token token) {
        searchMemberQuery.execute(new SearchMemberQueryParameter(token.accessToken(), username, page, pageSize), callback);
      }

      @Override public void failure(Throwable e) {
        callback.failure(new ZdSException(e));
      }
    });
  }
}
