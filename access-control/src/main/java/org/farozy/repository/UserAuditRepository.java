package org.farozy.repository;

import org.farozy.entity.UserAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuditRepository extends JpaRepository<UserAudit, Long> {
    List<UserAudit> findByUserId(Long userId);
}