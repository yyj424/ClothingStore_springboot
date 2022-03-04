package com.yyj.wylie_yyj_springboot.controller;

import com.yyj.wylie_yyj_springboot.domain.entity.*;
import com.yyj.wylie_yyj_springboot.dto.CartProduct;
import com.yyj.wylie_yyj_springboot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {
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

    @GetMapping("/order")
    public String orderForm(@RequestParam("pid")Long id, @RequestParam("option")List<String> oids, @RequestParam("totalPrice")String total, @RequestParam Map<String, String> map,
                            Model model, Authentication auth, @RequestParam("price")String price) {
        Account account = accountService.getAccount(auth.getName());
        Product product = productService.getProductById(id);
        List<ProductOption> options = new ArrayList<>();
        Map<Long, Integer> quantity = new HashMap<>();
        for (String oid : oids) {
            options.add(productService.getOptionById(Long.valueOf(oid)));
            if (map.containsKey(oid)) {
                quantity.put(Long.valueOf(oid), Integer.valueOf(map.get(oid)));
            }
        }
        model.addAttribute("account", account);
        model.addAttribute("product", product);
        model.addAttribute("options", options);
        model.addAttribute("quantity", quantity);
        model.addAttribute("total", Integer.valueOf(total));
        model.addAttribute("price", Integer.valueOf(price));
        return "/shop/OrderForm";
    }

    @RequestMapping("/cart/order")
    public String cartOrderForm(Authentication auth, @RequestParam("selected")List<String> cids, @RequestParam("totalPrice")String total, Model model) {
        List<CartProduct> cartList = new ArrayList<>();
        Account account = accountService.getAccount(auth.getName());
        for (String id : cids) {
            Cart cart = cartService.getCartById(Long.valueOf(id));
            ProductOption option = productService.getOptionById(cart.getOid());
            Product product = productService.getProductById(option.getPid());
            cartList.add(new CartProduct(cart.getCid(), product, option, cart.getQuantity(), cart.getPrice()));
        }
        model.addAttribute("account", account);
        model.addAttribute("cartList", cartList);
        model.addAttribute("total", Integer.parseInt(total));
        System.out.println("*********************total: " + total);
        return "/shop/CartOrderForm";
    }

    @PostMapping("/order")
    public String order(@ModelAttribute("OrderCommand") OrderCommand cmd, Authentication auth, RedirectAttributes attr) {
        cmd.order.setStatus("결제완료");
        orderService.saveOrder(cmd.order);
        Long oid = orderService.findLastOid();
        Map<Long, Integer> inStock = new HashMap<>();
        for (OrderDetail detail : cmd.details) {
            //주문 상품 저장
            detail.setOrid(oid);
            orderService.saveOrderDetail(detail);
            //재고 변경
            ProductOption option = productService.getOptionById(detail.getOpid());
            option.setStock(option.getStock() - detail.getQuantity());
            productService.saveProductOption(option);
            if (inStock.containsKey(option.getPid())) {
                inStock.put(option.getPid(), inStock.get(option.getPid()) + option.getStock());
            }
            else {
                inStock.put(option.getPid(), option.getStock());
            }
            //장바구니에 있는 경우 삭제
            Cart cart = cartService.getCart(detail.getOpid(), auth.getName());
            if (cart != null) {
                cartService.deleteCart(cart.getCid());
            }
        }
        for (Long pid : inStock.keySet()){
            System.out.println("*************************Key:"+pid+", Value:"+inStock.get(pid)); ///장바구니 구현 후 확인
            if (inStock.get(pid) == 0) {
                Product product = productService.getProductById(pid);
                product.setSoldOut("Y");
                productService.saveProduct(product);
            }
        }
        attr.addAttribute("oid", oid);
        return "redirect:/order/confirm";
    }

    @RequestMapping("/order/confirm")
    public String confirm(@RequestParam("oid")Long oid, Model model){
        model.addAttribute("oid", oid);
        return "/shop/OrderConfirm";
    }

//    @RequestMapping("/loginForOrder")
//    public String loginForOrder() {
//        return "/shop/LoginForm";
//    }
    @GetMapping("/loginForOrder")
    public String loginForm() {
        return "/shop/LoginForm";
    }
}
