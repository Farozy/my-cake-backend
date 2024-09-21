package org.farozy.repository;

import org.farozy.entity.OtpSendLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpSendLogRepository extends JpaRepository<OtpSendLog, Long> {
    Optional<OtpSendLog> findByEmailAndLastSentBetween(String email, LocalDateTime start, LocalDateTime end);
    Optional<OtpSendLog> findByWhatsappNumberAndLastSentBetween(String whatsappNumber, LocalDateTime start, LocalDateTime end);
}
