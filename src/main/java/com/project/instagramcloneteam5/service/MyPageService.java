package com.project.instagramcloneteam5.service;

import com.project.instagramcloneteam5.dto.auth.MemberDto;
import com.project.instagramcloneteam5.exception.support.MemberNotEqualsException;
import com.project.instagramcloneteam5.exception.support.MemberNotFoundException;
import com.project.instagramcloneteam5.model.Authority;
import com.project.instagramcloneteam5.model.Board;
import com.project.instagramcloneteam5.model.Member;
import com.project.instagramcloneteam5.repository.BoardRepository;
import com.project.instagramcloneteam5.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {


    private final MemberRepository userRepository;

    @Transactional(readOnly = true)
    public List<MemberDto> findAllUsers() {
        List<Member> members = userRepository.findAll();
        List<MemberDto> memberDtos = new ArrayList<>();
        for(Member member : members) {
            memberDtos.add(MemberDto.toDto(member));
        }
        return memberDtos;
    }

    @Transactional(readOnly = true)
    public MemberDto findUser(Long id) {
        return MemberDto.toDto(userRepository.findById(id).orElseThrow(MemberNotFoundException::new));
    }


    @Transactional
    public MemberDto editUserInfo(Long id, MemberDto updateInfo) {
        Member member = userRepository.findById(id).orElseThrow(() -> {
            return new MemberNotFoundException();
        });

        // 권한 처리
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!authentication.getName().equals(member.getUsername())) {
            throw new MemberNotEqualsException();
        } else {
            member.setNickname(updateInfo.getNickname());
            member.setNickname(updateInfo.getNickname());
            return MemberDto.toDto(member);
        }
    }

    @Transactional
    public void deleteUserInfo(Long id) {
        Member member = userRepository.findById(id).orElseThrow(MemberNotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String auth = String.valueOf(authentication.getAuthorities());
        String authByAdmin = "[" + Authority.ROLE_ADMIN +"]";

        if(authentication.getName().equals(member.getUsername()) || auth.equals(authByAdmin)) {
            userRepository.deleteById(id);
        } else {
            throw new MemberNotEqualsException();
        }
    }

}