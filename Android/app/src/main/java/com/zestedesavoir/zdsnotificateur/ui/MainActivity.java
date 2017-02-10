package com.zestedesavoir.zdsnotificateur.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.auth.Token;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.ZdSLibrary;
import com.zestedesavoir.zdsnotificateur.services.NotificationService;
import com.zestedesavoir.zdsnotificateur.services.StarterReceiver;
import com.zestedesavoir.zdsnotificateur.ui.auth.LoginActivity;
import com.zestedesavoir.zdsnotificateur.ui.notifications.NotificationsFragment;
import com.zestedesavoir.zdsnotificateur.ui.notifications.OnMainNavigation;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Gerard Paligot
 */
public class MainActivity extends AppCompatActivity implements OnMainNavigation {
  @BindView(R.id.toolbar) Toolbar toolbar;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);

    if (savedInstanceState == null) {
      ZdSLibrary.get(this).getSession().authenticateByToken(new Callback<Token>() {
        @Override public void success(Token token) {
          getSupportFragmentManager().beginTransaction()
              .replace(R.id.content, NotificationsFragment.newInstance())
              .commit();
        }

        @Override public void failure(Throwable e) {
          goToLoginScreen();
        }
      });
    }
  }

  @Override protected void onResume() {
    super.onResume();
    final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    manager.cancel(NotificationService.NOTIFICATION_ID);
  }

  @Override protected void onPause() {
    super.onPause();
    sendBroadcast(StarterReceiver.getStartIntent(getApplicationContext()));
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_disconnect:
        ZdSLibrary.get(this).getSession().disconnect(new Callback<Void>() {
          @Override public void success(Void aVoid) {
            goToLoginScreen();
          }

          @Override public void failure(Throwable e) {
          }
        });

        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void goToLoginScreen() {
    final Intent intent = new Intent(MainActivity.this, LoginActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
  }
}
