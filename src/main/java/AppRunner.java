import data.ConsoleTemplateGenerator;
import data.FileTemplateGenerator;
import exceptions.InvalidFileArgumentsException;
import exceptions.MissingValueException;
import interfaces.TemplateGenerator;

import java.io.IOException;
import java.net.URISyntaxException;

public class AppRunner {

    public static void main(
            String[] args) throws InvalidFileArgumentsException, IOException, MissingValueException, URISyntaxException {
        TemplateGenerator templateGenerator;
        if (args.length > 0) {
            validateFileArguments(args);
            templateGenerator = new FileTemplateGenerator(args[0], args[1]);
        } else {
            templateGenerator = new ConsoleTemplateGenerator();
        }
        templateGenerator.setTemplateAndValues();
        if (templateGenerator instanceof ConsoleTemplateGenerator) {
            String result = templateGenerator.generateOutput();
            System.out.println("Result:\n" + result);
        }
    }

    static void validateFileArguments(String[] args) throws InvalidFileArgumentsException {
        if (args.length != 2) {
            throw new InvalidFileArgumentsException("Two file names are required.");
        }
        String fileName1 = args[0];
        String fileName2 = args[1];

        if (!fileName1.endsWith(".txt") || !fileName2.endsWith(".txt")) {
            throw new InvalidFileArgumentsException("Both file names must have '.txt' extension.");
        }
    }
}
