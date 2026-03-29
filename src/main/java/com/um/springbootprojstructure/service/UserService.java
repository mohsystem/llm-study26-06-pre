package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.UserDto;
import com.um.springbootprojstructure.entity.AccountStatus;
import com.um.springbootprojstructure.entity.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);
    UserDto getById(Long id);
    List<UserDto> getAll();
    UserDto update(Long id, UserDto userDto);
    void delete(Long id);

    Page<UserDto> list(Role role, AccountStatus status, int page, int size);
}
