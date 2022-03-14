package com.yyj.wylie_yyj_springboot.domain.repository;

import com.yyj.wylie_yyj_springboot.domain.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Page<OrderDetail> findAllByOrders_UidOrderByOrders_Orid(Pageable pageable, String uid);
    Page<OrderDetail> findAllByOrders_UidAndOrders_StatusAndOrders_DateBetweenOrderByOrders_Orid(Pageable pageable, String uid, String status, Date startDate, Date endDate);
    Page<OrderDetail> findAllByOrders_UidAndOrders_DateBetweenOrderByOrders_Orid(Pageable pageable, String uid, Date startDate, Date endDate);
}
