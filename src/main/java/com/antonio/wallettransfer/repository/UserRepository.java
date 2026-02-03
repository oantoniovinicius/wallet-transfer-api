package com.antonio.wallettransfer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.antonio.wallettransfer.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCpfCnpj(String document);

    Optional<User> findByEmail(String email);
}
