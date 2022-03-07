package com.yyj.wylie_yyj_springboot.controller;

import com.yyj.wylie_yyj_springboot.domain.entity.Account;
import com.yyj.wylie_yyj_springboot.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/signup")
    public String signUp(){
        return "/account/SignUpForm";
    }

    @PostMapping("/signup")
    public String singUp(Account account) {
        accountService.saveAccount(account);
        return "/account/SignUpComplete";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "account/LoginForm";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "/";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "/admin/AdminMain";
    }

    @RequestMapping("/mypage/main")
    public String myPageMain(Authentication auth, Model model) {
        model.addAttribute("account", accountService.getAccount(auth.getName()));
        return "/account/MyPageMain";
    }
}
