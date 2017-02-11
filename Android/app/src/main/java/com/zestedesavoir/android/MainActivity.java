package com.zestedesavoir.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zestedesavoir.android.login.LoginFragment;
import com.zestedesavoir.android.login.managers.Session;
import com.zestedesavoir.android.notification.NotificationsFragment;
import com.zestedesavoir.android.notification.managers.NotificationManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements OnNavigationListener {
    @Inject
    Session session;

    @Inject
    NotificationManager notificationManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @NonNull
    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        ((ZdSApplication) getApplicationContext()).getAppComponent().inject(this);
        setSupportActionBar(toolbar);

        mCompositeSubscription.add(session.isAuthenticated()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (!aBoolean) {
                        goToLoginScreen();
                    } else {
                        goToNotificationScreen();
                    }
                })
        );
    }

    @Override
    public void goToLoginScreen() {
        toolbar.setVisibility(View.GONE);
        goTo(LoginFragment.newInstance(session));
    }

    @Override
    public void goToNotificationScreen() {
        toolbar.setVisibility(View.VISIBLE);
        goTo(NotificationsFragment.newInstance(notificationManager));
    }

    private void goTo(Fragment fragment) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.commit();
    }
}
