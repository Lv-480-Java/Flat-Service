package com.softserve.maklertaboo.service;

import com.softserve.maklertaboo.constant.ErrorMessage;
import com.softserve.maklertaboo.entity.flat.FavoriteFlat;
import com.softserve.maklertaboo.entity.flat.Flat;
import com.softserve.maklertaboo.entity.user.User;
import com.softserve.maklertaboo.exception.exceptions.FavoriteListIsEmptyException;
import com.softserve.maklertaboo.exception.exceptions.FlatAlreadyInTheFavoriteListException;
import com.softserve.maklertaboo.exception.exceptions.FlatNotFoundException;
import com.softserve.maklertaboo.exception.exceptions.UserNotFoundException;
import com.softserve.maklertaboo.repository.FavoriteFlatRepository;
import com.softserve.maklertaboo.repository.FlatRepository;
import com.softserve.maklertaboo.repository.user.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class FavoriteFlatService {

    private final FavoriteFlatRepository favoriteFlatRepository;
    private final FlatRepository flatRepository;
    private final UserRepository userRepository;

    @Autowired
    public FavoriteFlatService(FavoriteFlatRepository favoriteFlatRepository,
                               FlatRepository flatRepository,
                               UserRepository userRepository) {
        this.favoriteFlatRepository = favoriteFlatRepository;
        this.flatRepository = flatRepository;
        this.userRepository = userRepository;
    }



    public void saveFavoriteFlat(Long id, String email) {
        FavoriteFlat favoriteFlat = new FavoriteFlat();
        Flat flat = flatRepository.findById(id).orElse(null);
        if (flat != null) {
            favoriteFlat.setFlat(flat);
        } else {
            throw new FlatNotFoundException(ErrorMessage.FLAT_NOT_FOUND);
        }

        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            favoriteFlat.setUser(user);
        } else {
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        favoriteFlat.setActive(true);

        if (!(user.getFavoriteFlats().contains(favoriteFlat))) {
            user.getFavoriteFlats().add(favoriteFlat);
            userRepository.save(user);
        } else {
            throw new FlatAlreadyInTheFavoriteListException(
                    ErrorMessage.FLAT_ALREADY_IN_THE_FAVORITE_LIST);
        }
    }

    public List<FavoriteFlat> getAllFavoriteFlatsOfUser(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        List<FavoriteFlat> favoriteFlats = user.getFavoriteFlats();
        if (favoriteFlats == null || favoriteFlats.size() == 0) {
            throw new FavoriteListIsEmptyException(ErrorMessage.EMPTY_FAVORITE_LIST);
        }
        return favoriteFlats
                .stream()
                .filter(FavoriteFlat::isActive)
                .collect(Collectors.toList());
    }

    public void deactivateFlat(Long id, String email) {
        FavoriteFlat favoriteFlat = new FavoriteFlat();
        Flat flat = flatRepository.findById(id).orElse(null);
        if (flat != null) {
            favoriteFlat.setFlat(flat);
        } else {
            throw new FlatNotFoundException(ErrorMessage.FLAT_NOT_FOUND);
        }

        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            favoriteFlat.setUser(user);
        } else {
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        if (user.getFavoriteFlats().contains(favoriteFlat)) {
            user.getFavoriteFlats().remove(favoriteFlat);
            userRepository.save(user);
        } else {
            throw new FlatAlreadyInTheFavoriteListException(
                    ErrorMessage.FLAT_ALREADY_IN_THE_FAVORITE_LIST);
        }
    }
}