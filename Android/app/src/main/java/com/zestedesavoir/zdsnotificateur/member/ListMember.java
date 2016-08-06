package com.zestedesavoir.zdsnotificateur.member;

import java.util.List;

/**
 * @author Gerard Paligot
 */
public final class ListMember {
  public final int count;
  public final String next;
  public final String previous;
  public final List<Member> results;

  public ListMember(int count, String next, String previous, List<Member> results) {
    this.count = count;
    this.next = next;
    this.previous = previous;
    this.results = results;
  }

  public int getCount() {
    return count;
  }

  public String getNext() {
    return next;
  }

  public String getPrevious() {
    return previous;
  }

  public List<Member> getResults() {
    return results;
  }

  @Override public String toString() {
    return "ListMember{" +
        "count=" + count +
        ", next='" + next + '\'' +
        ", previous='" + previous + '\'' +
        ", results=" + results +
        '}';
  }
}
