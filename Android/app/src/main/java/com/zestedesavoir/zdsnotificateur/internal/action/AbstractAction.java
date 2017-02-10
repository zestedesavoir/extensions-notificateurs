package com.zestedesavoir.zdsnotificateur.internal.action;

import com.zestedesavoir.zdsnotificateur.internal.Callback;

/**
 * Create an action without any parameter from the user of your application.
 *
 * @author Gerard Paligot
 */
public abstract class AbstractAction<T> {
  private T cache;

  public void execute(String token, Callback<T> callback) {
    final T resource = cache;
    if (resource != null) {
      // We have a cached value. Emit it immediately.
      callback.success(resource);
      return;
    }

    if (token == null || token.isEmpty()) {
      request(callback);
    } else {
      requestAuthenticated(token, callback);
    }
  }

  protected final class CacheCallback implements Callback<T> {
    private final Callback<T> next;

    public CacheCallback(Callback<T> next) {
      this.next = next;
    }

    @Override public void success(T resource) {
      cache = resource;
      next.success(resource);
    }

    @Override public void failure(Throwable e) {
      next.failure(e);
    }
  }

  protected abstract void request(Callback<T> callback);

  protected void requestAuthenticated(String token, Callback<T> callback) {
    request(callback);
  }
}
