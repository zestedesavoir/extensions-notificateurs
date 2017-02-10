package com.zestedesavoir.android.internal.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class AbsFragment<T extends BasePresenter> extends Fragment implements BaseView<T> {
    protected T presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflate = inflater.inflate(getResLayout(), container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @LayoutRes
    protected abstract int getResLayout();

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.subscribe();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.unsubscribe();
        }
    }

    @Override
    public void setPresenter(T presenter) {
        this.presenter = presenter;
    }
}
