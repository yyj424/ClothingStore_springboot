package com.yyj.wylie_yyj_springboot.domain.repository;

import com.yyj.wylie_yyj_springboot.domain.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    OrderDetail getByOrid(Long orderId);
}
