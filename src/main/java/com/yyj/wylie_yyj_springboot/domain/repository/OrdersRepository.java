package com.yyj.wylie_yyj_springboot.domain.repository;

import com.yyj.wylie_yyj_springboot.domain.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query(value="select ORID_SEQ.CURRVAL from DUAL", nativeQuery=true)
    Long findLastOid();
    int countByUidAndStatus(String uid, String status);
    List<Orders> findAllByUid(String uid);
    Page<Orders> findAllByUid(Pageable pageable, String uid);
}
