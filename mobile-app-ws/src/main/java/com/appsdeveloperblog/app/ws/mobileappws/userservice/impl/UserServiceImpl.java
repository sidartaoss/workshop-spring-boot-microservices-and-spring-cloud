package com.appsdeveloperblog.app.ws.mobileappws.userservice.impl;

import com.appsdeveloperblog.app.ws.mobileappws.shared.Utils;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.UserDetailRequestModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.UserRest;
import com.appsdeveloperblog.app.ws.mobileappws.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private Map<Object, Object> users;
    private Utils utils;

    public UserServiceImpl() {}

    @Autowired
    public UserServiceImpl(Utils utils) {
        this.utils = utils;
    }

    @Override
    public UserRest createUser(UserDetailRequestModel userDetailRequestModel) {
        UserRest userRest = new UserRest();
        userRest.setEmail(userDetailRequestModel.getEmail());
        userRest.setFirstName(userDetailRequestModel.getFirstName());
        userRest.setLastName(userDetailRequestModel.getLastName());

//        String userId = UUID.randomUUID().toString();
        String userId = utils.generateUserId();
        userRest.setUserId(userId);

        if (users == null) {
            users = new HashMap<>();
        }

        users.put(userId, userRest);

        return userRest;
    }
}
