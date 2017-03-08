package com.zestedesavoir.android.notification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.zestedesavoir.android.R;
import com.zestedesavoir.android.internal.exceptions.RetrofitException;
import com.zestedesavoir.android.internal.ui.AbsFragment;
import com.zestedesavoir.android.internal.ui.EndlessRecyclerViewScrollListener;
import com.zestedesavoir.android.notification.managers.NotificationsManager;
import com.zestedesavoir.android.notification.models.Notification;

import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

public class NotificationsFragment extends AbsFragment<NotificationsContracts.Presenter> implements NotificationsContracts.View {
    public static final String TAG = "NotificationsFragment";

    public static Fragment newInstance(NotificationsManager manager) {
        final NotificationsFragment fragment = new NotificationsFragment();
        fragment.setPresenter(new NotificationsPresenter(fragment, manager));
        return fragment;
    }

    @BindView(R.id.progress)
    ProgressBar pbLoading;
    @BindView(R.id.rv_notifications)
    RecyclerView rvNotifications;
    @BindView(R.id.srl_notifications)
    SwipeRefreshLayout srlNotifications;

    private NotificationsAdapter adapter;

    @Override
    protected int getResLayout() {
        return R.layout.fragment_notifications;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        adapter = new NotificationsAdapter(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(false);
            ab.setTitle(getString(R.string.app_name));
        }

        rvNotifications.setHasFixedSize(true);
        rvNotifications.setAdapter(adapter);
        rvNotifications.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) rvNotifications.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.getNotifications(page);
            }
        });

        srlNotifications.setOnRefreshListener(() -> presenter.getNotifications(1));
        srlNotifications.setColorSchemeResources(R.color.accent);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getNotifications(1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                listener.goToSettingsScreen();
                return true;
        }
        return false;
    }

    @Override
    public void addAllNotifications(List<Notification> notifications) {
        adapter.addAll(notifications);
    }

    @Override
    public void updateNotifications(List<Notification> notifications) {
        adapter.update(notifications);
    }

    @Override
    public void showError(RetrofitException throwable) {
        if (throwable.getKind() == RetrofitException.Kind.NO_TOKEN) {
            return;
        }
        if (throwable.getKind() == RetrofitException.Kind.HTTP && throwable.getResponse().code() == 404) {
            // ignore, 404 is returned when we are at the last page.
            return;
        }
        if (listener != null && throwable.getKind() == RetrofitException.Kind.HTTP && throwable.getResponse().code() == 401) {
            listener.goToLoginScreen();
        } else if (throwable.getKind() == RetrofitException.Kind.NETWORK) {
            Timber.e(throwable);
            Snackbar.make(getView(), R.string.alert_network_error, Snackbar.LENGTH_LONG).show();
        } else {
            Timber.e(throwable);
            Snackbar.make(getView(), R.string.alert_server_error, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showProgress(final boolean show) {
        srlNotifications.setRefreshing(show);
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            rvNotifications.setVisibility(show ? View.GONE : View.VISIBLE);
            rvNotifications.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rvNotifications.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
            pbLoading.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
            rvNotifications.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
