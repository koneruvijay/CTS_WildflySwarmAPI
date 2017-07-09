package com.cognizant.wildflyswarmapi.service;

import com.cognizant.wildflyswarmapi.model.User;

import java.util.List;

/**
 * Created by Koneru on 7/6/17.
 */
public interface UserService {
    User findById(long id);

    User findByName(String name);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUserById(long id);

    List<User> findAllUsers();

    void deleteAllUsers();

    boolean isUserExist(User user);
}
