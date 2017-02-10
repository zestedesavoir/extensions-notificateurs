package com.zestedesavoir.zdsnotificateur.internal.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.zestedesavoir.zdsnotificateur.BuildConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

/**
 * @author Gerard Paligot
 */
@Module(complete = false, library = true)
public final class DatabaseModule {
  @Provides @Singleton SQLiteOpenHelper provideOpenHelper(Context context) {
    return new DatabaseOpenHelper(context);
  }

  @Provides @Singleton BriteDatabase provideSqlBrite(SQLiteOpenHelper openHelper) {
    SqlBrite sqlBrite = SqlBrite.create(new SqlBrite.Logger() {
      @Override public void log(String message) {
        Timber.tag("Database").v(message);
      }
    });

    BriteDatabase db = sqlBrite.wrapDatabaseHelper(openHelper);
    if (BuildConfig.DEBUG) {
      db.setLoggingEnabled(true);
    }

    return db;
  }
}
