package com.zestedesavoir.zdsnotificateur.internal;

/**
 * This callback is used to return all responses in managers. Think to place a value in the
 * generic type <code>T</code> to get the correct object in the <code>success</code> method.
 *
 * @author Gerard Paligot
 */
public interface Callback<T> {
  /**
   * The system can satisfy your request, you'll get your object in this success method.
   *
   * @param t Expected object.
   */
  void success(T t);

  /**
   * The system can't satisfy your request for the reason X or Y, think to analyse the
   * type of the exception and its message to know more about the error.
   *
   * @param e Exception occurred during the request.
   */
  void failure(Throwable e);
}
