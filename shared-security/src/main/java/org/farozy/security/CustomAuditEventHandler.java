package org.farozy.security;

import org.farozy.entity.User;
import org.farozy.entity.UserAudit;
import org.farozy.repository.UserAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CustomAuditEventHandler {

    private final UserAuditRepository userAuditRepository;

    @Autowired
    public CustomAuditEventHandler(UserAuditRepository userAuditRepository) {
        this.userAuditRepository = userAuditRepository;
    }

    @EventListener
    public void handleAuthenticationEvent(AbstractAuthenticationEvent authEvent) {
        if (authEvent.getAuthentication() != null &&
                authEvent.getAuthentication().getPrincipal() instanceof User user) {
            UserAudit audit = new UserAudit();
            audit.setUserId(user.getId());
            audit.setAction(authEvent.getClass().getSimpleName());
            audit.setDescription(authEvent.getAuthentication().getDetails().toString());
            audit.setTimestamp(LocalDateTime.now());
            userAuditRepository.save(audit);
        }
    }
}
