package com.zestedesavoir.zdsnotificateur.internal.action;

/**
 * This interface is used as a marker and declare the strategy for the key used by the cache.
 * If you create an action parametrized (see {@link AbstractActionParameter}), think to use
 * this interface and specify a correct strategy for the cache.
 *
 * @author Gerard Paligot
 */
public interface Parameter<T> {
  T getKey();
}
