package core.java.concurrency;

// Unique class name to prevent package-level duplication errors
class ComparisonSleepTask implements Runnable {
    @Override
    public void run() {
        System.out.println("[" + Thread.currentThread().getName() + "] Task started. Pausing for 400ms...");
        try {
            // Static, Overloaded, Throws InterruptedException, Not final
            Thread.sleep(400);
            System.out.println("[" + Thread.currentThread().getName() + "] Woke up after 400ms.");
        } catch (InterruptedException e) {
            System.err.println("[" + Thread.currentThread().getName() + "] Sleep interrupted!");
            Thread.currentThread().interrupt();
        }
    }
}

// Task illustrating yield() behavior: Cooperative CPU time-slice yielding
class YieldTask implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 2; i++) {
            System.out.println("[" + Thread.currentThread().getName() + "] Iteration " + i + " - Yielding CPU...");
            // Static, Native, Not Overloaded, Does NOT throw InterruptedException, Not final
            Thread.yield();
        }
    }
}

// Task illustrating join() dependency target
class DependencyTask implements Runnable {
    @Override
    public void run() {
        System.out.println("[" + Thread.currentThread().getName() + "] Executing critical prerequisite task...");
        try {
            Thread.sleep(500);
            System.out.println("[" + Thread.currentThread().getName() + "] Prerequisite task finished.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Sleep_Yield_Join_Comparison_Demo {
    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("1. Demonstrating Thread.sleep() [Static, Overloaded]");
        System.out.println("==================================================");

        Thread sleepThread = new Thread(new ComparisonSleepTask(), "Sleep-Worker");
        sleepThread.start();

        try {
            // Using overloaded sleep(millis, nanos)
            Thread.sleep(100, 500000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        System.out.println("\n==================================================");
        System.out.println("2. Demonstrating Thread.yield() [Static, Native, Non-Overloaded]");
        System.out.println("==================================================");

        Thread yieldThread1 = new Thread(new YieldTask(), "Yield-Worker-1");
        Thread yieldThread2 = new Thread(new YieldTask(), "Yield-Worker-2");

        yieldThread1.start();
        yieldThread2.start();


        System.out.println("\n==================================================");
        System.out.println("3. Demonstrating threadInstance.join() [Instance, Final, Overloaded]");
        System.out.println("==================================================");

        Thread depThread = new Thread(new DependencyTask(), "Dependency-Worker");
        depThread.start();

        System.out.println("[Main Thread] Waiting for " + depThread.getName() + " to finish via join()...");

        try {
            // Instance method called on a specific thread object
            // Overloaded: supports join(), join(long millis), join(long millis, int nanos)
            depThread.join();
            System.out.println("[Main Thread] Dependency cleared. Continuing main execution.");
        } catch (InterruptedException e) {
            System.err.println("[Main Thread] Join operation interrupted!");
            Thread.currentThread().interrupt();
        }


        System.out.println("\n==================================================");
        System.out.println("4. Method Characteristics Summary");
        System.out.println("==================================================");
        System.out.println("Method  | Type     | Overloaded | Exception            | Final");
        System.out.println("--------|----------|------------|----------------------|------");
        System.out.println("sleep() | Static   | Yes        | InterruptedException | No");
        System.out.println("yield() | Static   | No         | None                 | No");
        System.out.println("join()  | Instance | Yes        | InterruptedException | Yes");
    }
}