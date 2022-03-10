package com.yyj.wylie_yyj_springboot.dto;

import com.yyj.wylie_yyj_springboot.domain.entity.ProductOption;

import java.util.Date;

public class MyPageOrders {
    Long orderId;
    Date orderDate;
    String status;
    String thumbnail;
    String name;
    ProductOption option;
    int qty;
    int price;

    public MyPageOrders(Long orderId, Date orderDate, String status, String thumbnail, String name, ProductOption option, int qty, int price) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.status = status;
        this.thumbnail = thumbnail;
        this.name = name;
        this.option = option;
        this.qty = qty;
        this.price = price;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductOption getOption() {
        return option;
    }

    public void setOption(ProductOption option) {
        this.option = option;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
