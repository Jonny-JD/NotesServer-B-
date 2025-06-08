package com.jimmy_d.notes_backend.validation;

import com.jimmy_d.notes_backend.database.entity.Role;
import com.jimmy_d.notes_backend.validation.annotation.UserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleValidator implements ConstraintValidator<UserRole, Set<String>> {
    @Override
    public boolean isValid(Set<String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return new HashSet<>(Arrays.stream(Role.values())
                .map(Enum::toString)
                .collect(Collectors.toSet())).containsAll(value);
    }
}
