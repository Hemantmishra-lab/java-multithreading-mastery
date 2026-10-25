package core.java.concurrency;

// Worker task that yields CPU control to allow equal or higher priority threads to run
class YieldingTask extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("[" + Thread.currentThread().getName() + "] Processing step " + i);

            // Thread.yield() gives a hint to the thread scheduler that this thread is willing
            // to yield its current processor time slice to threads of same or higher priority.
            // Note: Native method public static native void yield() (hint can be ignored by scheduler).
            Thread.yield();
        }
    }
}

// Background task running concurrently alongside the yielding thread
class NonYieldingTask extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("[" + Thread.currentThread().getName() + "] Processing step " + i);
        }
    }
}

public class Thread_Yield_Method_Demo {
    public static void main(String[] args) {

        // --- 1. Creating Worker Threads ---
        YieldingTask yieldingThread = new YieldingTask();
        NonYieldingTask standardThread = new NonYieldingTask();

        yieldingThread.setName("Yielding-Thread");
        standardThread.setName("Standard-Thread");

        // --- 2. Equal Priority Setting ---
        // Both threads share NORM_PRIORITY (5), making yield() hints eligible for scheduling switches
        yieldingThread.setPriority(Thread.NORM_PRIORITY);
        standardThread.setPriority(Thread.NORM_PRIORITY);

        System.out.println("Starting threads... Output is non-deterministic as scheduler may accept/ignore yield().\n");

        // --- 3. Executing Threads ---
        yieldingThread.start();
        standardThread.start();

        try {
            yieldingThread.join();
            standardThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nExecution complete.");
    }
}
