package com.shimady.ticketbookingapp.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordValidatorTest {

    private final PasswordValidator passwordValidator = new PasswordValidator();

    @Mock
    private Password password;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    public void setUp() {
        passwordValidator.initialize(password);
    }

    @Test
    public void shouldValidateValidPassword() {
        String password = "Strongpassword@69";

        boolean valid = passwordValidator.isValid(password, constraintValidatorContext);

        assertThat(valid).isTrue();
    }

    @Test
    public void shouldInvalidateShortPassword() {
        String password = "Str69@";

        boolean valid = passwordValidator.isValid(password, constraintValidatorContext);

        assertThat(valid).isFalse();
    }

    @Test
    public void shouldInvalidatePasswordWithoutDigit() {
        String password = "Strongp@assword";

        boolean valid = passwordValidator.isValid(password, constraintValidatorContext);

        assertThat(valid).isFalse();
    }

    @Test
    public void shouldInvalidatePasswordWithoutSpecialCharacter() {
        String password = "2Strong1password3";

        boolean valid = passwordValidator.isValid(password, constraintValidatorContext);

        assertThat(valid).isFalse();
    }

    @Test
    public void shouldInvalidatePasswordWithoutLoweCaseLetter() {
        String password = "1STRONG2PASS@WORD3";

        boolean valid = passwordValidator.isValid(password, constraintValidatorContext);

        assertThat(valid).isFalse();
    }

    @Test
    public void shouldInvalidatePasswordWithoutUpperCaseLetter() {
        String password = "1strong2pass@word3";

        boolean valid = passwordValidator.isValid(password, constraintValidatorContext);

        assertThat(valid).isFalse();
    }
}
