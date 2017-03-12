package com.zestedesavoir.android.internal.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zestedesavoir.android.OnNavigationListener;

import butterknife.ButterKnife;

public abstract class AbsFragment<T extends BasePresenter> extends Fragment implements BaseView<T> {
    protected T presenter;
    protected OnNavigationListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnNavigationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement " + OnNavigationListener.class.getName());
        }
    }

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
