package core.java.concurrency;

class SleepTask implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("[" + Thread.currentThread().getName() + "] Iteration: " + i);

            try {
                // 1. Core sleep() Usage (Milliseconds) - Pauses execution for specified duration
                // Throws checked InterruptedException which must be handled or declared
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.println("[" + Thread.currentThread().getName() + "] Sleep interrupted!");
                Thread.currentThread().interrupt(); // Restore interrupted status
                break;
            }
        }
    }
}

class LockHolderTask implements Runnable {
    private final Object lock = new Object();

    @Override
    public void run() {
        synchronized (lock) {
            System.out.println("\n[" + Thread.currentThread().getName() + "] Acquired lock. Going to sleep...");
            try {
                // Key behavior: Thread.sleep() pauses execution BUT DOES NOT release locks
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("[" + Thread.currentThread().getName() + "] Woke up and releasing lock.");
        }
    }
}

public class Thread_Sleep_Method_Demo {
    public static void main(String[] args) {

        // --- 1. Demonstrating Overloaded sleep() Methods ---
        System.out.println("--- Overloaded sleep() Prototype Demonstrations ---");
        try {
            System.out.println("Sleeping for 300 ms (Native method: sleep(long millis))...");
            Thread.sleep(300);

            System.out.println("Sleeping for 300 ms and 500,000 ns (Overload: sleep(long millis, int nanos))...");
            Thread.sleep(300, 500000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        // --- 2. Invalid Argument Exception Handling ---
        System.out.println("\n--- Exception Handling for Invalid Arguments ---");
        try {
            System.out.println("Attempting to sleep with negative milliseconds (-100ms)...");
            Thread.sleep(-100); // Triggers IllegalArgumentException
        } catch (IllegalArgumentException e) {
            System.err.println("Caught Exception: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try {
            System.out.println("Attempting to sleep with out-of-range nanoseconds (1,500,000ns)...");
            Thread.sleep(100, 1500000); // Nanoseconds range must be 0-999999
        } catch (IllegalArgumentException e) {
            System.err.println("Caught Exception: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        // --- 3. Thread Execution & Scheduling Behavior ---
        System.out.println("\n--- Concurrent Sleep Execution ---");
        Thread thread1 = new Thread(new SleepTask(), "Worker-1");
        Thread thread2 = new Thread(new SleepTask(), "Worker-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        // --- 4. Lock Retention Demonstration ---
        Thread lockThread = new Thread(new LockHolderTask(), "Lock-Worker");
        lockThread.start();

        try {
            lockThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nMain thread execution finished.");
    }
}
