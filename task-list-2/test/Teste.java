import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class Teste {

    protected static class CustomExecutionListener extends RunListener {

        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_BLACK = "\u001B[30m";
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_BLUE = "\u001B[34m";

        boolean failed = false;


        public void testRunStarted(Description description) throws Exception {
            System.out.println(ANSI_GREEN + "Number of tests to execute: " + ANSI_RED + description.testCount() + ANSI_RESET);
        }

        // public void testRunFinished(Result result) throws Exception {
        //     System.out.println("Number of tests executed: " + result.getRunCount());
        // }

        public void testStarted(Description description) throws Exception {
            // System.out.println("Starting: " + description.getMethodName());
            // System.out.print(description.getMethodName());
            System.out.println(description.getAnnotations());
            System.out.print(ANSI_BLUE + "--> " + ANSI_RESET);
        }

        public void testFinished(Description description) throws Exception {
            // System.out.println("Finished: " + description.getMethodName());
            if (!this.failed) {
                System.out.println(description.getMethodName() + ANSI_GREEN + " [OK]" + ANSI_RESET);
            }
            this.failed = false;
        }

        public void testFailure(Failure failure) throws Exception {
            // System.out.println("\t" + failure.getDescription().getMethodName());
            this.failed = true;
            // System.out.println(ANSI_RED + " [Falha] " + ANSI_RESET);
            System.out.println(ANSI_RED + failure.toString() + ANSI_RESET);
        }

        // public void testAssumptionFailure(Failure failure) {
        //     System.out.println("Failed: " + failure.getDescription().getMethodName());
        // }

        // public void testIgnored(Description description) throws Exception {
        //     System.out.println("Ignored: " + description.getMethodName());
        // }
    }

    public static void main(String[] args) {

        JUnitCore junitCore = new JUnitCore();
        junitCore.addListener(new CustomExecutionListener());

        String sep = new String(new char[30]).replace("\0", "-");

        System.out.println(sep + " Running tests " + sep);
        final Result result = junitCore.run(Testes.class);
        System.out.println(sep);

        // System.out.println(sep + " Fails " + sep);
        // for (final Failure failure : result.getFailures()) {
        //     System.out.println(failure.toString());
        //     System.out.println("--");
        // }
        // System.out.println(sep);

        System.out.println(sep + " Results " + sep);
        int count = result.getRunCount();
        int fails = result.getFailureCount();
        int correctPercent = (100 * (count - fails)) / count;

        System.out.println("Testes: " + count + " / Falha(s): " + fails);
        System.out.println("--");
        System.out.println("Nota (0 ~ 100): " + correctPercent);
    }
}