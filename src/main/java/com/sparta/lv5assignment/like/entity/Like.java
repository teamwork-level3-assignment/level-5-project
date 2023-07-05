package com.sparta.lv5assignment.like.entity;

import com.sparta.lv5assignment.board.entity.Board;
import com.sparta.lv5assignment.comment.entity.Comment;
import com.sparta.lv5assignment.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "likes")
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Like(User findUser, Board board) {
        this.user = findUser;
        this.board = board;
    }

    public Like(User findUser, Board board, Comment comment) {
        this.user = findUser;
        this.board = board;
        this.comment = comment;
    }

    public Like(User findUser, Comment comment) {
        this.user = findUser;
        this.comment = comment;
    }
}
