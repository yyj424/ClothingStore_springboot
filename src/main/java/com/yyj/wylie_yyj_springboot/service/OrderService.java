package com.yyj.wylie_yyj_springboot.service;

import com.yyj.wylie_yyj_springboot.domain.entity.OrderDetail;
import com.yyj.wylie_yyj_springboot.domain.entity.Orders;
import com.yyj.wylie_yyj_springboot.domain.repository.OrderDetailRepository;
import com.yyj.wylie_yyj_springboot.domain.repository.OrdersRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<Orders> getOrderByUid(String uid) {
        return ordersRepository.findAllByUid(uid);
    }
    public Page<Orders> getOrderByUid(String uid, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        return ordersRepository.findAllByUid(pageable, uid);
    }
    public Page<Orders> getOrderByStatus(String uid, String status, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        return ordersRepository.findAllByUidAndStatus(pageable, uid, status);
    }
    public int getCountByStatus(String uid, String status) {
        return ordersRepository.countByUidAndStatus(uid, status);
    }
    public Page<OrderDetail> findAllDetails(String uid, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 5);
        return orderDetailRepository.findAllByOrders_UidOrderByOrders_Orid(pageable, uid);
    }
}
