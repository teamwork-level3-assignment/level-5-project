package com.sparta.lv3assignment.service;

import com.sparta.lv3assignment.dto.BoardRequestDto;
import com.sparta.lv3assignment.dto.BoardResponseDto;
import com.sparta.lv3assignment.entity.Board;
import com.sparta.lv3assignment.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {

    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * 게시판 글 생성
     * @param requestDto
     * @return
     */
    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        // RequestDto -> Entity
        Board board = new Board(requestDto);
        // DB 저장
        Board saveBoard = boardRepository.save(board);
        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(saveBoard);
        return boardResponseDto;
    }

    /**
     * 게시판 글 상세보기
     * @param id
     * @return
     */
    public BoardResponseDto getBoard(Long id) {
        Board board = findBoard(id);
        return new BoardResponseDto(board);
    }

    /**
     * 게시판 글 리스트
     * @return
     */
    public List<BoardResponseDto> getBoardlist() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    /**
     * 게시판 글 수정
     * @param id
     * @param requestDto
     * @return
     */
    @Transactional
    public Long updateBoard(Long id, BoardRequestDto requestDto) {
        Board board = findBoard(id);
        board.update(requestDto);
        return id;
    }

    /**
     * 게시판 글 삭제
     * @param id
     * @param requestDto
     * @return
     */
    public Long deleteBoard(Long id, BoardRequestDto requestDto) {
        Board board = findBoard(id);
        boardRepository.delete(board);
        return id;
    }

    private Board findBoard(Long id){
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 글이 존재하지 않습니다.")
        );
    }
}
