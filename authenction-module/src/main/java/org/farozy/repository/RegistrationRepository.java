package org.farozy.repository;

import org.farozy.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByUserId(Long userId);

    void deleteByUserId(Long id);
}
