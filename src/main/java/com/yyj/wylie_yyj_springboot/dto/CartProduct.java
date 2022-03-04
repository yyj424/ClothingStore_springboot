package com.yyj.wylie_yyj_springboot.dto;

import com.yyj.wylie_yyj_springboot.domain.entity.Product;
import com.yyj.wylie_yyj_springboot.domain.entity.ProductOption;

import java.util.List;

public class CartProduct {
    Long cartId;
    Product product;
    ProductOption option;
    int quantity;
    int price;
    List<CartProduct> cartProductList;

    public CartProduct(Long cartId, Product product, ProductOption option, int quantity, int price) {
        this.cartId = cartId;
        this.product = product;
        this.option = option;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductOption getOption() {
        return option;
    }

    public void setOption(ProductOption option) {
        this.option = option;
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

    public List<CartProduct> getCartProductList() {
        return cartProductList;
    }

    public void setCartProductList(List<CartProduct> cartProductList) {
        this.cartProductList = cartProductList;
    }

    @Override
    public boolean equals(Object obj) {
        CartProduct cartProduct = (CartProduct) obj;
        if (cartProduct.option.getOid() == this.option.getOid()) {
            return true;
        }
        return false;
    }
}
