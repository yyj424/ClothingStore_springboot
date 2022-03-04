package com.yyj.wylie_yyj_springboot.service;

import com.yyj.wylie_yyj_springboot.domain.entity.Board;
import com.yyj.wylie_yyj_springboot.domain.entity.Comment;
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

@Service
@Transactional
@AllArgsConstructor
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public void saveComment(Comment comment) {
        commentRepository.saveAndFlush(comment);
    }
    public Comment getComment(Long id) {
        return commentRepository.getById(id);
    }
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
    public Page<Comment> getCommentList(int pageNo, Long bid) {
        Pageable pageable = PageRequest.of(pageNo - 1, 3, Sort.by("date").descending());
        return commentRepository.findAllByBid(pageable, bid);
    }
    public String findUidByCid(Long cid) {
        return commentRepository.findUidByCid(cid);
    }
}