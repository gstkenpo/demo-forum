package com.forum.dao;

import com.forum.dao.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
    public Member findByEmail(String email);
    public Member findByUserName(String userName);
}