package com.sparta.lv3assignment.service;

import com.sparta.lv3assignment.dto.BoardRequestDto;
import com.sparta.lv3assignment.dto.BoardResponseDto;
import com.sparta.lv3assignment.entity.Board;
import com.sparta.lv3assignment.entity.Message;
import com.sparta.lv3assignment.entity.StatusEnum;
import com.sparta.lv3assignment.entity.User;
import com.sparta.lv3assignment.jwt.JwtUtil;
import com.sparta.lv3assignment.repository.BoardRepository;
import com.sparta.lv3assignment.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private HttpServletRequest req;

    private JwtUtil jwtUtil;

    /**
     * 게시판 글 생성
     *
     * @param requestDto
     * @return
     */
    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        // 토큰 => 유효성 검사 및 사용자 정보 가져오기
        Claims info = getClaims();
        String username = info.getSubject();

        // 유저조회 -> board 를 생성할때 누가 생성했는지 알아내기 위해
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("해당 사용자가 없습니다"));

        // RequestDto -> Entity
        Board board = new Board(requestDto, user);
        // DB 저장
        Board saveBoard = boardRepository.save(board);
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
     * @return
     */
    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto) {
        Claims info = getClaims();
        String username = info.getSubject();

        // 유저조회 -> board 를 생성할때 누가 생성했는지 알아내기 위해
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("해당 사용자가 없습니다"));

        // 글 존재 유무
        Board board = findBoard(id);
        if (user.getUsername().equals(board.getUser().getUsername())) {
            board.update(requestDto);
            board = boardRepository.saveAndFlush(board);
        } else {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        return new BoardResponseDto(board);
    }

    /**
     * 게시판 글 삭제
     *
     * @param id
     * @return
     */
    public ResponseEntity<Message> deleteBoard(Long id) {
        Message message = new Message();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Claims info = getClaims();
        String username = info.getSubject();

        // 유저조회 -> board 를 생성할때 누가 생성했는지 알아내기 위해
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("해당 사용자가 없습니다"));

        // 글 존재 유무
        Board board = findBoard(id);
        if (user.getUsername().equals(board.getUser().getUsername())) {
            boardRepository.delete(board);
            message.setStatus(StatusEnum.OK);
            message.setMessage("게시글 삭제 성공");
        } else {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    /**
     * 게시글 존재유무 유효성 검사
     *
     * @param id
     * @return
     */
    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 글이 존재하지 않습니다.")
        );
    }

    /**
     * 토큰 유효성 검사 및 사용자 정보 가져오기
     *
     * @return
     */
    private Claims getClaims() {
        String token = jwtUtil.getTokenFromHeader(req);
        token = jwtUtil.substringToken(token);
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
        Claims info = jwtUtil.getUserInfoFromToken(token);
        return info;
    }
}
