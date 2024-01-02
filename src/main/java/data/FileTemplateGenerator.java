package data;

import exceptions.UserInputException;
import interfaces.TemplateGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileTemplateGenerator implements TemplateGenerator {

    private final Map<String, String> variableValues;
    private String template;

    public FileTemplateGenerator() {
        this.variableValues = new HashMap<>();
    }

    @Override
    public void setTemplateAndValues() throws IOException {
        System.out.println("Enter the file path for template and values:");
        String filePath = getUserInput().trim();

        StringBuilder templateBuilder = new StringBuilder();
        boolean readingValues = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if ("values".equals(line.trim())) {
                    readingValues = true;
                    continue;
                } else if ("values_end".equals(line.trim())) {
                    readingValues = false;
                    continue;
                }

                if (readingValues) {
                    String[] keyValue = line.split(":");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();
                        variableValues.put(key, value);
                    }
                } else {
                    templateBuilder.append(line)
                            .append("\n");
                }
            }
        }
        this.template = templateBuilder.toString()
                .trim();
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public Map<String, String> getVariableValues() {
        return variableValues;
    }

    public String getUserInput() {
        try {
            return new java.util.Scanner(System.in).nextLine();
        } catch (Exception e) {
            throw new UserInputException("Error while getting user input", e);
        }
    }
}
