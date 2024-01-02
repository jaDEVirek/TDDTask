import data.ConsoleTemplateGenerator;
import data.FileTemplateGenerator;
import exceptions.MissingValueException;
import interfaces.TemplateGenerator;

import java.io.IOException;

public class AppRunner {

    public static void main(String[] args) {
        try {
            System.out.println("Choose mode: ");
            System.out.println("1. File Mode");
            System.out.println("2. Console Mode");
            int choice = getUserChoice();

            TemplateGenerator templateGenerator;
            if (choice == 1) {
                templateGenerator = new FileTemplateGenerator();
            } else if (choice == 2) {
                templateGenerator = new ConsoleTemplateGenerator();
            } else {
                System.out.println("Invalid choice. Exiting.");
                return;
            }

            templateGenerator.setTemplateAndValues();

            if (templateGenerator instanceof ConsoleTemplateGenerator) {
                String result = templateGenerator.generateOutput();
                System.out.println("Result:");
                System.out.println(result);
            }

            if (templateGenerator instanceof FileTemplateGenerator) {
                System.out.println("Enter the file path to save the output:");
                String outputPath = getUserInput().trim();
                templateGenerator.saveOutputToFile(outputPath);
            }

        } catch (IOException | MissingValueException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static int getUserChoice() {
        System.out.print("Enter your choice (1 or 2): ");
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(getUserInput().trim());
                if (choice == 1 || choice == 2) {
                    break;
                } else {
                    System.out.print("Invalid choice. Please enter 1 or 2: ");
                }
            } catch (NumberFormatException ex) {
                System.out.print("Invalid choice. Please enter 1 or 2: ");
            }
        }
        return choice;
    }

    static String getUserInput() {
        try {
            return new java.util.Scanner(System.in).nextLine();
        } catch (Exception e) {
            return "";
        }
    }
}
