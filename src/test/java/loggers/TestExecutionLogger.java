package loggers;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


@Tag("execution_logger_for_all_tests")
public class TestExecutionLogger implements TestWatcher {

    @Override
    public void testSuccessful(ExtensionContext context) {
        log("Test passed: " + context.getDisplayName());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        log("Test failed: " + context.getDisplayName() + " - " + cause.getMessage());
    }

    private void log(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test-execution-log.txt", true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}