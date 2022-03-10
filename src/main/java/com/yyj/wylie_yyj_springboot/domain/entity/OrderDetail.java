package com.yyj.wylie_yyj_springboot.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OrderDetail {
    @Id
    @Column(name = "orderdetail_id")
    @SequenceGenerator(name = "ordtlid_seq_generator", sequenceName = "ordtlid_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ordtlid_seq_generator")
    private Long did;
    @Column(name = "option_id")
    private Long opid;
    @Column(name = "order_id")
    private Long orid;
    private int quantity;
    private int price;

    public OrderDetail(Long opid, int quantity, int price) {
        this.opid = opid;
        this.quantity = quantity;
        this.price = price;
    }

    @ManyToOne(targetEntity = Orders.class)
    @JoinColumn(name="order_id", insertable=false, updatable=false)
    private Orders orders;

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Long getOpid() {
        return opid;
    }

    public void setOpid(Long opid) {
        this.opid = opid;
    }

    public Long getOrid() {
        return orid;
    }

    public void setOrid(Long orid) {
        this.orid = orid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
