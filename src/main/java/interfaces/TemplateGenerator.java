package interfaces;

import exceptions.MissingValueException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface TemplateGenerator {

    void setTemplateAndValues() throws IOException, MissingValueException, URISyntaxException;

    default String generateOutput() throws MissingValueException {
        String output = getTemplate() != null ? getTemplate() : "";

        Pattern pattern = Pattern.compile("#\\{([^}]+)}");
        Matcher matcher = pattern.matcher(output);

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

    String getTemplate();

    Map<String, String> getVariableValues();
}
