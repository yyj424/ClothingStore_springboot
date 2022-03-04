package com.yyj.wylie_yyj_springboot.controller;

import com.yyj.wylie_yyj_springboot.domain.entity.Board;
import com.yyj.wylie_yyj_springboot.domain.entity.Comment;
import com.yyj.wylie_yyj_springboot.service.BoardService;
import com.yyj.wylie_yyj_springboot.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class BoardController {
    @Autowired
    BoardService boardService;
    @Autowired
    CommentService commentService;

//    @RequestMapping("/board/list")
//    public String list(Model model) {
//        List<Board> boardList = service.getPostList();
//        model.addAttribute("boardList", boardList);
//        return "board/BoardList";
//    }

    @RequestMapping("/board/list/{page}")
    public String list(@PageableDefault Pageable pageable, @PathVariable("page") int page, Model model) {
        Page<Board> boardPage = boardService.getPostList(page);
        List<Board> boardList = boardPage.getContent();
        model.addAttribute("boardList", boardList);
        model.addAttribute("boardPage", boardPage);
        return "board/BoardList";
    }

    @GetMapping("/board/post")
    public String form(Model model, Authentication auth) {
        model.addAttribute("uid", auth.getName());
        return "board/BoardForm";
    }

    @PostMapping("/board/post")
    public String post(Board board, Authentication auth) {
        board.setUid(auth.getName());
        boardService.savePost(board);
        return "redirect:/board/list/1";
    }

    @RequestMapping("/board/{id}")
    public String view(@PathVariable("id") Long id, Model model, @RequestParam(value="cmtPage") int cmtPage, Authentication auth) {
        Board board = boardService.getPost(id);
        Page<Comment> commentPage = commentService.getCommentList(cmtPage, id);
        List<Comment> commentList = commentPage.getContent();
        model.addAttribute("board", board);
        model.addAttribute("commentPage", commentPage);
        model.addAttribute("commentList", commentList);
        if (auth != null) {
            model.addAttribute("uid", auth.getName());
        }
        return "board/BoardDetail";
    }

    @GetMapping("/board/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model, Authentication auth) {
        if (!boardService.findUidByBid(id).equals(auth.getName())) {
            return "/account/LoginForm";
        }
        Board board = boardService.getPost(id);
        model.addAttribute("board", board);
        return "board/BoardUpdate";
    }

    @PostMapping("/board/update/{id}")
    public String update(@PathVariable("id") Long id, Board board, Authentication auth) {
        Board updateBoard = boardService.getPost(id);
        updateBoard.setCategory(board.getCategory());
        updateBoard.setTitle(board.getTitle());
        updateBoard.setContent(board.getContent());
        boardService.savePost(updateBoard);
        return "redirect:/board/list/1";
    }

    @RequestMapping("/board/delete/{id}")
    public String delete(@PathVariable("id") Long id, Authentication auth) {
        if (!boardService.findUidByBid(id).equals(auth.getName())) {
            return "/account/LoginForm";
        }
        boardService.deletePost(id);
        return "redirect:/board/list/1";
    }

    @RequestMapping("/board/category/{category}/{page}")
    public String filtering(@PageableDefault Pageable pageable, @PathVariable("page") int page, @PathVariable("category") int category, Model model) {
        Page<Board> boardPage = boardService.filteringByCategory(category, page);
        List<Board> boardList = boardPage.getContent();
        model.addAttribute("boardList", boardList);
        model.addAttribute("boardPage", boardPage);
        model.addAttribute("category", category);
        return "board/BoardList";
    }

    @RequestMapping("/board/search/{page}")
    public String search(@PageableDefault Pageable pageable, @PathVariable("page") int page, @RequestParam(value="keyword") String keyword, Model model) {
        Page<Board> boardPage = boardService.searchPostList(keyword, page);
        List<Board> boardList = boardPage.getContent();
        if (boardList.isEmpty()) {
            model.addAttribute("noResult", true);
        }
        model.addAttribute("boardList", boardList);
        model.addAttribute("boardPage", boardPage);
        model.addAttribute("keyword", keyword);
        return "board/BoardList";
    }

    @RequestMapping("/board/{id}/comment")
    public String postComment(@PathVariable("id") Long id, Comment comment, Authentication auth) {
        comment.setUid(auth.getName());
        comment.setBid(id);
        commentService.saveComment(comment);
        return "redirect:/board/" + id + "?cmtPage=1";
    }

    @RequestMapping("/board/{bid}/comment/delete/{cid}")
    public String deleteComment(@PathVariable("bid") Long bid, @PathVariable("cid") Long cid, Authentication auth) {
        if (!commentService.findUidByCid(cid).equals(auth.getName())) {
            return "/account/LoginForm";
        }
        commentService.deleteComment(cid);
        return "redirect:/board/" + bid + "?cmtPage=1";
    }

//    @GetMapping("/comment/{cid}")
//    @ResponseBody
//    public String showUpdateComment(@PathVariable("cid") Long cid, Model model) {
//        Comment comment = commentService.getComment(cid);
//        Map<String, String> cmtMap = new HashMap<>();
//        cmtMap.put("uid", comment.getUid());
//        cmtMap.put("content", comment.getContent());
//        model.addAttribute("cmtUid", comment.getUid() );
//        return null;
//    }
//
//    @PostMapping("/board/{id}/update/comment/{cid}")
//    public String updateComment(@PathVariable("id") Long id, Comment comment) {
//
//        return "redirect:/board/" + id;
//    }
}
