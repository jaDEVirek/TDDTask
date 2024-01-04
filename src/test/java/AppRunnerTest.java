import exceptions.InvalidFileArgumentsException;
import loggers.TestExecutionLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(TestExecutionLogger.class)
@EnabledOnOs({OS.WINDOWS, OS.LINUX, OS.MAC})
@EnabledForJreRange(min = JRE.JAVA_9)
class AppRunnerTest {

    @ParameterizedTest
    @CsvSource({
            "file1.txt, file2.txt",
            "fileA.txt, fileB.txt"
    })
    @Tag("ValidArguments")
    @DisplayName("Validating file arguments with valid arguments should not throw an exception")
    void validateFileArguments_WithValidArguments_ShouldNotThrowException(String fileName1, String fileName2) {
        String[] validArgs = {fileName1, fileName2};
        assertDoesNotThrow(() -> AppRunner.validateFileArguments(validArgs));
    }

    @Test
    @Tag("InvalidNumberOfArguments")
    @DisplayName("Validating file arguments with invalid number of arguments should throw an exception")
    void validateFileArguments_WithInvalidNumberOfArguments_ShouldThrowException() {
        String singleName = "input.txt";
        assertThrows(InvalidFileArgumentsException.class,
                () -> AppRunner.validateFileArguments(new String[]{singleName}));
    }

    @ParameterizedTest
    @CsvSource({
            "file1.txt, file2.jpg",
            "file1.txt, file2.mp3"
    })
    @Tag("InvalidFileExtension")
    @DisplayName("Validating file arguments with invalid file extension should throw an exception")
    void validateFileArguments_WithInvalidFileExtension_ShouldThrowException(String fileName1, String fileName2) {
        assertThrows(InvalidFileArgumentsException.class,
                () -> AppRunner.validateFileArguments(new String[]{fileName1, fileName2}));
    }

}