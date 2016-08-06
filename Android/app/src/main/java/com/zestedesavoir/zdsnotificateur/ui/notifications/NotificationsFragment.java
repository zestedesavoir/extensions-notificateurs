package com.zestedesavoir.zdsnotificateur.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zestedesavoir.zdsnotificateur.R;
import com.zestedesavoir.zdsnotificateur.internal.Callback;
import com.zestedesavoir.zdsnotificateur.internal.ZdSLibrary;
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

  @BindView(R.id.rv_notifications) RecyclerView rvNotifications;

  private OnMainNavigation listener;
  private NotificationsAdapter adapter;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    final View inflate = inflater.inflate(R.layout.notifications_fragment, container, false);
    ButterKnife.bind(this, inflate);
    return inflate;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (ab != null) {
      ab.setDisplayHomeAsUpEnabled(false);
      ab.setTitle(getString(R.string.app_name));
    }

    rvNotifications.setHasFixedSize(true);
    rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
    rvNotifications.setAdapter(adapter);

    ZdSLibrary.get(getContext()).getNotificationManager().getAll(new Callback<List<Notification>>() {
      @Override public void success(List<Notification> notifications) {
        adapter.call(notifications);
      }

      @Override public void failure(Throwable e) {
        if (listener != null) {
          listener.goToLoginScreen();
        }
      }
    });
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
}
