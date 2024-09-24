package org.farozy.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.CartDto;
import org.farozy.entity.Cart;
import org.farozy.entity.Product;
import org.farozy.entity.User;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.CartRepository;
import org.farozy.repository.ProductRepository;
import org.farozy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public List<Cart> findAll() {
        try {
            return cartRepository.findAll();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching all cart: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public Cart addCart(CartDto request) {
        try {
            return addOrUpdateCart(null, request);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while saving cart: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public List<Cart> getCartsByUserId(Long userId) {
        try {
            User user = findUserById(userId);

            List<Cart> carts = cartRepository.findByUserId(user.getId());

            if (carts.isEmpty()) {
                throw new ResourceNotFoundException("No carts found for user ID: " + user.getId());
            }

            return carts;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching cart by user ID: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public Cart getCartById(Long cartId) {
        try {
            return findCartById(cartId);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching cart by ID: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {
        try {
            getCartById(cartId);

            cartRepository.deleteById(cartId);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting cart by ID: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteCartByUserId(Long userId) {
        try {
            User user = findUserById(userId);

            List<Cart> cart = cartRepository.findByUserId(user.getId());

            if (cart.isEmpty()) {
                throw new ResourceNotFoundException("No carts found for user ID: " + user.getId());
            }

            cartRepository.deleteByUserId(user.getId());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting cart by user ID: " + ex.getMessage());
        }
    }

    public Cart updateCart(Long cartId, CartDto request) {
        try {
            findCartById(cartId);

            return addOrUpdateCart(cartId, request);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while updating cart: " + ex.getMessage());
        }
    }

    private Cart addOrUpdateCart(Long cartId, CartDto request) {
        Cart cart = cartId != null ? findCartById(cartId) : new Cart();
        int totalPrice = request.getQuantity() * request.getPrice();

        User user = findUserById(request.getUserId());
        Product product = findProductById(request.getProductId());

        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(request.getQuantity());
        cart.setPrice(request.getPrice());
        cart.setTotalPrice(BigDecimal.valueOf(totalPrice));
        cart.setStatus(request.getStatus() != null ? request.getStatus() : "PENDING");

        return cartRepository.save(cart);
    }

    private Cart findCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The cart with the specified ID does not exist"
                ));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The user with the specified ID does not exist"
                ));
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The product with the specified ID does not exist"
                ));
    }

}
