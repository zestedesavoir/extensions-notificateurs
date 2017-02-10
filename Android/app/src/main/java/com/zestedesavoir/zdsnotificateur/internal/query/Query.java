package com.zestedesavoir.zdsnotificateur.internal.query;

import com.zestedesavoir.zdsnotificateur.internal.Callback;

/**
 * Create a query without any parameter from the user of your application.
 *
 * @author Gerard Paligot
 */
public interface Query<T> {
  void execute(Callback<T> callback);
}
