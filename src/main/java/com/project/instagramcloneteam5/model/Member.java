package com.project.instagramcloneteam5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;


    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private String kakaoEmail;


    @Builder
    public Member(String username, String password,String nickname, Authority authority) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.authority = authority;
    }

    //kakao
    @Builder
    public Member(String username, String encodePassword, String kakaoNickname, String kakaoEmail) {
        this.username = username;
        this.password = encodePassword;
        this.nickname = kakaoNickname;
        this.kakaoId = null;

    }

    @Builder
    public Member(String username, String encodePassword, String kakaoEmail, Long kakaoId) {
        this.username = username;
        this.password = encodePassword;
        this.kakaoEmail = kakaoEmail;
        this.kakaoId = kakaoId;

    }

    //새로만들어진 entity들이 아직 영속화가 되지 않았더라면 각각 다른 값으로 취급하겠다라는 뜻
    //Entity를 데이터베이스에 연결시켜서 영속화시키는 환경에서 서로 다른 두 row(entity)가 같은 조건이 무엇인가?
    //라는 대답을 하는  equals & hascode 절차
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return id != null && id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
