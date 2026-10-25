package core.java.concurrency;

class CustomRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("[" + Thread.currentThread().getName() + "] Task started. Priority: "
                + Thread.currentThread().getPriority());

        // Demonstrating yield() - hint to thread scheduler to give up CPU time slot
        Thread.yield();

        try {
            // Demonstrating sleep() - pause execution for 500 milliseconds
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("[" + Thread.currentThread().getName() + "] Interrupted during sleep.");
            // Restore interrupted state
            Thread.currentThread().interrupt();
        }

        // Checking interrupted status using isInterrupted()
        System.out.println("[" + Thread.currentThread().getName() + "] Is interrupted? "
                + Thread.currentThread().isInterrupted());
    }
}

class DaemonTask implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("[Daemon-Thread] Background monitoring task running...");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

public class Thread_Methods_Constructors_Demo {
    public static void main(String[] args) {

        // --- 1. Thread Constructors ---
        // Constructor: Thread(Runnable target, String name)
        Thread workerThread = new Thread(new CustomRunnable(), "Worker-Thread-1");

        // Constructor: Thread(Runnable target) + setName() / setPriority()
        Thread priorityThread = new Thread(new CustomRunnable());
        priorityThread.setName("High-Priority-Thread");
        priorityThread.setPriority(Thread.MAX_PRIORITY); // Priority: 10

        // --- 2. Daemon Thread Demonstration ---
        Thread daemonThread = new Thread(new DaemonTask(), "Daemon-Worker");
        // Must set daemon before starting the thread
        daemonThread.setDaemon(true);
        daemonThread.start();

        // --- 3. Checking Thread State (isAlive) ---
        System.out.println("Before start - Is workerThread alive? " + workerThread.isAlive());

        // --- 4. Starting Threads & Monitoring Execution ---
        workerThread.start();
        priorityThread.start();

        System.out.println("After start - Is workerThread alive? " + workerThread.isAlive());

        // Display basic information using getName() and getPriority()
        System.out.println("Main Thread Name: " + Thread.currentThread().getName());
        System.out.println("Main Thread Priority: " + Thread.currentThread().getPriority());

        // Interrupting workerThread to demonstrate interruption handling
        workerThread.interrupt();

        try {
            // Main thread waits for user threads to complete before exiting
            workerThread.join();
            priorityThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("After completion - Is workerThread alive? " + workerThread.isAlive());
        System.out.println("Main thread execution completed. (Daemon thread automatically terminates)");
    }
}