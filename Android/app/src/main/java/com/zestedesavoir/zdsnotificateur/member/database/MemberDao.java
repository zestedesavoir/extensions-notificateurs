package com.zestedesavoir.zdsnotificateur.member.database;

import com.zestedesavoir.zdsnotificateur.internal.database.Dao;
import com.zestedesavoir.zdsnotificateur.member.Member;

import java.util.List;

/**
 * @author Gerard Paligot
 */
public interface MemberDao extends Dao<Member> {
  List<Member> search(String username);
}
