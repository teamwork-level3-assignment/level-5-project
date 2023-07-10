package com.sparta.lv5assignment.board.controller;

import com.sparta.lv5assignment.board.dto.BoardRequestDto;
import com.sparta.lv5assignment.board.dto.BoardResponseDto;
import com.sparta.lv5assignment.global.dto.Message;
import com.sparta.lv5assignment.global.dto.StatusEnum;
import com.sparta.lv5assignment.global.filter.UserDetailsImpl;
import com.sparta.lv5assignment.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시판 글 생성
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/boards")
    public ResponseEntity<Message<BoardResponseDto>> createBoard(
            @RequestBody BoardRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardResponseDto board = boardService.createBoard(requestDto, userDetails.getUser());
        return ResponseEntity
                .status(StatusEnum.CREATED_DATA.getStatusCode())
                .body(Message.success(StatusEnum.CREATED_DATA.name(), board));
    }

    /**
     * 게시판 글 상세 조회
     *
     * @param id
     * @return
     */
    @GetMapping("/boards/{id}")
    public ResponseEntity<Message<BoardResponseDto>> getBoard(@PathVariable Long id) {
        BoardResponseDto findBoard = boardService.getBoard(id);
        return ResponseEntity
                .status(StatusEnum.SUCCESS.getStatusCode())
                .body(Message.success(StatusEnum.SUCCESS.name(), findBoard));
    }

    /**
     * 게시판 글 리스트
     *
     * @return
     */
    @GetMapping("/boards")
    public ResponseEntity<Message<List<BoardResponseDto>>> getBoardlist() {
        List<BoardResponseDto> boardlist = boardService.getBoardlist();
        return ResponseEntity
                .status(StatusEnum.SUCCESS.getStatusCode())
                .body(Message.success(StatusEnum.SUCCESS.name(), boardlist));
    }

    /**
     * 게시판 글 업데이트
     *
     * @param id
     * @param requestDto
     * @return
     */
    @PutMapping("/boards/{id}")
    public ResponseEntity<Message<BoardResponseDto>> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardResponseDto boardResponseDto = boardService.updateBoard(id, requestDto, userDetails.getUser());
        return ResponseEntity
                .status(StatusEnum.SUCCESS.getStatusCode())
                .body(Message.success(StatusEnum.SUCCESS.name(), boardResponseDto));
    }

    /**
     * 게시판 글 삭제
     *
     * @param id
     * @return
     */
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<Message<String>> deleteBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(id, userDetails.getUser());
        return ResponseEntity
                .status(StatusEnum.DELETE_SUCCESS.getStatusCode())
                .body(Message.success(StatusEnum.DELETE_SUCCESS.name(), StatusEnum.DELETE_SUCCESS.getCode()));
    }
}
