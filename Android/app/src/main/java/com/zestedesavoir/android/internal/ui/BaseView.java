package com.zestedesavoir.android.internal.ui;

public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
}
