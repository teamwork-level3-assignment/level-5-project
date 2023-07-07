package com.sparta.lv5assignment.category.entity;


import com.sparta.lv5assignment.board.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CategoryBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public CategoryBoard(Board saveBoard, Category category) {
        this.board = saveBoard;
        this.category = category;
        board.getCategoryBoardList().add(this); // 연관관계 설정
    }
}
