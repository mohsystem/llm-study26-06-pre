package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.entity.User;

public interface CurrentUserService {
    User requireUserByBearerToken(String authorizationHeader);
}
