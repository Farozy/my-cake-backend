package org.farozy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.CartDto;
import org.farozy.entity.Cart;
import org.farozy.helper.ResponseHelper;
import org.farozy.payload.ApiResponse;
import org.farozy.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Cart>>> getAll() {
        List<Cart> carts = cartService.findAll();
        String message = "Fetching carts successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, carts);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Cart>> addCart(@Valid @RequestBody CartDto cart) {
        Cart savedCart = cartService.addCart(cart);
        String message = "Saved cart successfully";

        return ResponseHelper.buildResponseData(HttpStatus.CREATED, message, savedCart);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Cart>>> getCartByUserId(@PathVariable Long userId) {
        List<Cart> carts = cartService.getCartsByUserId(userId);
        String message = "Fetching carts by user ID successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, carts);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Cart>> getCartById(@PathVariable Long cartId) {
        Cart cart = cartService.getCartById(cartId);
        String message = "Fetching cart by ID successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, cart);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Cart>> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        String message = "Deleted cart by ID successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, null);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Cart>> deleteCartByUserId(@PathVariable Long userId) {
        cartService.deleteCartByUserId(userId);
        String message = "Deleted cart by user ID successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, null);
    }

    @PatchMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Cart>> updateCart(@PathVariable Long cartId, @Valid @RequestBody CartDto request) {
        Cart updatedCart = cartService.updateCart(cartId, request);
        String message = "Updated cart successfully";

        return ResponseHelper.buildResponseData(HttpStatus.OK, message, updatedCart);
    }

}
