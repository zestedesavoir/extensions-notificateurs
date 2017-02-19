package com.zestedesavoir.android.internal.ioc;

import android.content.Context;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.zestedesavoir.android.BuildConfig;
import com.zestedesavoir.android.internal.db.DatabaseOpenHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Module
public class DatabaseModule {
    @Provides
    @Singleton
    BriteDatabase provideBriteDatabase(Context context) {
        final DatabaseOpenHelper openHelper = new DatabaseOpenHelper(context);
        final SqlBrite sqlBrite = new SqlBrite.Builder()
                .logger(message -> Timber.tag("Database").v(message))
                .build();
        final BriteDatabase database = sqlBrite.wrapDatabaseHelper(openHelper, Schedulers.io());
        database.setLoggingEnabled(BuildConfig.DEBUG);
        return database;
    }
}