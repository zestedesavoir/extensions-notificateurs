package com.zestedesavoir.zdsnotificateur.member.actions;

import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.action.AbstractActionParameter;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ErrorMessageException;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ErrorResponse;
import com.zestedesavoir.zdsnotificateur.member.Member;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Call;
import retrofit.Response;

/**
 * @author Gerard Paligot
 */
@Singleton
public final class CreateMemberAction extends AbstractActionParameter<Member, Void, CreateMemberParameter> {
  private final MemberService service;

  @Inject public CreateMemberAction(MemberService service) {
    this.service = service;
  }

  @Override public void request(final CreateMemberParameter param, final Callback<Member> callback) {
    final Call<Member> newMember = service.create(param.username, param.email, param.password);
    newMember.enqueue(new retrofit.Callback<Member>() {
      @Override public void onResponse(Response<Member> response) {
        if (response.isSuccess()) {
          callback.success(response.body());
        } else {
          try {
            final ErrorMessageException exception = new ErrorMessageException();
            exception.setResponse(new ErrorResponse(response.errorBody().string()));
            callback.failure(exception);
          } catch (IOException e) {
            callback.failure(e);
          }
        }
      }

      @Override public void onFailure(Throwable t) {
        callback.failure(t);
      }
    });
  }
}
