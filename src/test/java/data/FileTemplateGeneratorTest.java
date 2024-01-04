package data;

import exceptions.MissingValueException;
import loggers.TestExecutionLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("FileTemplateGenerator Tests")
@ExtendWith(TestExecutionLogger.class)
@EnabledOnOs({OS.WINDOWS, OS.LINUX, OS.MAC})
@EnabledForJreRange(min = JRE.JAVA_9)
@ExtendWith(MockitoExtension.class)
public class FileTemplateGeneratorTest {
    @InjectMocks
    FileTemplateGenerator fileTemplateGeneratorMock;

    private static Map<String, String> createVariableValues(String... keyValuePairs) {
        Map<String, String> variableValues = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            variableValues.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }
        return variableValues;
    }

    @Test
    @Tag("positive_read_template_for_file")
    @DisplayName("Should read template and values from file")
    public void setTemplateAndValues_ShouldReadTemplateAndValuesFromFile(
            @TempDir Path tempDir) throws IOException, MissingValueException, URISyntaxException {
        final FileTemplateGenerator fileTemplateGenerator = new FileTemplateGenerator(
                "src/test/resources/valid_template_values.txt", tempDir.resolve("outPut.txt")
                .toString());

        fileTemplateGenerator.setTemplateAndValues();

        assertEquals("Hello, #{name}! My age is #{age}. I work as a #{occupation}.",
                fileTemplateGenerator.getTemplate());
        assertVariableValues(fileTemplateGenerator.getVariableValues(), "name", "John", "age", "30", "occupation",
                "Software Engineer");
    }

    @Test
    @Tag("invalid_path_for_file")
    @DisplayName("Should handle invalid file path gracefully")
    public void setTemplateAndValues_ShouldHandleInvalidFilePathGracefully(@TempDir Path tempDir) throws IOException {
        final FileTemplateGenerator fileTemplateGenerator = new FileTemplateGenerator(
                "", tempDir.resolve("outPut.txt")
                .toString());

        assertThrows(FileNotFoundException.class, fileTemplateGenerator::setTemplateAndValues);
        assertNull(fileTemplateGenerator.getTemplate());
        assertTrue(fileTemplateGenerator.getVariableValues()
                .isEmpty());
    }

    @Test
    @Tag("EmptyFileName")
    @DisplayName("Should throw IOException for empty file name")
    void saveOutputToFile_EmptyFileNameShouldThrowIOException() {
        String emptyFileName = "";

        assertThrows(IOException.class, () -> fileTemplateGeneratorMock.saveOutputToFile(emptyFileName));
    }

    @Test
    @Tag("NonEmptyFileName")
    @DisplayName("Should save output to file when file name is not empty")
    void shouldSaveOutputToFile_WhenFileNameIsNotEmpty(
            @TempDir Path tempDir) throws IOException, URISyntaxException, MissingValueException {
        String fileName = "test.txt";
        Path outputFilePath = tempDir.resolve(fileName);
        FileTemplateGenerator fileTemplateGeneratorSpy = spy(
                new FileTemplateGenerator(fileName, outputFilePath.toString()));

        fileTemplateGeneratorSpy.saveOutputToFile(fileName);

        verify(fileTemplateGeneratorSpy, times(1)).saveOutputToFile(fileName);
        verify(fileTemplateGeneratorSpy, times(1)).writeOutputToFile(any());
    }

    private void assertVariableValues(Map<String, String> actual, String... expectedPairs) {
        Map<String, String> expected = createVariableValues(expectedPairs);
        assertEquals(expected, actual);
    }
}
