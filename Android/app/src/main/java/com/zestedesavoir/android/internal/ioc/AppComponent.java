package com.zestedesavoir.android.internal.ioc;

import com.zestedesavoir.android.MainActivity;
import com.zestedesavoir.android.login.LoginManagerModule;
import com.zestedesavoir.android.notification.NotificationsManagerModule;
import com.zestedesavoir.android.notification.daos.StateNotificationModule;
import com.zestedesavoir.android.notification.services.NotificationService;
import com.zestedesavoir.android.notification.services.OperationNotificationReceiver;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class, NetworkModule.class, LoginManagerModule.class,
                NotificationsManagerModule.class, DatabaseModule.class,
                StateNotificationModule.class
        }
)
public interface AppComponent {
    void inject(MainActivity activity);

    void inject(NotificationService service);

    void inject(OperationNotificationReceiver receiver);
}
