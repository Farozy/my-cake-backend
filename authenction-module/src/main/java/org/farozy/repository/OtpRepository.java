package org.farozy.repository;

import org.farozy.entity.Otp;
import org.farozy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    List<Otp> findByUser(User user);

    boolean existsByOtpCode(Integer otpCode);
}
