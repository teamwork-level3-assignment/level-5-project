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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent_id;

    @OneToMany(mappedBy = "parent_id", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = {CascadeType.REMOVE})
    private List<Like> likeList = new ArrayList<>();

    public Comment(User user, Board findBoard, CommentRequestDto requestDto) {
        this.user = user;
        this.contents = requestDto.getContents();
        this.board = findBoard;
    }


    // 단방향일 때 그냥 저장하는 로직과 동일
    public Comment(User findUser, Board findBoard, CommentRequestDto requestDto, Comment comment) {
        this.user = findUser;
        this.board = findBoard;
        this.contents = requestDto.getContents();
        this.parent_id = comment;


        // 자식으로 들어온 경우. 연관관계의 주인이 아닌 entity 가 들어온 경우,

    }

    public void update(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }
}
