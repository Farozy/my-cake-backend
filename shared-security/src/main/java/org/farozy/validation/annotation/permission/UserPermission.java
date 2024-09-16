package org.farozy.validation.annotation.permission;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class UserPermission {

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'USER_READ')")
    public @interface UserRead {
    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'USER_CREATE')")
    public @interface UserCreate {
    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'USER_UPDATE')")
    public @interface UserUpdate {
    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'USER_DELETE')")
    public @interface UserDelete {
    }

}
