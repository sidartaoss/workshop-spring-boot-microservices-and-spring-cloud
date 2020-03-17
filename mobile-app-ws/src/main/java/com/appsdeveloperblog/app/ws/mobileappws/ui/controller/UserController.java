package com.appsdeveloperblog.app.ws.mobileappws.ui.controller;

import com.appsdeveloperblog.app.ws.mobileappws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.UpdateUserDetailRequestModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.UserRest;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.UserDetailRequestModel;
import com.appsdeveloperblog.app.ws.mobileappws.userservice.UserService;
import com.appsdeveloperblog.app.ws.mobileappws.userservice.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("users")        // http://localhost:8080/users/
public class UserController {

    private Map<String, UserRest> users;

    @Autowired
    private UserService userService;

//    @GetMapping(path = "/{userId}", produces = {
//            MediaType.APPLICATION_XML_VALUE,
//            MediaType.APPLICATION_JSON_VALUE
//            }
//    )
//    public UserRest getUser(@PathVariable String userId) {
////        return "get user was called with userId = " + userId;
//        UserRest userRest = new UserRest();
//        userRest.setEmail("sidarta@silva.com");
//        userRest.setFirstName("Sidarta");
//        userRest.setLastName("Silva");
//        return userRest;
//    }

    @GetMapping(path = "/{userId}", produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    }
    )
    public ResponseEntity<UserRest> getUser(@PathVariable String userId) {
//        return "get user was called with userId = " + userId;
//        UserRest userRest = new UserRest();
//        userRest.setEmail("sidarta@silva.com");
//        userRest.setFirstName("Sidarta");
//        userRest.setLastName("Silva");

        if (true) {
            throw new UserServiceException("A user service is thrown");
        }

        if (users.containsKey(userId)) {
            return new ResponseEntity<UserRest>(users.get(userId), HttpStatus.OK);
        }
        return new ResponseEntity<UserRest>(HttpStatus.NOT_FOUND);
//        return new ResponseEntity<UserRest>(userRest, HttpStatus.OK);
//        return new ResponseEntity<UserRest>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public String getUsers(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                           @RequestParam(value = "limit", defaultValue = "50") int limit,
                           @RequestParam(value = "sort", defaultValue = "desc", required = false) String sort) {
          return "get users was called with page= " + page + " and limit = " + limit + " and sort = " + sort;
    }

    @PostMapping(consumes = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    },
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<UserRest> createUser(@Valid @RequestBody UserDetailRequestModel userDetailRequestModel) {
//        return "create user was called";
/*        UserRest userRest = new UserRest();
        userRest.setEmail(userDetailRequestModel.getEmail());
        userRest.setFirstName(userDetailRequestModel.getFirstName());
        userRest.setLastName(userDetailRequestModel.getLastName());

        String userId = UUID.randomUUID().toString();
        userRest.setUserId(userId);

        if (users == null) {
            users = new HashMap<>();
        }

        users.put(userId, userRest);*/


//        UserRest userRest = new UserServiceImpl().createUser(userDetailRequestModel);
        UserRest userRest = this.userService.createUser(userDetailRequestModel);
        return new ResponseEntity<UserRest>(userRest, HttpStatus.OK);
    }

    @PutMapping(path = "/{userId}",
            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserRest updateUser(@PathVariable String userId, @Valid @RequestBody UpdateUserDetailRequestModel userDetailRequestModel) {
//        return "update user was called";
        UserRest storedUserRest = users.get(userId);
        storedUserRest.setFirstName(userDetailRequestModel.getFirstName());
        storedUserRest.setLastName(userDetailRequestModel.getLastName());
        users.put(userId, storedUserRest);
        return storedUserRest;
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
//        return "delete user was called";
        users.remove(userId);
        return ResponseEntity.noContent().build();
    }
}
