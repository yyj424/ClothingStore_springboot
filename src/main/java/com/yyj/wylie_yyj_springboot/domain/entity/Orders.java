package com.yyj.wylie_yyj_springboot.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Orders {
    @Id
    @Column(name = "order_id")
    @SequenceGenerator(name = "orid_seq_generator", sequenceName = "orid_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orid_seq_generator")
    private Long orid;
    @Column(name = "user_id")
    private String uid;
    private String name;
    private String phone;
    @Column(name = "postcode")
    private String code;
    @Column(name = "address1")
    private String addr1;
    @Column(name = "address2")
    private String addr2;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "orderdate")
    private Date date;
    private String status;
    @Column(name = "payment")
    private String pay;
    private int totalPrice;

    public Orders(String uid, String name, String phone, String code, String addr1, String addr2, String status, String pay, int totalPrice) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.code = code;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.status = status;
        this.pay = pay;
        this.totalPrice = totalPrice;
    }

    public Long getOrid() {
        return orid;
    }

    public void setOrid(Long orid) {
        this.orid = orid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalprice) {
        this.totalPrice = totalprice;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orid=" + orid +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", code='" + code + '\'' +
                ", addr1='" + addr1 + '\'' +
                ", addr2='" + addr2 + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", pay='" + pay + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}