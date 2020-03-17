package com.appsdeveloperblog.app.ws.mobileappws.userservice;

import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.UserDetailRequestModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.UserRest;

public interface UserService {

    UserRest createUser(UserDetailRequestModel userDetailRequestModel);
}
