package com.project.instagramcloneteam5.repository;


import com.project.instagramcloneteam5.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsernameAndPassword(String username, String password);
    Optional<Member> findByUsername(String username);
//    Optional<Member> findById(Long id);
    boolean existsByUsername(String username);

    Optional<Member> findByKakaoEmail(String kakaoEmail);
    Optional<Member> findMemberByUsername(String username);

    Optional<Member> findByKakaoId(Long kakaoId);
}