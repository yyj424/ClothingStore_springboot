package com.yyj.wylie_yyj_springboot.domain.repository;

import com.yyj.wylie_yyj_springboot.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByCategory(Pageable pageable, int category);
    Page<Board> findByTitleContainingIgnoreCase(Pageable pageable, String keyword);
    @Query("select b.uid from Board b where b.bid = ?1")
    String findUidByBid(Long id);
}
