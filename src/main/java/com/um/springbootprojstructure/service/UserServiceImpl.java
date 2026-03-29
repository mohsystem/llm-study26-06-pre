package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.UserDto;
import com.um.springbootprojstructure.entity.AccountStatus;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.mapper.UserMapper;
import com.um.springbootprojstructure.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto create(UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getUsername().isBlank()) {
            throw new IllegalArgumentException("username is required");
        }
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("email is required");
        }
        if (userDto.getFullName() == null || userDto.getFullName().isBlank()) {
            throw new IllegalArgumentException("fullName is required");
        }

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("username already exists");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("email already exists");
        }

        User toSave = UserMapper.toEntity(userDto);
        toSave.setId(null); // ensure create
        User saved = userRepository.save(toSave);
        return UserMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user not found: " + id));
        return UserMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserMapper::toDto).toList();
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user not found: " + id));

        if (userDto.getUsername() != null && !userDto.getUsername().isBlank()
                && !userDto.getUsername().equals(existing.getUsername())
                && userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("username already exists");
        }

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()
                && !userDto.getEmail().equals(existing.getEmail())
                && userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("email already exists");
        }

        if (userDto.getUsername() != null && !userDto.getUsername().isBlank()) {
            existing.setUsername(userDto.getUsername());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            existing.setEmail(userDto.getEmail());
        }
        if (userDto.getFullName() != null && !userDto.getFullName().isBlank()) {
            existing.setFullName(userDto.getFullName());
        }
        existing.setActive(userDto.isActive());

        User saved = userRepository.save(existing);
        return UserMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("user not found: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> list(Role role, AccountStatus status, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = (size <= 0) ? 20 : Math.min(size, 200);
        Pageable pageable = PageRequest.of(safePage, safeSize);

        if (role != null && status != null) {
            boolean active = (status == AccountStatus.ACTIVE);
            return userRepository.findAllByRoleAndActive(role, active, pageable).map(UserMapper::toDto);
        }
        if (role != null) {
            return userRepository.findAllByRole(role, pageable).map(UserMapper::toDto);
        }
        if (status != null) {
            boolean active = (status == AccountStatus.ACTIVE);
            return userRepository.findAllByActive(active, pageable).map(UserMapper::toDto);
        }
        return userRepository.findAll(pageable).map(UserMapper::toDto);
    }
}
