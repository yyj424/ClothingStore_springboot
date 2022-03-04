package com.yyj.wylie_yyj_springboot.service;

import com.yyj.wylie_yyj_springboot.domain.entity.OrderDetail;
import com.yyj.wylie_yyj_springboot.domain.entity.Orders;
import com.yyj.wylie_yyj_springboot.domain.repository.OrderDetailRepository;
import com.yyj.wylie_yyj_springboot.domain.repository.OrdersRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class OrderService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public void saveOrder(Orders orders) {
        ordersRepository.saveAndFlush(orders);
    }
    public void saveOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.saveAndFlush(orderDetail);
    }
    public Long findLastOid() {
        return ordersRepository.findLastOid();
    }
}
