package data;

import exceptions.MissingValueException;
import exceptions.UserInputException;
import interfaces.TemplateGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileTemplateGenerator implements TemplateGenerator {

    private final Map<String, String> variableValues;
    private final String outPutFile;
    private final String inputFile;
    private String template;

    public FileTemplateGenerator(String inputName, String outputName) {
        this.variableValues = new HashMap<>();
        this.inputFile = inputName;
        this.outPutFile = outputName;
    }

    @Override
    public void setTemplateAndValues() throws IOException, MissingValueException, URISyntaxException {
        StringBuilder templateBuilder = new StringBuilder();
        boolean readingValues = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(this.inputFile))) {
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
        this.saveOutputToFile(this.outPutFile);
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

    void saveOutputToFile(String fileName) throws IOException, MissingValueException, URISyntaxException {
        if (fileName.isEmpty()) {
            throw new IOException("File name cannot be empty.");
        }
        Path outputFilePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources")
                .resolve(fileName);
        Files.deleteIfExists(outputFilePath);
        Files.createFile(outputFilePath);

        writeOutputToFile(outputFilePath);
    }

    void writeOutputToFile(Path filePath) throws IOException, MissingValueException {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.println(generateOutput());
        }
    }
}
