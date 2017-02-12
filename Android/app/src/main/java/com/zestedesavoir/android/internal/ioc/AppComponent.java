package com.zestedesavoir.android.internal.ioc;

import com.zestedesavoir.android.MainActivity;
import com.zestedesavoir.android.login.LoginManagerModule;
import com.zestedesavoir.android.notification.NotificationsManagerModule;
import com.zestedesavoir.android.notification.services.NotificationService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class, NetworkModule.class, LoginManagerModule.class, NotificationsManagerModule.class
        }
)
public interface AppComponent {
    void inject(MainActivity activity);

    void inject(NotificationService service);
}
