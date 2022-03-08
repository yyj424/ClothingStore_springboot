package com.yyj.wylie_yyj_springboot.service;

import com.yyj.wylie_yyj_springboot.domain.Role;
import com.yyj.wylie_yyj_springboot.domain.entity.Account;
import com.yyj.wylie_yyj_springboot.domain.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    //    public List<Account> getAccountList() {
//        return accountRepository.findAll();
//    }
    public void saveAccount(Account account) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        account.setPw(passwordEncoder.encode(account.getPw()));
        accountRepository.saveAndFlush(account);
    }
    public Account getAccount(String id) {
        return accountRepository.getById(id);
    }
//    public void deleteAccount(String id) {
//        accountRepository.deleteById(id);
//    }

    public String getUserName(String id) { return accountRepository.getNameByUid(id); }

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        Account account = accountRepository.getById(uid);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        if (("admin").equals(uid)) {
            authorityList.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorityList.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }
        return new User(uid, account.getPw(), authorityList);
    }
}
