package com.sparta.lv5assignment.board.service;

import com.sparta.lv5assignment.board.dto.BoardRequestDto;
import com.sparta.lv5assignment.board.dto.BoardResponseDto;
import com.sparta.lv5assignment.board.entity.Board;
import com.sparta.lv5assignment.category.entity.Category;
import com.sparta.lv5assignment.category.entity.CategoryBoard;
import com.sparta.lv5assignment.category.repository.CategoryBoardRepository;
import com.sparta.lv5assignment.category.repository.CategoryRepository;
import com.sparta.lv5assignment.board.repository.BoardRepository;
import com.sparta.lv5assignment.global.dto.StatusEnum;
import com.sparta.lv5assignment.global.exception.CustomException;
import com.sparta.lv5assignment.user.repository.UserRepository;
import com.sparta.lv5assignment.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryBoardRepository categoryBoardRepository;

    /**
     * 게시판 글 생성
     *
     * @param requestDto
     * @param user
     * @return
     */
    @Transactional  /* ✨✨✨✨✨✨ throw가 터지면 전부 롤백됨. */
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {

        String username = user.getUsername();

        // 유저조회 -> board 를 생성할때 누가 생성했는지 알아내기 위해
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(StatusEnum.NOT_FOUND_USER));

        // 등록하고 싶은 카테고리 가지고 오기
        // 저장하기 : 1. 연관관계의 주인에서 저장을 하던가,   <- 현재는 이 방법을 택함
        //           2. 아니면 양방향 설정하고 addXxx()라는 별도의 메서드로 그 안에서 연관관계 설정을 하던가
        List<String> categoryNames = requestDto.getCategoryNames();

        // RequestDto -> Entity
        Board board = new Board(requestDto, findUser);

        // DB 저장
        Board saveBoard = boardRepository.save(board);

        for (String categoryName : categoryNames) {
            Category category = categoryRepository.findByCategoryName(categoryName)
                    .orElseThrow(() -> new CustomException(StatusEnum.NOT_FOUND_CATEGORY));
            // DB 저장
            categoryBoardRepository.save(new CategoryBoard(saveBoard, category));

        }

        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(saveBoard);
        return boardResponseDto;
    }

    /**
     * 게시판 글 상세보기
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public BoardResponseDto getBoard(Long id) {
        log.info("특정게시글 조회 쿼리");
        // 특정 게시글과 그와 관련된 댓글 가지고 오기
        Board board = findBoard(id);

        log.info("LAZY 옵션주엇을 때 날라가는 쿼리");
        return new BoardResponseDto(board);
    }

    /**
     * 게시판 글 리스트
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoardlist() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    /**
     * 게시판 글 수정
     *
     * @param id
     * @param requestDto
     * @param user
     * @return
     */
    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, User user) {
        String username = user.getUsername();

        // 유저조회 -> board 를 생성할때 누가 생성했는지 알아내기 위해
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(StatusEnum.NOT_FOUND_USER));

        // 글 존재 유무
        Board board = findBoard(id);
        if (findUser.getUsername().equals(board.getUser().getUsername())
                || findUser.getRole().getAuthority().equals("ROLE_ADMIN")) {
            board.update(requestDto);
            board = boardRepository.saveAndFlush(board);
        } else {
            throw new CustomException(StatusEnum.ONLY_CREATER);
        }
        return new BoardResponseDto(board);
    }

    /**
     * 게시판 글 삭제
     *
     * @param id
     * @return
     */
    public void deleteBoard(Long id, User user) {

        String username = user.getUsername();

        // 유저조회 -> board 를 생성할때 누가 생성했는지 알아내기 위해
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(StatusEnum.NOT_FOUND_USER));

        // 글 존재 유무
//        Board board = findBoard(id);
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new CustomException(StatusEnum.NOT_FOUND_BOARD)
        );

        if (findUser.getUsername().equals(board.getUser().getUsername())
                || findUser.getRole().getAuthority().equals("ROLE_ADMIN")) {
            boardRepository.delete(board);
        } else {
            throw new CustomException(StatusEnum.ONLY_CREATER);
        }
    }

    /**
     * 게시글 존재유무 유효성 검사
     *
     * @param id
     * @return
     */
    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new CustomException(StatusEnum.NOT_FOUND_BOARD)
        );
    }
}
