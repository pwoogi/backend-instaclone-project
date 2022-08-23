package com.project.instagramcloneteam5.service;

import com.project.instagramcloneteam5.dto.auth.MemberDto;
import com.project.instagramcloneteam5.model.Member;
import com.project.instagramcloneteam5.repository.BoardRepository;
import com.project.instagramcloneteam5.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestMemberService {


    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;


    @Transactional(readOnly = true)
    public List<MemberDto> findAllUsers() {
        List<Member> members = memberRepository.findAll();
        List<MemberDto> memberDtos = new ArrayList<>();
        for (Member member : members) {
            memberDtos.add(MemberDto.toDto(member));
        }
        return memberDtos;
    }
}