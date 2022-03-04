package com.yyj.wylie_yyj_springboot.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ProductOption {
    @Id
    @Column(name = "option_id")
    @SequenceGenerator(name = "poid_seq_generator", sequenceName = "poid_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poid_seq_generator")
    private Long oid;
    private String color;
    private int stock;
    @Column(name = "sizeoption")
    private String size;
    @Column(name = "product_id")
    private Long pid;

    public ProductOption(String color, int stock, String size, Long pid) {
        this.color = color;
        this.stock = stock;
        this.size = size;
        this.pid = pid;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
