package com.yyj.wylie_yyj_springboot.controller;

import com.yyj.wylie_yyj_springboot.domain.entity.*;
import com.yyj.wylie_yyj_springboot.dto.MyPageOrders;
import com.yyj.wylie_yyj_springboot.service.AccountService;
import com.yyj.wylie_yyj_springboot.service.BoardService;
import com.yyj.wylie_yyj_springboot.service.OrderService;
import com.yyj.wylie_yyj_springboot.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@AllArgsConstructor
public class MyPageController {
    @Autowired
    AccountService accountService;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;
    @Autowired
    BoardService boardService;

    @RequestMapping("/mypage/main")
    public String myPageMain(Authentication auth, Model model) {
        String uid = auth.getName();
        model.addAttribute("uid", uid);
        model.addAttribute("name", accountService.getUserName(uid));
        model.addAttribute("shippedBefore", orderService.getCountByStatus(uid, "결제완료"));
        model.addAttribute("shippedStandBy", orderService.getCountByStatus(uid, "배송준비중"));
        model.addAttribute("shippedBegin", orderService.getCountByStatus(uid, "배송중"));
        model.addAttribute("shippedComplete", orderService.getCountByStatus(uid, "배송완료"));
        model.addAttribute("orderCancel", orderService.getCountByStatus(uid, "취소"));
        model.addAttribute("orderExchange", orderService.getCountByStatus(uid, "교환"));
        model.addAttribute("orderReturn", orderService.getCountByStatus(uid, "반품"));
        return "/mypage/MyPageMain";
    }

    @RequestMapping("/mypage/orders/{page}")
    public String myPageOrders(@Param("status")String status, @Param("startDate")String startDate, @Param("endDate")String endDate, Authentication auth, Model model, @PathVariable("page")int page) throws ParseException {
        String uid = auth.getName();
        Page<OrderDetail> ordersPage;
        if (status != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            if (startDate == null || endDate == null) {
                Calendar cal = Calendar.getInstance();
                Date end = cal.getTime();
                endDate = formatter.format(end);
                cal.add(Calendar.MONTH, -2);
                Date start = cal.getTime();
                startDate = formatter.format(start);
                ordersPage = orderService.findAllDetailsWithStatus(uid, status, start, end, page);
            }
            else {
                ordersPage = orderService.findAllDetailsWithStatus(uid, status, formatter.parse(startDate), formatter.parse(endDate), page);
            }
        }
        else {
            ordersPage = orderService.findAllDetails(uid, page);
        }
        List<OrderDetail> orders = ordersPage.getContent();
        List<MyPageOrders> myPageOrders = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        for (OrderDetail order : orders) {
            ProductOption option = productService.getOptionById(order.getOpid());
            Product product = productService.getProductById(option.getPid());
            if (!idList.contains(order.getOrders().getOrid())) {
                idList.add(order.getOrders().getOrid());
            }
            else {
                idList.add(null);
            }
            myPageOrders.add(new MyPageOrders(order.getOrders().getOrid(), order.getOrders().getDate(), order.getOrders().getStatus(), product.getThumb(), product.getName(), option, order.getQuantity(), order.getPrice()));
        }
        model.addAttribute("idList", idList);
        model.addAttribute("orders", myPageOrders);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "/mypage/MyPageOrders";
    }

    @RequestMapping("/mypage/orders/detail/{id}")
    public String myPageOrders(@PathVariable("id") Long id, Authentication auth, Model model) {
        Orders order = orderService.findOrder(id);
        List<OrderDetail> details = orderService.findOrderDetails(id);
        List<MyPageOrders> myPageOrders = new ArrayList<>();
        for (OrderDetail detail : details) {
            ProductOption option = productService.getOptionById(detail.getOpid());
            Product product = productService.getProductById(option.getPid());
            myPageOrders.add(new MyPageOrders(detail.getOrders().getStatus(), product.getThumb(), product.getName(), option, detail.getQuantity(), detail.getPrice()));
        }
        model.addAttribute("account", accountService.getAccount(auth.getName()));
        model.addAttribute("order", order);
        model.addAttribute("details", myPageOrders);
        return "/mypage/MyPageOrdersDetail";
    }

    @RequestMapping("/mypage/board/list/{page}")
    public String myPageBoardList(Authentication auth, @PathVariable("page") int page, @Param("keyword")String keyword, Model model) {
        Page<Board> boardPage;
        if (keyword != null) {
            boardPage = boardService.getPostListByUidAndKeyword(page, auth.getName(), keyword);
            model.addAttribute("keyword", keyword);
        }
        else {
            boardPage = boardService.getPostListByUid(page, auth.getName());
        }
        List<Board> boardList = boardPage.getContent();
        model.addAttribute("boardList", boardList);
        model.addAttribute("boardPage", boardPage);
        return "/mypage/MyPageBoardList";
    }

    @RequestMapping("/mypage/board/{id}")
    public String myPageBoard(Authentication auth, @PathVariable("id") Long id, Model model) {
        Board board = boardService.getPost(id);
        model.addAttribute("board", board);
        return "/mypage/MyPageBoardDetail";
    }

    @RequestMapping("/mypage/board/update/{id}")
    public String myPageBoardUpdate(Authentication auth, @PathVariable("id") Long id, Board board, Model model) {
        Board updateBoard = boardService.getPost(id);
        updateBoard.setCategory(board.getCategory());
        updateBoard.setTitle(board.getTitle());
        updateBoard.setContent(board.getContent());
        boardService.savePost(updateBoard);
        return "redirect:/mypage/board/list/1";
    }

    @RequestMapping("/mypage/board/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deletePost(id);
        return "redirect:/mypage/board/list/1";
    }

    @RequestMapping("/mypage/account")
    public String account(Authentication auth, Model model) {
        Account account = accountService.getAccount(auth.getName());
        model.addAttribute("account", account);
        return "/mypage/MyPageAccount";
    }

    @RequestMapping("/mypage/account/update")
    @ResponseBody
    public void accountUpdate(Authentication auth, @RequestBody Map map) {
        Account updateAccount = accountService.getAccount(auth.getName());
        updateAccount.setName(map.get("name").toString());
        if (map.get("pw") != null) {
            updateAccount.setPw(map.get("pw").toString());
        }
        updateAccount.setPhone(map.get("phone").toString());
        updateAccount.setEmail(map.get("email").toString());
        updateAccount.setCode(map.get("postcode").toString());
        updateAccount.setAddr1(map.get("roadAddress").toString());
        updateAccount.setAddr2(map.get("detailAddress").toString());
        accountService.saveAccount(updateAccount);
    }
}