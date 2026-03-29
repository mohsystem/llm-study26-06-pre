package com.um.springbootprojstructure.repository;

import com.um.springbootprojstructure.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    List<ApiKey> findAllByUser_IdOrderByCreatedAtDesc(Long userId);
    Optional<ApiKey> findByIdAndUser_Id(Long id, Long userId);
}
