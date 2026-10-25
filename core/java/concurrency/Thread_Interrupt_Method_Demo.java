package core.java.concurrency;

// Task illustrating proper interrupt handling when thread is in SLEEPING / WAITING state
class InterruptibleTask implements Runnable {
    @Override
    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                System.out.println("[" + Thread.currentThread().getName() + "] Step " + i + " complete.");

                // Key Requirement: interrupt() works when thread is in sleeping or waiting state
                // This triggers an InterruptedException when interrupt() is called on this thread
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.err.println("[" + Thread.currentThread().getName()
                    + "] Interrupted while sleeping! Exception caught: " + e.getMessage());
            // Best practice: Restore interrupted state after catching InterruptedException
            Thread.currentThread().interrupt();
        }
    }
}

// Task demonstrating that interrupt() has no effect if the thread NEVER enters sleep/wait state
class NonInterruptibleTask implements Runnable {
    @Override
    public void run() {
        System.out.println("[" + Thread.currentThread().getName() + "] Running active computation without sleep/wait...");

        // Heavy computational loop with no sleep() or wait() calls
        long count = 0;
        for (int i = 0; i < 1_000_000; i++) {
            count += i;
        }

        // interrupt() call on main thread will be ignored here because state is active RUNNABLE
        System.out.println("[" + Thread.currentThread().getName()
                + "] Task finished normally. Interrupted status: " + Thread.currentThread().isInterrupted());
    }
}

public class Thread_Interrupt_Method_Demo {
    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("1. Successful Interruption (Thread is SLEEPING)");
        System.out.println("==================================================");

        Thread sleepingThread = new Thread(new InterruptibleTask(), "Sleeping-Worker");
        sleepingThread.start();

        try {
            // Allow worker thread to start and enter sleep state
            Thread.sleep(300);

            System.out.println("[Main Thread] Interrupting " + sleepingThread.getName() + "...");
            // Force thread out of sleep state into catch block
            sleepingThread.interrupt();

            sleepingThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        System.out.println("\n==================================================");
        System.out.println("2. Ineffective Interruption (Thread is NOT sleeping/waiting)");
        System.out.println("==================================================");

        Thread activeThread = new Thread(new NonInterruptibleTask(), "Active-Worker");
        activeThread.start();

        // Calling interrupt() while thread is actively processing (not in sleep/wait state)
        activeThread.interrupt();

        try {
            activeThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n[Main Thread] Interrupt demonstration complete.");
    }
}
