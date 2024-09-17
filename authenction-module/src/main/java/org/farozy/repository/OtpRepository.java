package org.farozy.repository;

import org.farozy.entity.Otp;
import org.farozy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    List<Otp> findByUser(User user);

    boolean existsByOtpCode(Integer otpCode);
}
