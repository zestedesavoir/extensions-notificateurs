package com.zestedesavoir.zdsnotificateur.member;

import com.zestedesavoir.zdsnotificateur.internal.Callback;

import java.util.List;

/**
 * Manage members.
 *
 * @author Gerard Paligot
 */
public interface MemberManager {
  /**
   * Register a new member in Zeste de Savoir. You need an internet connection to use this
   * method. If you don't have one, the failure method will be call on the callback parameter.
   *
   * @param username Username of the new member.
   * @param password Password of the new member.
   * @param email    Email of the new member.
   * @param callback Callback with new member object.
   */
  void register(String username, String password, String email, Callback<Member> callback);

  /**
   * Search a member according to its username. If you don't have an internet connection,
   * the request will be make on the members saved in local. Think that all members loaded
   * by this manager or others, they will be saved in local to get them in offline.
   *
   * @param username Potential username of a member.
   * @param page     Page of the list.
   * @param pageSize Size of the page.
   * @param callback Callback with the list of members.
   */
  void search(String username, int page, int pageSize, Callback<List<Member>> callback);
}
