package interfaces;

import exceptions.MissingValueException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface TemplateGenerator {

    void setTemplateAndValues() throws IOException;

    default String generateOutput() throws MissingValueException {
        String output = getTemplate();

        Pattern pattern = Pattern.compile("#\\{([^}]+)}");
        Matcher matcher = pattern.matcher(getTemplate());

        while (matcher.find()) {
            String variable = matcher.group(1);
            if (getVariableValues().containsKey(variable)) {
                String replacement = getVariableValues().get(variable);
                output = output.replace("#{" + variable + "}", replacement);
            } else {
                throw new MissingValueException("Value for variable #{" + variable + "} not provided at runtime.");
            }
        }
        if (output.matches(".*#\\{.*}.*")) {
            throw new MissingValueException("Some variable values provided at runtime are not found in the template.");
        }
        return output;
    }

    default void saveOutputToFile(String outputPath) throws IOException {
        try {
            final Path outputFile = Paths.get(outputPath);
            if (!Files.exists(outputFile)) {
                Files.createFile(outputFile);
            }
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(outputFile))) {
                writer.println(generateOutput());
            }
        } catch (IOException | MissingValueException e) {
            throw new RuntimeException("Error writing to the output file.", e);
        }
    }

    String getTemplate();

    Map<String, String> getVariableValues();
}
