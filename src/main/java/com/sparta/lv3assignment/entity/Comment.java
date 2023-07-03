package com.sparta.lv3assignment.entity;


import com.sparta.lv3assignment.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    private String username;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public Comment(String username, Board findBoard, CommentRequestDto requestDto) {
        this.username = username;
        this.contents = requestDto.getContents();
        this.board = findBoard;
    }

    public void update(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }
}
