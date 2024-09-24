package org.farozy.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.FavoriteDto;
import org.farozy.entity.Favorite;
import org.farozy.entity.Product;
import org.farozy.entity.User;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.repository.FavoriteRepository;
import org.farozy.repository.ProductRepository;
import org.farozy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Favorite addFavorite(FavoriteDto request) {
        try {
            return addOrUpdateFavorite(null, request);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while adding favorite: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public Favorite updateFavorite(Long favoriteId, FavoriteDto request) {
        try {
            return addOrUpdateFavorite(favoriteId, request);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while updating favorite by user ID: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public List<Favorite> getFavoritesByUserId(Long userId) {
        try {
            findUserById(userId);

            List<Favorite> favorites = favoriteRepository.findByUserId(userId);

            if (favorites.isEmpty()) {
                throw new ResourceNotFoundException("Favorites not found for user ID: " + userId);
            }

            return favorites;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching favorite by user ID: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteFavorite(Long favoriteId) {
        try {
            findFavoriteById(favoriteId);

            favoriteRepository.deleteById(favoriteId);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting favorite by ID: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteFavoriteByUserId(Long userId) {
        try {
            User user = findUserById(userId);

            List<Favorite> favorites = favoriteRepository.findByUserId(user.getId());

            if (favorites.isEmpty()) {
                throw new ResourceNotFoundException("Favorites not found for user ID: " + userId);
            }

            favoriteRepository.deleteByUserId(user.getId());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting favorite by user ID: " + ex.getMessage());
        }
    }

    private Favorite addOrUpdateFavorite(Long favoriteId, FavoriteDto request) {
        Favorite favorite = favoriteId != null ? findFavoriteById(favoriteId) : new Favorite();

        User user = findUserById(request.getUserId());
        Product product = findProductById(request.getProductId());

        Favorite existingFavorite = favoriteRepository.findByUserIdAndProductId(user.getId(), product.getId());

        if (existingFavorite == null) {
            throw new ResourceNotFoundException("User ID and Product ID not found in favorite");
        }

        if (request.getStatus() != null) {
            favorite.setStatus(request.getStatus());
        }

        favorite.setUser(user);
        favorite.setProduct(product);

        return favoriteRepository.save(favorite);
    }

    private Favorite findFavoriteById(Long Id) {
        return favoriteRepository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The favorite with the specified ID does not exist"
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
