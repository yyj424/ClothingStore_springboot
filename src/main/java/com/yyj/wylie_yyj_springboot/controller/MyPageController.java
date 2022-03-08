package com.yyj.wylie_yyj_springboot.controller;

import com.yyj.wylie_yyj_springboot.domain.entity.Orders;
import com.yyj.wylie_yyj_springboot.service.AccountService;
import com.yyj.wylie_yyj_springboot.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class MyPageController {
    @Autowired
    AccountService accountService;

    @Autowired
    OrderService orderService;

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
    public String myPageOrders(Authentication auth, Model model, @PathVariable("page")int page) {
        String uid = auth.getName();
        model.addAttribute("uid", uid);
        List<Orders> orders = orderService.getOrderByUid(uid);
        for (Orders order : orders) {
            //find order detail > qty,price,opid > Option op > pid > product thumbnail

        }
        //model.addAttribute("orders", );
        return "/mypage/MyPageOrders";
    }
}
