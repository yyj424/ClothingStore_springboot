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

import java.util.Calendar;
import java.util.Date;
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
    public Orders findOrder(Long id) { return ordersRepository.getById(id); }
    public List<OrderDetail> findOrderDetails(Long id) { return orderDetailRepository.findAllByOrders_Orid(id); }
    public int getCountByStatus(String uid, String status) {
        return ordersRepository.countByUidAndStatus(uid, status);
    }
    public Page<OrderDetail> findAllDetails(String uid, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 5, Sort.by("orders.orid").descending());
        return orderDetailRepository.findAllByOrders_Uid(pageable, uid);
    }
    public Page<OrderDetail> findAllDetailsWithStatus(String uid, String status, Date start, Date end, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 5, Sort.by("orders.orid").descending());
        Calendar cal = Calendar.getInstance();
        cal.setTime(end);
        cal.add(Calendar.DATE, 1);
        end = new Date(cal.getTimeInMillis());
        if (status.equals("전체")) {
            return orderDetailRepository.findAllByOrders_UidAndOrders_DateBetween(pageable, uid, start, end);
        }
        else {
            return orderDetailRepository.findAllByOrders_UidAndOrders_StatusAndOrders_DateBetween(pageable, uid, status, start, end);
        }
    }
}
