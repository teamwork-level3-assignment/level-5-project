package com.sparta.lv3assignment.controller;

import com.sparta.lv3assignment.dto.BoardRequestDto;
import com.sparta.lv3assignment.dto.BoardResponseDto;
import com.sparta.lv3assignment.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService, HttpServletRequest req) {
        this.boardService = boardService;
    }

    /**
     * 게시판 글 생성
     * @param requestDto
     * @return
     */
    @PostMapping("/boards")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {
        return boardService.createBoard(requestDto);

    }
    /**
     * 게시판 글 상세 조회
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
     * @param id
     * @param requestDto
     * @return
     */
    @PutMapping("/boards/{id}")
    public Long updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return boardService.updateBoard(id, requestDto);
    }

    /**
     * 게시판 글 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/boards/{id}")
    public Long deleteBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return boardService.deleteBoard(id, requestDto);
    }
}
