package data;

import loggers.TestExecutionLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@DisplayName("FileTemplateGenerator Tests")
@ExtendWith(TestExecutionLogger.class)
@EnabledOnOs({OS.WINDOWS, OS.LINUX, OS.MAC})
@EnabledForJreRange(min = JRE.JAVA_9)
public class FileTemplateGeneratorTest {

    private FileTemplateGenerator fileTemplateGenerator;

    private static Map<String, String> createVariableValues(String... keyValuePairs) {
        Map<String, String> variableValues = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            variableValues.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }
        return variableValues;
    }

    @BeforeEach
    public void setUp() {
        fileTemplateGenerator = spy(new FileTemplateGenerator());
    }

    @Test
    @Tag("positive_read_template_for_file")
    @DisplayName("Should read template and values from file")
    public void setTemplateAndValues_ShouldReadTemplateAndValuesFromFile() throws IOException {
        mockUserInput("src/test/resources/valid_template_values.txt");

        fileTemplateGenerator.setTemplateAndValues();

        assertEquals("Hello, #{name}! My age is #{age}. I work as a #{occupation}.",
                fileTemplateGenerator.getTemplate());
        assertVariableValues(fileTemplateGenerator.getVariableValues(), "name", "John", "age", "30", "occupation",
                "Software Engineer");
    }

    @Test
    @Tag("invalid_path_for_file")
    @DisplayName("Should handle invalid file path gracefully")
    public void setTemplateAndValues_ShouldHandleInvalidFilePathGracefully() throws IOException {
        mockUserInput("nonexistent_file.txt");

        assertThrows(FileNotFoundException.class, () -> fileTemplateGenerator.setTemplateAndValues());
        assertNull(fileTemplateGenerator.getTemplate());
        assertTrue(fileTemplateGenerator.getVariableValues()
                .isEmpty());
    }

    private void mockUserInput(String filePath) {
        System.setIn(new ByteArrayInputStream(filePath.getBytes()));
    }

    private void assertVariableValues(Map<String, String> actual, String... expectedPairs) {
        Map<String, String> expected = createVariableValues(expectedPairs);
        assertEquals(expected, actual);
    }
}
