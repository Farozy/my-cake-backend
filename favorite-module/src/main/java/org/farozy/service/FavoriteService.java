package org.farozy.service;

import org.farozy.dto.FavoriteDto;
import org.farozy.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    Favorite addFavorite(FavoriteDto request);

    Favorite updateFavorite(Long favoriteId, FavoriteDto request);

    List<Favorite> getFavoritesByUserId(Long userId);

    void deleteFavorite(Long favoriteId);

    void deleteFavoriteByUserId(Long userId);
}
