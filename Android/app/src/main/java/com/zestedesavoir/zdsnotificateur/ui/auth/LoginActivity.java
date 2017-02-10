package com.zestedesavoir.zdsnotificateur.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.auth.Token;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.ZdSLibrary;
import com.zestedesavoir.zdsnotificateur.ui.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Gerard Paligot
 */
public class LoginActivity extends AppCompatActivity implements OnLoginNavigation {

  @BindView(R.id.content) FrameLayout flContent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);

    if (savedInstanceState == null) {
      ZdSLibrary.get(this).getSession().authenticateByToken(new Callback<Token>() {
        @Override public void success(Token token) {
          goToNotificationsScreen();
        }

        @Override public void failure(Throwable e) {
          getSupportFragmentManager().beginTransaction()
              .replace(R.id.content, LoginFragment.newInstance())
              .commit();
        }
      });
    }
  }

  @Override public void goToLoginScreen() {
    getSupportFragmentManager().popBackStack();
    Snackbar.make(flContent, R.string.alert_register, Snackbar.LENGTH_SHORT).show();
  }

  @Override public void goToRegisterScreen() {
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.content, RegisterFragment.newInstance())
        .addToBackStack(null)
        .commit();
  }

  @Override public void goToNotificationsScreen() {
    final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
  }
}
