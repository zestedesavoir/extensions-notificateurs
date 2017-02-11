package com.zestedesavoir.android.notification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.zestedesavoir.android.OnNavigationListener;
import com.zestedesavoir.android.R;
import com.zestedesavoir.android.internal.ui.AbsFragment;
import com.zestedesavoir.android.internal.ui.EndlessRecyclerViewScrollListener;
import com.zestedesavoir.android.notification.managers.NotificationManager;
import com.zestedesavoir.android.notification.models.Notification;

import java.util.List;

import butterknife.BindView;

public class NotificationsFragment extends AbsFragment<NotificationsContracts.Presenter> implements NotificationsContracts.View {
    public static Fragment newInstance(NotificationManager manager) {
        final NotificationsFragment fragment = new NotificationsFragment();
        fragment.setPresenter(new NotificationsPresenter(fragment, manager));
        return fragment;
    }

    @BindView(R.id.progress)
    ProgressBar pbLoading;
    @BindView(R.id.rv_notifications)
    RecyclerView rvNotifications;

    private NotificationsAdapter adapter;
    private OnNavigationListener listener;

    @Override
    protected int getResLayout() {
        return R.layout.fragment_notifications;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new NotificationsAdapter(context);
        try {
            listener = (OnNavigationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement " + OnNavigationListener.class.getName());
        }
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.getNotifications(1);
    }

    @Override
    public void updateNotifications(List<Notification> notifications) {
        adapter.updateNotifications(notifications);
    }

    @Override
    public void showError(Throwable throwable) {
        if (listener != null) {
            listener.goToLoginScreen();
        }
    }

    @Override
    public void showProgress(final boolean show) {
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
