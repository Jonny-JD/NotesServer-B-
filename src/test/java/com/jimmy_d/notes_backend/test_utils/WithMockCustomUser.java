package com.jimmy_d.notes_backend.test_utils;

import com.jimmy_d.notes_backend.database.entity.Role;
import org.springframework.security.test.context.support.WithSecurityContext;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserFactory.class)
public @interface WithMockCustomUser {
    long id() default 1L;
    String username() default "dummy_user";
    String password() default "dummy_pass";
    String email() default "dummy@mail.com";
    Role[] authorities() default {Role.USER};
}