package com.sparta.lv5assignment.comment.entity;


import com.sparta.lv5assignment.board.entity.Board;
import com.sparta.lv5assignment.comment.dto.CommentRequestDto;
import com.sparta.lv5assignment.global.entity.Timestamped;
import com.sparta.lv5assignment.like.entity.Like;
import com.sparta.lv5assignment.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "comment", cascade = {CascadeType.REMOVE})
    private List<Like> likeList = new ArrayList<>();

    public Comment(User user, Board findBoard, CommentRequestDto requestDto) {
        this.user = user;
        this.contents = requestDto.getContents();
        this.board = findBoard;
    }

    public void update(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }
}
