package org.farozy.repository;

import org.farozy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByWhatsappNumber(String phone);

    Optional<User> findByEmailOrWhatsappNumber(String email, String whatsappNumber);

}