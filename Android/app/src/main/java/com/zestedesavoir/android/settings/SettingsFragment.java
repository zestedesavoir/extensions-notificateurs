package com.zestedesavoir.android.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.MenuItem;

import com.zestedesavoir.android.BuildConfig;
import com.zestedesavoir.android.OnNavigationListener;
import com.zestedesavoir.android.R;
import com.zestedesavoir.android.login.managers.Session;

import de.psdev.licensesdialog.LicensesDialog;

public class SettingsFragment extends PreferenceFragmentCompat implements SettingsContracts.View {
    public static final String TAG = "SettingsFragment";

    public static Fragment newInstance(Session session) {
        final SettingsFragment fragment = new SettingsFragment();
        fragment.setPresenter(new SettingsPresenter(fragment, session));
        return fragment;
    }

    private SettingsContracts.Presenter presenter;

    protected OnNavigationListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        try {
            listener = (OnNavigationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement " + OnNavigationListener.class.getName());
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        findPreference("pref_licenses").setOnPreferenceClickListener(preference -> {
            new LicensesDialog.Builder(getContext())
                    .setNotices(R.raw.notices)
                    .setTitle(R.string.settings_notice_title)
                    .setCloseText(R.string.settings_notice_close)
                    .build()
                    .showAppCompat();
            return true;
        });
        findPreference("pref_disconnect").setOnPreferenceClickListener(preference -> {
            presenter.disconnect();
            return true;
        });
        findPreference("pref_version").setSummary(BuildConfig.VERSION_NAME);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(getString(R.string.menu_settings));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                listener.back();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(SettingsContracts.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDisconnected() {
        listener.back();
        listener.goToLoginScreen();
    }
}
