package com.zestedesavoir.android.login;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.zestedesavoir.android.R;
import com.zestedesavoir.android.internal.ui.AbsFragment;
import com.zestedesavoir.android.login.managers.Session;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class LoginFragment extends AbsFragment<LoginContracts.Presenter> implements LoginContracts.View {
    public static Fragment newInstance(Session session) {
        final LoginFragment fragment = new LoginFragment();
        fragment.setPresenter(new LoginPresenter(fragment, session));
        return fragment;
    }

    @BindView(R.id.et_login)
    TextInputEditText etLogin;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;
    @BindView(R.id.btn_connect)
    Button btnConnect;

    public LoginFragment() {
    }

    @Override
    protected int getResLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trySubmit();
                return true;
            }
            return false;
        });
    }

    @OnClick(R.id.btn_connect)
    public void trySubmit() {
        if (!btnConnect.isEnabled()) {
            return;
        }

        etLogin.setError(null);
        etPassword.setError(null);

        btnConnect.setEnabled(false);
        presenter.connect(etLogin.getText().toString().trim(), etPassword.getText().toString().trim());
    }

    @Override
    public void showUsernameError() {
        etLogin.setError(getString(R.string.field_login));
        etLogin.requestFocus();
        btnConnect.setEnabled(true);
    }

    @Override
    public void showPasswordError() {
        etPassword.setError(getString(R.string.field_password));
        etPassword.requestFocus();
        btnConnect.setEnabled(true);
    }

    @Override
    public void showServerError(Throwable throwable) {
        Timber.e(throwable);
        Snackbar.make(getView(), R.string.alert_server_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void authenticated() {
        Snackbar.make(getView(), R.string.alert_success_auth, Snackbar.LENGTH_LONG).show();
    }
}
