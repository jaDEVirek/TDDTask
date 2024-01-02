import data.ConsoleTemplateGenerator;
import data.FileTemplateGeneratorTest;
import interfaces.TemplateGeneratorTest;
import loggers.TestExecutionLogger;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import utils.EmailValidatorTest;

import java.io.PrintWriter;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;


public class AllTestsSuiteRunner {
    SummaryGeneratingListener listener = new SummaryGeneratingListener();

    public static void main(String[] args) {
        AllTestsSuiteRunner runner = new AllTestsSuiteRunner();
        runner.runAll();

        TestExecutionSummary summary = runner.listener.getSummary();
        summary.printTo(new PrintWriter(System.out));
    }

    public void runAll() {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
                        selectClass(FileTemplateGeneratorTest.class),
                        selectClass(ConsoleTemplateGenerator.class),
                        selectClass(TemplateGeneratorTest.class),
                        selectClass(EmailValidatorTest.class),
                        selectClass(TestExecutionLogger.class)
                )
                .filters(
                        includeClassNamePatterns(".*Test")
                )
                .build();
        try (LauncherSession session = LauncherFactory.openSession()) {
            Launcher launcher = session.getLauncher();
            launcher.registerTestExecutionListeners(listener);
            launcher.execute(request);
        }
    }
}
