package com.jimmy_d.notes_backend.validation;

import com.jimmy_d.notes_backend.validation.annotation.NoteContent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class NoteContentValidator implements ConstraintValidator<NoteContent, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && !value.trim().isEmpty();
    }

}
