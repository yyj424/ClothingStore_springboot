package com.yyj.wylie_yyj_springboot.service;

import com.yyj.wylie_yyj_springboot.domain.entity.Cart;
import com.yyj.wylie_yyj_springboot.domain.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public void saveCart(Cart cart) {
        cartRepository.saveAndFlush(cart);
    }
    public List<Cart> getCart(String uid) { return cartRepository.findAllByUid(uid); }
    public void deleteCart(Long cid) { cartRepository.deleteById(cid); }
    public Cart getCart(Long oid, String uid) { return cartRepository.getCartByOidAndUid(oid, uid); }
    public Cart getCartById(Long cid) { return cartRepository.getById(cid); }
}
