package org.farozy.service;

import org.farozy.dto.CartDto;
import org.farozy.entity.Cart;

import java.util.List;

public interface CartService {

    List<Cart> findAll();

    Cart addCart(CartDto cart);

    List<Cart> getCartsByUserId(Long userId);

    Cart getCartById(Long id);

    void deleteCart(Long cartId);

    void deleteCartByUserId(Long userId);

    Cart updateCart(Long cartId, CartDto request);

}
