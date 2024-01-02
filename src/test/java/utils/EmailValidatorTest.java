package utils;


import exceptions.InvalidEmailException;
import loggers.TestExecutionLogger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;


@ExtendWith(TestExecutionLogger.class)
@EnabledOnOs({OS.WINDOWS, OS.LINUX, OS.MAC})
@EnabledForJreRange(min = JRE.JAVA_9)
public class EmailValidatorTest {

    @Rule
    @SuppressWarnings("deprecation")
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    @Tag("invalid_email_exception")
    public void testInvalidEmailException() throws InvalidEmailException {
        String invalidEmailAddress = "invalid.email";
        exceptionRule.expect(InvalidEmailException.class);
        exceptionRule.expectMessage("Invalid email address: " + invalidEmailAddress);

        EmailValidator.isValidEmail(invalidEmailAddress);
    }

    @Test
    @Tag("invalid_email_exception_for_assertions")
    public void testInvalidEmailExceptionWithAssertions() {
        String invalidEmailAddress = "invalid.email";

        InvalidEmailException exception = Assertions.assertThrows(InvalidEmailException.class, () -> {
            EmailValidator.isValidEmail("invalid.email");
        });

        Assertions.assertTrue(exception.getMessage()
                .contains("Invalid email address: " + invalidEmailAddress));
    }

    @Test
    @Tag("valid_email")
    public void testValidEmail() throws InvalidEmailException {
        String validEmailAddress = "wiktorr.email@example.com";

        assertTrue(EmailValidator.isValidEmail(validEmailAddress));
    }
}