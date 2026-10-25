package core.java.concurrency;

// Task illustrating the difference between isInterrupted() and interrupted()
class StatusCheckTask implements Runnable {
    @Override
    public void run() {
        System.out.println("--- 1. Demonstrating isInterrupted() [Instance Method] ---");
        // isInterrupted() returns current status WITHOUT clearing it
        System.out.println("[" + Thread.currentThread().getName() + "] First call to isInterrupted(): "
                + Thread.currentThread().isInterrupted());
        System.out.println("[" + Thread.currentThread().getName() + "] Second call to isInterrupted(): "
                + Thread.currentThread().isInterrupted());

        System.out.println("\n--- 2. Demonstrating interrupted() [Static Method] ---");
        // interrupted() returns current status AND CLEARS IT (resets true -> false)
        System.out.println("[" + Thread.currentThread().getName() + "] First call to Thread.interrupted(): "
                + Thread.interrupted());
        System.out.println("[" + Thread.currentThread().getName() + "] Second call to Thread.interrupted() (Status Cleared): "
                + Thread.interrupted());
    }
}

public class Thread_Interrupted_Vs_IsInterrupted_Demo {
    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("Thread Interruption Status Check Demonstration");
        System.out.println("==================================================");

        Thread workerThread = new Thread(new StatusCheckTask(), "Status-Worker");
        workerThread.start();

        // Interrupt the worker thread while active
        workerThread.interrupt();

        try {
            workerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n==================================================");
        System.out.println("Method Comparison Summary");
        System.out.println("==================================================");
        System.out.println("Method          | Type     | Checks Interruption | Clears Status Flag?");
        System.out.println("----------------|----------|---------------------|--------------------");
        System.out.println("isInterrupted() | Instance | Yes                 | No  (Retains status)");
        System.out.println("interrupted()   | Static   | Yes                 | Yes (Resets to false)");
    }
}