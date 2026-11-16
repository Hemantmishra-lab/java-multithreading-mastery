

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Demonstrates creating worker tasks using the Callable interface and retrieving
 * asynchronous results via Future, contrasted with Runnable limitations.
 */
public class Callable_And_Future_Demo {

    public static void main(String[] args) {
        System.out.println("=== Callable vs. Runnable & Future Demo ===\n");

        // ExecutorService is required to execute Callable tasks (Thread class only accepts Runnable)
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            // 1. Submitting a Callable task that returns a computed result and can throw checked exceptions
            Callable<Double> taxCalculationTask = new TaxCalculationTask(150000.0, 0.18);
            Callable<String> reportGenerationTask = new ReportGenerationTask("Q4-Backend-Metrics");

            System.out.println("[Main Thread] Submitting Callable tasks to worker pool...");
            Future<Double> taxFuture = executor.submit(taxCalculationTask);
            Future<String> reportFuture = executor.submit(reportGenerationTask);

            // 2. Main thread can perform other work concurrently while tasks execute
            System.out.println("[Main Thread] Doing independent work while workers compute results...");
            System.out.println("[Main Thread] Is tax calculation done yet? " + taxFuture.isDone());

            // 3. Retrieving results using Future.get() (blocks until computation completes)
            Double calculatedTax = taxFuture.get();
            String reportStatus = reportFuture.get();

            System.out.println("\n=== Results Retrieved ===");
            System.out.println("Calculated Tax Amount: $" + calculatedTax);
            System.out.println("Report Status: " + reportStatus);
            System.out.println("Is tax calculation done now? " + taxFuture.isDone());

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Task execution failed: " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            // Always shut down the ExecutorService so the JVM can exit cleanly
            executor.shutdown();
        }
    }
}

/**
 * Worker task implementing Callable<Double> to return a computed value.
 */
class TaxCalculationTask implements Callable<Double> {
    private final double amount;
    private final double taxRate;

    public TaxCalculationTask(double amount, double taxRate) {
        this.amount = amount;
        this.taxRate = taxRate;
    }

    @Override
    public Double call() throws Exception {
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + threadName + "] Calculating tax on $" + amount + "...");
        Thread.sleep(1200); // Simulate processing delay
        return amount * taxRate;
    }
}

/**
 * Worker task implementing Callable<String> demonstrating checked exception capability.
 */
class ReportGenerationTask implements Callable<String> {
    private final String reportName;

    public ReportGenerationTask(String reportName) {
        this.reportName = reportName;
    }

    @Override
    public String call() throws Exception {
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + threadName + "] Generating report: " + reportName + "...");
        Thread.sleep(800);
        if (reportName == null || reportName.isEmpty()) {
            throw new IllegalArgumentException("Report name cannot be empty");
        }
        return reportName + "_COMPLETED.pdf";
    }
}