package com.project.instagramcloneteam5.service;

import com.project.instagramcloneteam5.config.SecurityUtil;
import com.project.instagramcloneteam5.dto.dto.*;
import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.PrivateException;
import com.project.instagramcloneteam5.exception.support.BoardNotFoundException;
import com.project.instagramcloneteam5.exception.support.MemberNotFoundException;
import com.project.instagramcloneteam5.model.Board;
import com.project.instagramcloneteam5.model.Comment;
import com.project.instagramcloneteam5.model.Image;
import com.project.instagramcloneteam5.model.Member;
import com.project.instagramcloneteam5.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final CommitRepository commitRepository;
    private final S3Service s3Service;


    // 게시글 전체 조회
    @Transactional
    public Map<String, List<BoardGetResponseDto>> getAllBoard() {
        Map<String, List<BoardGetResponseDto>> listMap = new HashMap<>();
        List<BoardGetResponseDto> list = new ArrayList<>();
        for (Board board : boardRepository.findAllByOrderByCreatedAtDesc()) {
            BoardGetResponseDto main = getBoardOne(board.getId());
            list.add(main);
        }
        listMap.put("BoardInfo", list);
        return listMap;
    }

    // 무한 스크롤
    public Map<String, List<BoardGetResponseDto>> getAllBoardSlice(int page, int size, String sortBy, Boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Slice<Board> sliceBoardList = boardRepository.findAllBy(pageable);

        Map<String, List<BoardGetResponseDto>> listMap = new HashMap<>();
        List<BoardGetResponseDto> list = new ArrayList<>();
        for (Board board : sliceBoardList) {
            BoardGetResponseDto main = getBoardOne(board.getId());
            list.add(main);
        }
        listMap.put("BoardInfo", list);
        return listMap;
    }

    //전체 게시글 전달을 위한 조회용 메서드
    public BoardGetResponseDto getBoardOne(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);

        List<String> imgUrl = imageRepository.findAllByBoard(board)
                .stream()
                .map(Image::getImgUrl)
                .collect(Collectors.toList());
        List<Comment> findCommentByBoard = commentRepository.findAllByBoard(board);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : findCommentByBoard) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }
        return new BoardGetResponseDto(boardId, board, imgUrl, commentResponseDtoList);
    }

    //게시물 상세조회
    public BoardDetailsResponseDto getEachOne(Long boardId){

        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);



        List<String> imgUrl = imageRepository.findAllByBoard(board)
                .stream()
                .map(Image::getImgUrl)
                .collect(Collectors.toList());


        List<Comment> findCommentByBoard = commentRepository.findAllByBoard(board);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : findCommentByBoard) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }
        return new BoardDetailsResponseDto(boardId, board, imgUrl, commentResponseDtoList);

    }

    // 게시글 작성
    @Transactional
    public void uploadBoard(BoardRequestDto res, List<String> imgPaths) {
        postBlankCheck(imgPaths);
        System.out.println("로그인한 username : " + SecurityUtil.getCurrentUsername());

        String username = SecurityUtil.getCurrentUsername();

        Member member = memberRepository.findMemberByUsername(username).orElseThrow(MemberNotFoundException::new);
        String content = res.getContent();

        Board board = new Board(content, member);
        boardRepository.save(board);

        List<String> imageList = new ArrayList<>();
        for (String imgUrl : imgPaths) {
            Image img = new Image(imgUrl, board);
            imageRepository.save(img);
            imageList.add(img.getImgUrl());
        }
    }

    private void postBlankCheck(List<String> imgPaths) {
        if(imgPaths == null || imgPaths.isEmpty()){
            throw new PrivateException(Code.WRONG_INPUT_IMAGE);
        }
    }
    // 게시글 수정
    @Transactional
    public BoardUpdateResponseDto updateBoard(Long boardId, BoardRequestDto boardRequestDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new PrivateException(Code.NOT_FOUND_POST));

        System.out.println("로그인한 username : " + SecurityUtil.getCurrentUsername());

        String username = SecurityUtil.getCurrentUsername();

        Member member = memberRepository.findMemberByUsername(username).orElseThrow(
                () -> new PrivateException(Code.NOT_FOUND_MEMBER)
        );

        // 본인의 게시글만 수정 가능
        if (!board.getMember().equals(member)) {
            throw new PrivateException(Code.WRONG_ACCESS_POST_UPDATE);
        }
        board.updateBoard(boardRequestDto);
        return new BoardUpdateResponseDto(boardId, board);
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new PrivateException(Code.NOT_FOUND_POST)
        );

        System.out.println("로그인한 username : " + SecurityUtil.getCurrentUsername());

        String username = SecurityUtil.getCurrentUsername();

        Member member = memberRepository.findMemberByUsername(username).orElseThrow(
                () -> new PrivateException(Code.NOT_FOUND_MEMBER)
        );

        // 본인의 게시글만 삭제 가능
        //TODO : 필수확인 구조 이상함

        if (!board.getMember().equals(member)) {
            System.out.println("이름1 = " + board.getMember() + "이름2 = " + member);
            throw new PrivateException(Code.WRONG_ACCESS_POST_DELETE);
        }
        commentRepository.deleteAll(board.getCommentList());
        s3Service.delete(board.getImageList());
        boardRepository.delete(board);
    }
}