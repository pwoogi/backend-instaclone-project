package com.project.instagramcloneteam5.service;

import com.project.instagramcloneteam5.config.SecurityUtil;
import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.PrivateException;
import com.project.instagramcloneteam5.model.Board;
import com.project.instagramcloneteam5.model.Comment;
import com.project.instagramcloneteam5.model.Commit;
import com.project.instagramcloneteam5.model.Member;
import com.project.instagramcloneteam5.model.dto.CommitRequestDto;
import com.project.instagramcloneteam5.model.dto.CommitResponseDto;
import com.project.instagramcloneteam5.repository.CommentRepository;
import com.project.instagramcloneteam5.repository.CommitRepository;
import com.project.instagramcloneteam5.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CommitService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final CommitRepository commitRepository;

    // 댓글 작성
    @Transactional
    public CommitResponseDto boardCommit(Long commentId, CommitRequestDto commitRequestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new PrivateException(Code.NOT_FOUND_POST));

        System.out.println("로그인한 username : " + SecurityUtil.getCurrentUsername());

        String username = SecurityUtil.getCurrentUsername();

        Member member = memberRepository.findMemberByUsername(username).orElseThrow(
                () -> new PrivateException(Code.NOT_FOUND_MEMBER)
        );
        Commit commit = new Commit(comment, commitRequestDto, member);
        commit = commitRepository.save(commit);
        return new CommitResponseDto(commit);
    }



    // 댓글 삭제
    @Transactional
    public void deleteCommit(Long commitId) {
        Commit commit = commitRepository.findById(commitId).orElseThrow(
                () -> new PrivateException(Code.NOT_FOUND_COMMENT));

        System.out.println("로그인한 username : " + SecurityUtil.getCurrentUsername());

        String username = SecurityUtil.getCurrentUsername();

        Member member = memberRepository.findMemberByUsername(username).orElseThrow(
                () -> new PrivateException(Code.NOT_FOUND_MEMBER)
        );

        // 본인의 댓글만 삭제 가능
        if (!commit.getMember().equals(member)) {
            throw new PrivateException(Code.WRONG_ACCESS_COMMENT_DELETE);
        }
        commitRepository.deleteById(commitId);
    }
}
