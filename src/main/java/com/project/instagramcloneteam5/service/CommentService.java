package com.project.instagramcloneteam5.service;

import com.project.instagramcloneteam5.config.SecurityUtil;
import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.PrivateException;
import com.project.instagramcloneteam5.exception.support.BoardNotFoundException;
import com.project.instagramcloneteam5.exception.support.CommentNotFoundException;
import com.project.instagramcloneteam5.model.*;
import com.project.instagramcloneteam5.model.dto.*;
import com.project.instagramcloneteam5.repository.BoardRepository;

import com.project.instagramcloneteam5.repository.CommentRepository;
import com.project.instagramcloneteam5.repository.CommitRepository;
import com.project.instagramcloneteam5.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    private final CommitRepository commitRepository;

    // 댓글 작성
    @Transactional
    public CommentResponseDto boardComment(Long boardId, CommentRequestDto commentRequestDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new PrivateException(Code.NOT_FOUND_POST));

        System.out.println("로그인한 username : " + SecurityUtil.getCurrentUsername());

        String username = SecurityUtil.getCurrentUsername();

        Member member = memberRepository.findMemberByUsername(username).orElseThrow(
                () -> new PrivateException(Code.NOT_FOUND_MEMBER)
        );
        Comment comment = new Comment(board, commentRequestDto, member);
        comment = commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }
    //대댓글 불러오기

    public CommentDetailsResponse getEachCommit(Long commentId){

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        List<Commit> findCommitByBoard = commitRepository.findAllByComment(comment);


        List<CommitResponseDto> commitList = new ArrayList<>();
        for (Commit commit : findCommitByBoard) {
            commitList.add(new CommitResponseDto(commit));
        }


        //TODO: 이거 잘 못 되어있어서 8/24일 오전에 일어나면 바로 대댓글 작업부터해야함
        return new CommentDetailsResponse(commentId,commitList);

    }



    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new PrivateException(Code.NOT_FOUND_COMMENT));

        System.out.println("로그인한 username : " + SecurityUtil.getCurrentUsername());

        String username = SecurityUtil.getCurrentUsername();

        Member member = memberRepository.findMemberByUsername(username).orElseThrow(
                () -> new PrivateException(Code.NOT_FOUND_MEMBER)
        );

        // 본인의 댓글만 삭제 가능
        if (!comment.getMember().equals(member)) {
            throw new PrivateException(Code.WRONG_ACCESS_COMMENT_DELETE);
        }
        commentRepository.deleteById(commentId);
    }
}