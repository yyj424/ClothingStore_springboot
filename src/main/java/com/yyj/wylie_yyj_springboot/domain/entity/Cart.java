package com.yyj.wylie_yyj_springboot.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Cart {
    @Id
    @Column(name = "cart_id")
    @SequenceGenerator(name = "crtid_seq_generator", sequenceName = "crtid_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crtid_seq_generator")
    private Long cid;
    @Column(name = "user_id")
    private String uid;
    @Column(name = "option_id")
    private Long oid;
    private int quantity;
    private int price;

    public Cart(String uid, Long oid, int quantity, int price) {
        this.uid = uid;
        this.oid = oid;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
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

    @Override
    public String toString() {
        return "Cart{" +
                "uid='" + uid + '\'' +
                ", oid=" + oid +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
