package com.project.instagramcloneteam5.controller;

import com.project.instagramcloneteam5.dto.supportdto.BoardDetailsResponseDto;
import com.project.instagramcloneteam5.dto.supportdto.BoardDetailsUpdateRequestDto;
import com.project.instagramcloneteam5.dto.supportdto.BoardRequestDto;
import com.project.instagramcloneteam5.dto.supportdto.BoardUpdateResponseDto;
import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.PrivateException;
import com.project.instagramcloneteam5.response.Response;
import com.project.instagramcloneteam5.service.BoardService;
import com.project.instagramcloneteam5.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final S3Service s3Service;

    // 게시글 전체 조회
    @GetMapping("/boards")
    @ResponseStatus(HttpStatus.OK)

    public Response getAllBoard(){ return Response.success(boardService.getAllBoard());}

    // 메인 페이지 무한 스크롤
    @GetMapping("/boardScroll")
    @ResponseStatus(HttpStatus.OK)

    public Response getBoardSlice(
            @RequestParam(required=false) Integer page,
            @RequestParam(required=false) Integer size,
            @RequestParam(required=false) String sortBy ,
            @RequestParam(required=false) Boolean isAsc
    ) {
        if (isNotNullParam(page, size, sortBy, isAsc)) {
            page -= 1;
            return Response.success(boardService.getAllBoardSlice(page, size, sortBy, isAsc));
        } else {
            throw new PrivateException(Code.PAGING_ERROR);
        }
    }

    private boolean isNotNullParam(Integer page, Integer size, String sortBy, Boolean isAsc) {
        return (page != null) && (size != null) && (sortBy != null) && (isAsc != null);
    }

    // 게시글 상세 조회
    @GetMapping("/boards/details/{boardId}")
    @ResponseStatus(HttpStatus.OK)

    public Response getEachOne(@PathVariable Long boardId) {
        BoardDetailsResponseDto boardDetailsResponseDto= boardService.getEachOne(boardId);
        return Response.success(boardDetailsResponseDto);
    }

    // 게시글 작성
    @PostMapping("/board/write")
    @ResponseStatus(HttpStatus.CREATED)

    public Response uploadBoard(@RequestPart("content") BoardRequestDto boardRequestDto,
                                @RequestPart("imgUrl") List<MultipartFile> multipartFiles) {
        if (multipartFiles == null) {
            throw new PrivateException(Code.WRONG_INPUT_CONTENT);
        }
        List<String> imgPaths = s3Service.upload(multipartFiles);
        System.out.println("IMG 경로들 : " + imgPaths);
        boardService.uploadBoard(boardRequestDto, imgPaths);
        return Response.success();

    }

    // 게시글 수정
    @PutMapping("/board/details/{boardId}")
    @ResponseStatus(HttpStatus.OK)
    public Response updateBoard(@PathVariable Long boardId,@RequestPart("content") BoardDetailsUpdateRequestDto boardDetailsUpdateRequestDto) {
        BoardUpdateResponseDto boardUpdateResponseDto = boardService.updateBoard(boardId, boardDetailsUpdateRequestDto);
        return Response.success(boardUpdateResponseDto);
    }

    // 게시글 삭제
    @DeleteMapping("/board/details/{boardId}")
    public Response deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return Response.success();
    }

    @PostMapping("/board/like/{boardId}")
    public Response heartLikes(
            @PathVariable Long boardId
    ){
        boardService.boardLike(boardId);
        return Response.success();
    }
}