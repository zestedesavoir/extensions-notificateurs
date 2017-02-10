package com.zestedesavoir.android.internal.ioc;

import com.zestedesavoir.android.MainActivity;
import com.zestedesavoir.android.login.LoginManagerModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class, NetworkModule.class, LoginManagerModule.class
        }
)
public interface AppComponent {
    void inject(MainActivity activity);
}
