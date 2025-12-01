package com.buizzment.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkerCreateDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testOrgIdsValidation() {
        WorkerCreateDto dto = WorkerCreateDto.builder()
                .name("Test Worker")
                .uanNumber("123456789012")
                .contactNumber("9876543210")
                .orgIds(Set.of("org1"))
                .build();

        Set<ConstraintViolation<WorkerCreateDto>> violations = validator.validate(dto);

        // This should fail with UnexpectedTypeException if @NotBlank is used on Set
        // Or if we just check for violations, we might see it there depending on
        // validator impl
        // But for unit test, we just want to ensure it validates correctly when fixed.
        // If it throws exception, the test fails, which is what we want for
        // reproduction.

        assertTrue(violations.isEmpty(), "Validation should pass for valid DTO");
    }
}
