package org.farozy.repository;

import org.farozy.entity.Otp;
import org.farozy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    List<Otp> findByUser(User user);

    boolean existsByOtpCode(Integer otpCode);

    Optional<Otp> findByOtpCode(Integer otpCode);
}
