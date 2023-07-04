package com.sparta.lv4assignment.controller;

import com.sparta.lv4assignment.dto.BoardRequestDto;
import com.sparta.lv4assignment.dto.BoardResponseDto;
import com.sparta.lv4assignment.entity.Message;
import com.sparta.lv4assignment.entity.StatusEnum;
import com.sparta.lv4assignment.filter.UserDetailsImpl;
import com.sparta.lv4assignment.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 게시판 글 생성
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/boards")
    public ResponseEntity<Message> createBoard(@RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            BoardResponseDto board = boardService.createBoard(requestDto, userDetails.getUser());
            return new ResponseEntity<>(new Message(StatusEnum.OK, StatusEnum.OK.getCode(), board), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Message(StatusEnum.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 게시판 글 상세 조회
     *
     * @param id
     * @return
     */
    @GetMapping("/boards/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    /**
     * 게시판 글 리스트
     *
     * @return
     */
    @GetMapping("/boards")
    public List<BoardResponseDto> getBoardlist() {
        return boardService.getBoardlist();
    }

    /**
     * 게시판 글 업데이트
     *
     * @param id
     * @param requestDto
     * @return
     */
    @PutMapping("/boards/{id}")
    public ResponseEntity<Message> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        try {
            BoardResponseDto boardResponseDto = boardService.updateBoard(id, requestDto, userDetails.getUser());
            return new ResponseEntity<>(new Message(StatusEnum.OK, StatusEnum.OK.getCode(), boardResponseDto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(StatusEnum.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 게시판 글 삭제
     *
     * @param id
     * @return
     */
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<Message> deleteBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            boardService.deleteBoard(id, userDetails.getUser());
            return new ResponseEntity<>(new Message(StatusEnum.OK, StatusEnum.OK.getCode(), null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(StatusEnum.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
