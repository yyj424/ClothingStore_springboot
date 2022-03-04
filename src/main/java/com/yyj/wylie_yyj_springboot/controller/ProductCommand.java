package com.yyj.wylie_yyj_springboot.controller;

import com.yyj.wylie_yyj_springboot.domain.entity.ProductOption;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductCommand {
    private int category;
    private String name;
    private int price;
    private String desc;
    private MultipartFile mainThumb;
    private List<MultipartFile> thumb;
    private List<MultipartFile> image;
    private int discount;
    List<ProductOption> options;

    public ProductCommand(int category, String name, int price, String desc, MultipartFile mainThumb, List<MultipartFile> thumb, List<MultipartFile> image, int discount, List<ProductOption> options) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.mainThumb = mainThumb;
        this.thumb = thumb;
        this.image = image;
        this.discount = discount;
        this.options = options;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public MultipartFile getMainThumb() {
        return mainThumb;
    }

    public void setMainThumb(MultipartFile mainThumb) {
        this.mainThumb = mainThumb;
    }

    public List<MultipartFile> getThumb() {
        return thumb;
    }

    public void setThumb(List<MultipartFile> thumb) {
        this.thumb = thumb;
    }

    public List<MultipartFile> getImage() {
        return image;
    }

    public void setImage(List<MultipartFile> image) {
        this.image = image;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<ProductOption> getOptions() {
        return options;
    }

    public void setOptions(List<ProductOption> options) {
        this.options = options;
    }
}
