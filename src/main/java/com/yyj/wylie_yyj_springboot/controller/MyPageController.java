package com.yyj.wylie_yyj_springboot.controller;

import com.yyj.wylie_yyj_springboot.domain.entity.OrderDetail;
import com.yyj.wylie_yyj_springboot.domain.entity.Orders;
import com.yyj.wylie_yyj_springboot.domain.entity.Product;
import com.yyj.wylie_yyj_springboot.domain.entity.ProductOption;
import com.yyj.wylie_yyj_springboot.dto.MyPageOrders;
import com.yyj.wylie_yyj_springboot.service.AccountService;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class MyPageController {
    @Autowired
    AccountService accountService;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

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
            ordersPage = orderService.findAllDetailsWithStatus(uid, status, formatter.parse(startDate), formatter.parse(endDate), page);
        }
        else {
            ordersPage = orderService.findAllDetails(uid, page);
        }
        List<OrderDetail> orders = ordersPage.getContent();
        List<MyPageOrders> myPageOrders = new ArrayList<>();
        for (OrderDetail order : orders) {
            ProductOption option = productService.getOptionById(order.getOpid());
            Product product = productService.getProductById(option.getPid());
            myPageOrders.add(new MyPageOrders(order.getOrders().getOrid(), order.getOrders().getDate(), order.getOrders().getStatus(), product.getThumb(), product.getName(), option, order.getQuantity(), order.getPrice()));
        }
        model.addAttribute("orders", myPageOrders);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "/mypage/MyPageOrders";
    }
}