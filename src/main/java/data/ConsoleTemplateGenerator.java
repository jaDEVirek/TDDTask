package data;

import interfaces.TemplateGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleTemplateGenerator implements TemplateGenerator {

    private final Map<String, String> variableValues;
    private String template;

    public ConsoleTemplateGenerator() {
        this.variableValues = new HashMap<>();
    }

    @Override
    public void setTemplateAndValues() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the template:");
        this.template = scanner.nextLine()
                .trim();

        System.out.println("Enter values (type 'values_end' to finish):");
        while (true) {
            String line = scanner.nextLine()
                    .trim();
            if ("values_end".equals(line)) {
                break;
            }

            String[] keyValue = line.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                variableValues.put(key, value);
            }
        }
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public Map<String, String> getVariableValues() {
        return variableValues;
    }
}