package com.sparta.lv5assignment.comment.service;


import com.sparta.lv5assignment.board.entity.Board;
import com.sparta.lv5assignment.board.repository.BoardRepository;
import com.sparta.lv5assignment.comment.dto.CommentRequestDto;
import com.sparta.lv5assignment.comment.dto.CommentResponseDto;
import com.sparta.lv5assignment.comment.entity.Comment;
import com.sparta.lv5assignment.comment.repository.CommentRepository;
import com.sparta.lv5assignment.global.dto.StatusEnum;
import com.sparta.lv5assignment.global.exception.CustomException;
import com.sparta.lv5assignment.global.jwt.JwtUtil;
import com.sparta.lv5assignment.user.entity.User;
import com.sparta.lv5assignment.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    public CommentResponseDto createCommentsInBoard(Long boardId, CommentRequestDto requestDto, User user) {

        // 누구인지 확인
        String username = user.getUsername();

        // 유저조회 -> board 를 생성할때 누가 생성했는지 알아내기 위해
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("해당 사용자가 없습니다"));

        // boardId로 실제로 저 게시글이 존재하는지 확인하기(DB에 있는지)
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException("해당 게시글이 없습니다"));

        // 댓글을 저장
        Comment comment = commentRepository.save(new Comment(findUser, findBoard, requestDto));
        return new CommentResponseDto(comment);
    }

    @Transactional // 변경감지
    public CommentResponseDto updateCommentInBoard(Long boardId, Long commentsId, CommentRequestDto requestDto, User user) {

        // 누구인지 확인
        String username = user.getUsername();

        // 유저조회 -> board 를 생성할때 누가 생성했는지 알아내기 위해
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("해당 사용자가 없습니다"));

        // boardId로 실제로 저 게시글이 존재하는지 확인하기(DB에 있는지)
        boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException("해당 게시글이 없습니다"));

        // commentsId로 실제로 저 댓글이 존재하는지 확인하기(DB에 있는지)
        Comment comment = commentRepository.findById(commentsId)
                .orElseThrow(() -> new CustomException("해당 댓글이 없습니다."));

        if (comment.getUser().getUsername().equals(findUser.getUsername())
                || findUser.getRole().getAuthority().equals("ROLE_ADMIN")) {
            comment.update(requestDto);
            Comment savedComment = commentRepository.saveAndFlush(comment);
            return new CommentResponseDto(savedComment);
        } else {
            throw new CustomException("해당 댓글의 작성자가 아닙니다.");
        }
    }


    public void deleteCommentInBoard(Long boardId, Long commentsId, User user) {

        // 누구인지 확인
        String username = user.getUsername();

        // 유저조회 -> board 를 생성할때 누가 생성했는지 알아내기 위해
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("해당 사용자가 없습니다"));

        // boardId로 실제로 저 게시글이 존재하는지 확인하기(DB에 있는지)
        boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException("해당 게시글이 없습니다"));

        // commentsId로 실제로 저 댓글이 존재하는지 확인하기(DB에 있는지)
        Comment comment = commentRepository.findById(commentsId)
                .orElseThrow(() -> new CustomException("해당 댓글이 없습니다."));

        if (comment.getUser().getUsername().equals(findUser.getUsername())
                || findUser.getRole().getAuthority().equals("ROLE_ADMIN")) {
            try {
                commentRepository.delete(comment);

            } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
                log.error(e.getMessage());
                throw new CustomException("알 수 없는 이유로 삭제할 수 없습니다. 입력한 정보를 확인하세요");
            }
        } else {
            throw new CustomException("해당 댓글의 작성자가 아닙니다.");
        }
    }

    /* javaDoc */
    /**
     * 대댓글 추가하기
     *
     * @param boardId
     * @param requestDto
     * @param user
     * @param commentId
     * @return CommentResponseDto
     */
    public CommentResponseDto createReplyInComment(Long boardId, CommentRequestDto requestDto, User user, Long commentId) {

        // 가지고 온 아이디 정보로 해당 게시글, 댓글, 사용자 존재 유무 확인하기
        // 누구인지 확인
        String username = user.getUsername();

        // 유저조회 -> board 를 생성할때 누가 생성했는지 알아내기 위해
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(StatusEnum.NOT_FOUND_USER));

        // boardId로 실제로 저 게시글이 존재하는지 확인하기(DB에 있는지)
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(StatusEnum.NOT_FOUND_BOARD));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(StatusEnum.NOT_FOUND_COMMENT));

                                                  /* 대댓글 객체 생성 */
        Comment saveReply = commentRepository.save(new Comment(findUser, findBoard, requestDto, comment));
        return new CommentResponseDto(saveReply);
    }
}