package com.um.springbootprojstructure.mapper;

import com.um.springbootprojstructure.dto.UserDto;
import com.um.springbootprojstructure.entity.User;

public final class UserMapper {

    private UserMapper() {}

    public static UserDto toDto(User user) {
        if (user == null) return null;
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.isActive()
        );
    }
}
