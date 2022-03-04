package com.yyj.wylie_yyj_springboot.dto;

import lombok.*;
@Getter
@Setter
public class ExcelData {
    private int num;
    private String name;
    private String color;
    private String size;
    private int stock;
    private int sales;

    public ExcelData() {
    }

    public ExcelData(int num, String name, String color, String size, int stock, int sales) {
        this.num = num;
        this.name = name;
        this.color = color;
        this.size = size;
        this.stock = stock;
        this.sales = sales;
    }
}