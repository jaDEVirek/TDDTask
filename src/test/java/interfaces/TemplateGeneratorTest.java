package interfaces;

import exceptions.MissingValueException;
import loggers.TestExecutionLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(TestExecutionLogger.class)
@EnabledOnOs({OS.WINDOWS, OS.LINUX, OS.MAC})
@EnabledForJreRange(min = JRE.JAVA_9)
public class TemplateGeneratorTest {

    private TemplateGenerator templateGenerator;

    private static Stream<Arguments> generateOutputTestData() {
        return Stream.of(
                Arguments.of("The #{animal} jumped over the #{obstacle}.",
                        createVariableValues("animal", "fox", "obstacle", "fence"), "The fox jumped over the fence."),
                Arguments.of("In the #{time} #{day}, #{name} #{action}.",
                        createVariableValues("time", "morning", "day", "Monday", "name", "Alice", "action",
                                "went to work"), "In the morning Monday, Alice went to work."),
                Arguments.of("#{name}'s favorite #{color} is #{favoriteColor}.",
                        createVariableValues("name", "Bob", "color", "color", "favoriteColor", "blue"),
                        "Bob's favorite color is blue.")
        );
    }

    private static Map<String, String> createVariableValues(String... keyValuePairs) {
        Map<String, String> variableValues = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            variableValues.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }
        return variableValues;
    }

    @BeforeEach
    public void setUp() {
        templateGenerator = mock(TemplateGenerator.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    @Tag("output_with_replaced_variables")
    public void generateOutput_ShouldGenerateOutputWithReplacedVariables() throws MissingValueException {
        String template = "Hello, #{name}!";
        Map<String, String> variableValues = new HashMap<>();
        variableValues.put("name", "John");
        when(templateGenerator.getTemplate()).thenReturn(template);
        when(templateGenerator.getVariableValues()).thenReturn(variableValues);

        String output = templateGenerator.generateOutput();

        assertEquals("Hello, John!", output);
    }

    @Test
    @Tag("missing_values_in_template")
    public void generateOutput_ShouldThrowMissingValueException_WhenVariableValueNotProvided() {
        String template = "Hello, #{name}!";
        Map<String, String> variableValues = new HashMap<>();
        when(templateGenerator.getTemplate()).thenReturn(template);
        when(templateGenerator.getVariableValues()).thenReturn(variableValues);

        assertThrows(MissingValueException.class, () -> templateGenerator.generateOutput());
    }

    @Test
    @Tag("missing_some_values_in_template")
    public void generateOutput_ShouldThrowMissingValueException_WhenSomeVariableValuesNotProvided() {
        String template = "Hello, #{name}! My age is #{age}.";
        Map<String, String> variableValues = new HashMap<>();
        variableValues.put("name", "John");
        when(templateGenerator.getTemplate()).thenReturn(template);
        when(templateGenerator.getVariableValues()).thenReturn(variableValues);

        assertThrows(MissingValueException.class, () -> templateGenerator.generateOutput());
    }

    @ParameterizedTest
    @MethodSource("generateOutputTestData")
    public void generateOutput_ShouldReturnExpectedOutput_WhenVariableValueProvided(String sentence,
            Map<String, String> variableValues, String expectedOutput) throws MissingValueException {
        when(templateGenerator.getTemplate()).thenReturn(sentence);
        when(templateGenerator.getVariableValues()).thenReturn(variableValues);

        String output = templateGenerator.generateOutput();

        assertNotNull(output);
        assertEquals(expectedOutput, output);
    }
}
