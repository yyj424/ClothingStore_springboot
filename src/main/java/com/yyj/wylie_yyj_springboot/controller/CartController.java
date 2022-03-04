package com.yyj.wylie_yyj_springboot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yyj.wylie_yyj_springboot.domain.entity.Account;
import com.yyj.wylie_yyj_springboot.domain.entity.Cart;
import com.yyj.wylie_yyj_springboot.domain.entity.Product;
import com.yyj.wylie_yyj_springboot.domain.entity.ProductOption;
import com.yyj.wylie_yyj_springboot.dto.CartProduct;
import com.yyj.wylie_yyj_springboot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {
    @Autowired
    AccountService accountService;
    @Autowired
    ProductService productService;
    @Autowired
    ImageService imageService;
    @Autowired
    OrderService orderService;
    @Autowired
    CartService cartService;

    @RequestMapping("/cart")
    public String add(Model model, Authentication auth) {
        String uid = auth.getName();
        List<CartProduct> cartList = new ArrayList<>();
        int total = 0;
        for (Cart cart : cartService.getCart(uid)) {
            ProductOption option = productService.getOptionById(cart.getOid());
            Product product = productService.getProductById(option.getPid());
            cartList.add(new CartProduct(cart.getCid(), product, option, cart.getQuantity(), cart.getPrice()));
            total += cart.getPrice();
        }
        model.addAttribute("uid", uid);
        model.addAttribute("cartList", cartList);
        model.addAttribute("total", total);
        return "/shop/Cart";
    }

    @RequestMapping("/cart/add")
    @ResponseBody
    public void add(@RequestBody Map map, Authentication auth) {
        ObjectMapper objectMapper = new ObjectMapper();
        List list = objectMapper.convertValue(map.get("options"), List.class);
        String uid = auth.getName();
        for (Object obj : list) {
            Map<String, String> cartMap = objectMapper.convertValue(obj, Map.class);
            int qty = Integer.parseInt(cartMap.get("qty"));
            int price = Integer.parseInt(map.get("price").toString()) * qty;
            Long oid = Long.parseLong(cartMap.get("id"));
            if (cartService.getCart(oid, uid) != null) {
                Cart cart = cartService.getCart(oid, uid);
                cart.setQuantity(cart.getQuantity() + qty);
                cart.setPrice(cart.getPrice() + price);
                cartService.saveCart(cart);
            }
            else {
                Cart cart = new Cart(uid, oid, qty, price);
                cartService.saveCart(cart);
            }
        } //auth가 null인 경우 어떻게 할지 고려
    }

    @RequestMapping("/cart/delete")
    @ResponseBody
    public void delete(@RequestBody Map map) {
        cartService.deleteCart(Long.parseLong(map.get("id").toString()));
    }

    @RequestMapping("/cart/minus")
    @ResponseBody
    public void minus(@RequestBody Map map) {
        Cart cart = cartService.getCartById(Long.parseLong(map.get("id").toString()));
        int price = cart.getPrice() / cart.getQuantity();
        int qty = cart.getQuantity() - 1;
        cart.setQuantity(qty);
        cart.setPrice(price * qty);
        cartService.saveCart(cart);
    }

    @RequestMapping("/cart/plus")
    @ResponseBody
    public void plus(@RequestBody Map map) {
        Cart cart = cartService.getCartById(Long.parseLong(map.get("id").toString()));
        int price = cart.getPrice() / cart.getQuantity();
        int qty = cart.getQuantity() + 1;
        cart.setQuantity(qty);
        cart.setPrice(price * qty);
        cartService.saveCart(cart);
    }
}
