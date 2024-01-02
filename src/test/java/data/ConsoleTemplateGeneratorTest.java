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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;


@DisplayName("ConsoleTemplateGenerator Tests")
@ExtendWith(TestExecutionLogger.class)
@EnabledOnOs({OS.WINDOWS, OS.LINUX, OS.MAC})
@EnabledForJreRange(min = JRE.JAVA_9)
public class ConsoleTemplateGeneratorTest {

    private ConsoleTemplateGenerator consoleTemplateGenerator;

    private static Map<String, String> createVariableValues(String... keyValuePairs) {
        Map<String, String> variableValues = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            variableValues.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }
        return variableValues;
    }

    @BeforeEach
    public void setUp() {
        consoleTemplateGenerator = spy(new ConsoleTemplateGenerator());
    }

    @Test
    @Tag("positive_read_template_for_console")
    @DisplayName("Should set template and values from user input")
    public void setTemplateAndValues_ShouldSetTemplateAndValuesFromUserInput() {
        mockUserInput("Hello, my name is  #{name}  and Iam #{age} old. !\nname:John\nage:30\nvalues_end");

        consoleTemplateGenerator.setTemplateAndValues();

        assertEquals("Hello, my name is  #{name}  and Iam #{age} old. !", consoleTemplateGenerator.getTemplate());
        assertVariableValues(consoleTemplateGenerator.getVariableValues(), "name", "John", "age", "30");
    }

    @Test
    @Tag("positive_handle_values_for_console")
    @DisplayName("Should handle 'values_end' gracefully")
    public void setTemplateAndValues_ShouldHandleValuesEndGracefully() {
        mockUserInput("Hello, #{name}!\nvalues_end");

        consoleTemplateGenerator.setTemplateAndValues();

        assertEquals("Hello, #{name}!", consoleTemplateGenerator.getTemplate());
        assertTrue(consoleTemplateGenerator.getVariableValues()
                .isEmpty());
    }

    private void mockUserInput(String userInput) {
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
    }

    private void assertVariableValues(Map<String, String> actual, String... expectedPairs) {
        Map<String, String> expected = createVariableValues(expectedPairs);
        assertEquals(expected, actual);
    }
}