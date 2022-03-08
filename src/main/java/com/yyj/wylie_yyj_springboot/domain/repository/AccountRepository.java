package com.yyj.wylie_yyj_springboot.domain.repository;

import com.yyj.wylie_yyj_springboot.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, String> {
    @Query(value="select name from Account where user_id=:uid", nativeQuery=true)
    String getNameByUid(String uid);
}
