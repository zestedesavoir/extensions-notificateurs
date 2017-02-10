package com.zestedesavoir.zdsnotificateur.ui.auth;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.internal.ZdSLibrary;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ErrorMessageException;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.ErrorResponse;
import com.zestedesavoir.zdsnotificateur.member.Member;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * @author Gerard Paligot
 */
public class RegisterFragment extends Fragment {

  public static RegisterFragment newInstance() {
    return new RegisterFragment();
  }

  private OnLoginNavigation listener;

  @BindView(R.id.ll_register) LinearLayout llRegister;
  @BindView(R.id.et_login) TextInputEditText etLogin;
  @BindView(R.id.et_password) TextInputEditText etPassword;
  @BindView(R.id.et_email) TextInputEditText etEmail;
  @BindView(R.id.btn_register) Button btnRegister;

  public RegisterFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View inflate = inflater.inflate(R.layout.fragment_register, container, false);
    ButterKnife.bind(this, inflate);
    return inflate;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    etPassword.setOnEditorActionListener((textView, id, keyEvent) -> {
      if (id == EditorInfo.IME_ACTION_DONE) {
        trySubmit();
        return true;
      }
      return false;
    });
    btnRegister.setOnClickListener(v -> trySubmit());
  }

  private void trySubmit() {
    if (!btnRegister.isEnabled()) {
      return;
    }

    etLogin.setError(null);
    etPassword.setError(null);
    etEmail.setError(null);

    View focusView = null;

    final String username = etLogin.getText().toString().trim();
    final String password = etPassword.getText().toString().trim();
    final String email = etEmail.getText().toString().trim();

    if (TextUtils.isEmpty(username)) {
      etLogin.setError(getString(R.string.field_login));
      focusView = etLogin;
    } else if (TextUtils.isEmpty(password)) {
      etPassword.setError(getString(R.string.field_password));
      focusView = etPassword;
    } else if (TextUtils.isEmpty(email)) {
      etEmail.setError(getString(R.string.field_email));
      focusView = etEmail;
    }

    if (focusView != null) {
      focusView.requestFocus();
    } else {
      btnRegister.setEnabled(false);
      ZdSLibrary.get(getContext()).getMemberManager().register(username, password, email, new FormCallback<Member>(btnRegister) {
        @Override public void success(Member member) {
          super.success(member);
          listener.goToLoginScreen();
        }

        @Override public void failure(Throwable e) {
          super.failure(e);
          if (e instanceof ErrorMessageException) {
            final ErrorResponse response = ((ErrorMessageException) e).getResponse();
            etEmail.setError(response.get("email"));
            etLogin.setError(response.get("username"));
            etPassword.setError(response.get("password"));
          } else {
            Timber.tag("Register").e(e, "Error during the registration.");
          }
        }
      });
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      listener = (OnLoginNavigation) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement " + OnLoginNavigation.class.getName());
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    listener = null;
  }
}
