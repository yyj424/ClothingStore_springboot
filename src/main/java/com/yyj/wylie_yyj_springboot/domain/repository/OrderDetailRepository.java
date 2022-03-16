package com.yyj.wylie_yyj_springboot.domain.repository;

import com.yyj.wylie_yyj_springboot.domain.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findAllByOrders_Orid(Long orid);
    Page<OrderDetail> findAllByOrders_Uid(Pageable pageable, String uid);
    Page<OrderDetail> findAllByOrders_UidAndOrders_StatusAndOrders_DateBetween(Pageable pageable, String uid, String status, Date startDate, Date endDate);
    Page<OrderDetail> findAllByOrders_UidAndOrders_DateBetween(Pageable pageable, String uid, Date startDate, Date endDate);
}
