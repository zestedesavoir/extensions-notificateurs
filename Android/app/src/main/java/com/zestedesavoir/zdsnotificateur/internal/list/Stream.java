package com.zestedesavoir.zdsnotificateur.internal.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Gerard Paligot
 */
public class Stream<T> {
  public static <T> Stream<T> of(T... list) {
    return new Stream<T>(list);
  }

  public static <T> Stream<T> of(List<T> list) {
    return new Stream<T>(list);
  }

  private final List<T> list;

  public Stream(T[] list) {
    this.list = Arrays.asList(list);
  }

  public Stream(List<T> list) {
    this.list = list;
  }

  public Stream<T> filter(final Predicate<? super T> predicate) {
    final List<T> newList = new ArrayList<>();
    for (T item : list) {
      if (predicate.test(item)) {
        newList.add(item);
      }
    }
    list.clear();
    list.addAll(newList);
    return this;
  }

  public <R> Stream<R> map(final Function<? super T, ? extends R> mapper) {
    final List<R> newList = new ArrayList<>();
    for (T item : list) {
      newList.add(mapper.apply(item));
    }
    return new Stream<R>(newList);
  }

  public Stream<T> sorted(final Comparator<? super T> comparator) {
    Collections.sort(list, comparator);
    return this;
  }

  public List<T> toList() {
    return list;
  }
}
