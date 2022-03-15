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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                System.out.println("$$$$$$$$$$$$$$$$" + startDate +",,,,," + endDate);
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
        Long id = 0L;
        for (OrderDetail order : orders) {
            ProductOption option = productService.getOptionById(order.getOpid());
            Product product = productService.getProductById(option.getPid());
            if (!Objects.equals(id, order.getOrders().getOrid())) {
                myPageOrders.add(new MyPageOrders(order.getOrders().getOrid(), order.getOrders().getDate(), order.getOrders().getStatus(), product.getThumb(), product.getName(), option, order.getQuantity(), order.getPrice()));
                id = order.getOrders().getOrid();
            }
            else {
                myPageOrders.add(new MyPageOrders(null, order.getOrders().getDate(), order.getOrders().getStatus(), product.getThumb(), product.getName(), option, order.getQuantity(), order.getPrice()));
            }
        }
        model.addAttribute("orders", myPageOrders);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "/mypage/MyPageOrders";
    }

    @RequestMapping("/mypage/board/{page}")
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
}