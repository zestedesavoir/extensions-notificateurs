package com.zestedesavoir.zdsnotificateur.ui.auth;

import android.view.View;

import com.zestedesavoir.zdsnotificateur.internal.Callback;

/**
 * @author Gerard Paligot
 */
public class FormCallback<T> implements Callback<T> {
  private View btnView;

  public FormCallback(View btnView) {
    this.btnView = btnView;
  }

  @Override public void success(T t) {
    btnView.setEnabled(true);
  }

  @Override public void failure(Throwable e) {
    btnView.setEnabled(true);
  }
}
