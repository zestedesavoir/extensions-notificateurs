package com.zestedesavoir.zdsnotificateur.internal.query;

import com.zestedesavoir.zdsnotificateur.internal.Callback;

/**
 * Create a query with parameter. See {@link Parameter} to see the interface marker.
 *
 * @author Gerard Paligot
 */
public interface QueryParameter<T, E extends Parameter> {
  void execute(E parameterQuery, Callback<T> callback);
}
