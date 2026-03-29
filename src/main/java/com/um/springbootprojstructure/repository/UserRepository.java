package com.um.springbootprojstructure.repository;

import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Page<User> findAllByRole(Role role, Pageable pageable);
    Page<User> findAllByActive(boolean active, Pageable pageable);
    Page<User> findAllByRoleAndActive(Role role, boolean active, Pageable pageable);

    Optional<User> findByPublicRef(String publicRef);
}
