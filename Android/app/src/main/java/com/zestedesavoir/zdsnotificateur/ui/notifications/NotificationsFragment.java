package com.zestedesavoir.zdsnotificateur.ui.notifications;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.ZdSLibrary;
import com.zestedesavoir.zdsnotificateur.internal.exceptions.AuthenticationException;
import com.zestedesavoir.zdsnotificateur.notifications.Notification;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Gerard Paligot
 */
public class NotificationsFragment extends Fragment {
  public static Fragment newInstance() {
    return new NotificationsFragment();
  }

  @BindView(R.id.container) FrameLayout flContainer;
  @BindView(R.id.progress) ProgressBar pbLoading;
  @BindView(R.id.rv_notifications) RecyclerView rvNotifications;

  private OnMainNavigation listener;
  private NotificationsAdapter adapter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    final View inflate = inflater.inflate(R.layout.notifications_fragment, container, false);
    ButterKnife.bind(this, inflate);
    return inflate;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    showProgress(true);

    final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (ab != null) {
      ab.setDisplayHomeAsUpEnabled(false);
      ab.setTitle(getString(R.string.app_name));
    }

    rvNotifications.setHasFixedSize(true);
    rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
    rvNotifications.setAdapter(adapter);
  }

  @Override public void onResume() {
    super.onResume();
    ZdSLibrary.get(getContext()).getNotificationManager().getAll(new Callback<List<Notification>>() {
      @Override public void success(List<Notification> notifications) {
        adapter.filter(Type.NONE, notifications);
        showProgress(false);
      }

      @Override public void failure(Throwable e) {
        if (e instanceof AuthenticationException) {
          if (listener != null) {
            listener.goToLoginScreen();
          }
        } else {
          showProgress(false);
          Snackbar.make(flContainer, R.string.alert_server_error, Snackbar.LENGTH_LONG).show();
        }
      }
    });
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_topic:
        adapter.filter(Type.TOPIC);
        return true;
      case R.id.action_post:
        adapter.filter(Type.POST);
        return true;
      case R.id.action_private_post:
        adapter.filter(Type.PRIVATE_POST);
        return true;
      case R.id.action_reaction:
        adapter.filter(Type.REACTION);
        return true;
      case R.id.action_clear:
        adapter.filter(Type.NONE);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    adapter = new NotificationsAdapter(context);
    try {
      listener = (OnMainNavigation) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement " + OnMainNavigation.class.getName());
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    listener = null;
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) protected void showProgress(final boolean show) {
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
