package com.example.online_shop.audit;

import com.example.online_shop.constants.RoleConstants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    /**
     * @return the current auditor or "System" if no authenticated user
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal()) ) {
            return Optional.of("System");
        }
        // Перевірка, чи є у користувача роль "ADMIN"
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(RoleConstants.ADMIN_ROLE))) {
            return Optional.of("System");
        }

        return Optional.ofNullable(authentication.getName());
    }
}
