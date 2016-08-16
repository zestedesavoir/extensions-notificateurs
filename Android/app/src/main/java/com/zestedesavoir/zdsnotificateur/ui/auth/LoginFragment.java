package com.zestedesavoir.zdsnotificateur.ui.auth;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.auth.Token;
import com.zestedesavoir.zdsnotificateur.internal.ZdSLibrary;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Gerard Paligot
 */
public class LoginFragment extends Fragment {

  public static LoginFragment newInstance() {
    return new LoginFragment();
  }

  private OnLoginNavigation listener;

  @BindView(R.id.et_login) TextInputEditText etLogin;
  @BindView(R.id.et_password) TextInputEditText etPassword;
  @BindView(R.id.btn_connect) Button btnConnect;
  @BindView(R.id.btn_register) Button btnRegister;

  public LoginFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View inflate = inflater.inflate(R.layout.fragment_login, container, false);
    ButterKnife.bind(this, inflate);
    return inflate;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == EditorInfo.IME_ACTION_DONE) {
          trySubmit();
          return true;
        }
        return false;
      }
    });
    btnConnect.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        trySubmit();
      }
    });
    btnRegister.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (listener != null) {
          listener.goToRegisterScreen();
        }
      }
    });
  }

  private void trySubmit() {
    if (!btnConnect.isEnabled()) {
      return;
    }

    etLogin.setError(null);
    etPassword.setError(null);

    View focusView = null;

    final String username = etLogin.getText().toString().trim();
    final String password = etPassword.getText().toString().trim();

    if (TextUtils.isEmpty(username)) {
      etLogin.setError(getString(R.string.field_login));
      focusView = etLogin;
    } else if (TextUtils.isEmpty(password)) {
      etPassword.setError(getString(R.string.field_password));
      focusView = etPassword;
    }

    if (focusView != null) {
      focusView.requestFocus();
    } else {
      btnConnect.setEnabled(false);
      ZdSLibrary.get(getContext()).getSession().authenticate(username, password, new FormCallback<Token>(btnConnect) {
        @Override public void success(Token token) {
          super.success(token);
          if (listener != null) {
            listener.goToNotificationsScreen();
          }
        }

        @Override public void failure(Throwable e) {
          super.failure(e);
          Snackbar.make(btnConnect, e.getCause().getMessage(), Snackbar.LENGTH_LONG).show();
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
