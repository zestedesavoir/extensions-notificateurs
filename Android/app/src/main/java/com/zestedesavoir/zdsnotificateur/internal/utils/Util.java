package com.zestedesavoir.zdsnotificateur.internal.utils;

/**
 * @author Gerard Paligot
 */
public final class Util {
  private Util() {
    throw new AssertionError("No instance.");
  }

  public static <T> T checkNotNull(T reference, String format, Object... args) {
    if (reference == null) {
      throw new NullPointerException(String.format(format, args));
    }
    return reference;
  }

  public static void checkArgument(boolean condition, String format, Object... args) {
    if (!condition) {
      throw new IllegalArgumentException(String.format(format, args));
    }
  }
}
