package com.zestedesavoir.zdsnotificateur.internal.action;

import com.zestedesavoir.zdsnotificateur.internal.Callback;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Create an action with parameter. See {@link Parameter} to see the interface marker.
 *
 * @author Gerard Paligot
 */
public abstract class AbstractActionParameter<T, V, K extends Parameter<V>> {
  private final Map<V, T> cache = new LinkedHashMap<>();

  public void execute(final String token, final K param, final Callback<T> callback) {
    final T resource = cache.get(param.getKey());
    if (resource != null) {
      // We have a cached value. Emit it immediately.
      callback.success(resource);
      return;
    }

    if (token == null || token.isEmpty()) {
      request(param, callback);
    } else {
      requestAuthenticated(token, param, callback);
    }
  }

  protected final class CacheCallback implements Callback<T> {
    private final Callback<T> next;
    private final K param;

    public CacheCallback(Callback<T> next, K param) {
      this.next = next;
      this.param = param;
    }

    @Override public void success(T resource) {
      cache.put(param.getKey(), resource);
      next.success(resource);
    }

    @Override public void failure(Throwable e) {
      next.failure(e);
    }
  }

  protected abstract void request(final K param, final Callback<T> callback);

  protected void requestAuthenticated(final String token, final K param, final Callback<T> callback) {
    request(param, callback);
  }
}
