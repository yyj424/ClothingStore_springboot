package com.yyj.wylie_yyj_springboot.service;

import com.yyj.wylie_yyj_springboot.domain.entity.Board;
import com.yyj.wylie_yyj_springboot.domain.repository.BoardRepository;
import com.yyj.wylie_yyj_springboot.domain.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    public void savePost(Board board) {
        boardRepository.saveAndFlush(board);
    }
    public Board getPost(Long id) {
        return boardRepository.getById(id);
    }
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }
    public Page<Board> getPostList(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 5, Sort.by("date").descending());
        return boardRepository.findAll(pageable);
    }
    public Page<Board> filteringByCategory(int category, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 5, Sort.by("date").descending());
        return boardRepository.findAllByCategory(pageable, category);
    }
    public Page<Board> searchPostList(String keyword, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 5, Sort.by("date").descending());
        return boardRepository.findByTitleContainingIgnoreCase(pageable, keyword);
    }
    public String findUidByBid(Long id) {
        return boardRepository.findUidByBid(id);
    }
}
