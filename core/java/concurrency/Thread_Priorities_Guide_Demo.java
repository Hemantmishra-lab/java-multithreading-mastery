package core.java.concurrency;

class PriorityTask implements Runnable {
    @Override
    public void run() {
        System.out.println("[" + Thread.currentThread().getName() + "] Executing with priority: "
                + Thread.currentThread().getPriority());
    }
}

class ParentThreadTask implements Runnable {
    @Override
    public void run() {
        System.out.println("\n--- Priority Inheritance Demonstration ---");
        System.out.println("Parent thread priority: " + Thread.currentThread().getPriority());

        // Child thread inherits the priority of its parent thread
        Thread childThread = new Thread(() -> {
            System.out.println("Child thread priority (inherited): " + Thread.currentThread().getPriority());
        }, "Child-Thread");

        childThread.start();
        try {
            childThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Thread_Priorities_Guide_Demo {
    public static void main(String[] args) {

        // --- 1. Predefined Priority Constants & Main Thread Priority ---
        Thread mainThread = Thread.currentThread();
        System.out.println("Default Main Thread Priority: " + mainThread.getPriority()); // Defaults to 5 (NORM_PRIORITY)

        System.out.println("\nPredefined Priority Constants:");
        System.out.println("Thread.MIN_PRIORITY  : " + Thread.MIN_PRIORITY);  // 1
        System.out.println("Thread.NORM_PRIORITY : " + Thread.NORM_PRIORITY); // 5
        System.out.println("Thread.MAX_PRIORITY  : " + Thread.MAX_PRIORITY);  // 10


        // --- 2. Setting and Getting Priorities ---
        Thread lowPriorityThread = new Thread(new PriorityTask(), "Low-Priority-Worker");
        Thread highPriorityThread = new Thread(new PriorityTask(), "High-Priority-Worker");

        // Set explicit priorities using setPriority()
        lowPriorityThread.setPriority(Thread.MIN_PRIORITY); // 1
        highPriorityThread.setPriority(Thread.MAX_PRIORITY); // 10

        // Verify priorities using getPriority()
        System.out.println("\nConfigured Priorities:");
        System.out.println(lowPriorityThread.getName() + " priority: " + lowPriorityThread.getPriority());
        System.out.println(highPriorityThread.getName() + " priority: " + highPriorityThread.getPriority());


        // --- 3. Invalid Priority Range Exception Handling ---
        try {
            System.out.println("\nAttempting to set priority to 15 (outside 1-10 range)...");
            lowPriorityThread.setPriority(15); // Throws IllegalArgumentException
        } catch (IllegalArgumentException e) {
            System.err.println("Exception caught: Priority must be within 1 to 10.");
        }


        // --- 4. Priority Inheritance ---
        // Change main thread's priority before spawning parent task
        mainThread.setPriority(7);
        Thread parentThread = new Thread(new ParentThreadTask(), "Parent-Thread");
        parentThread.start();

        try {
            parentThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        // --- 5. Concurrent Thread Execution with Priorities ---
        System.out.println("\n--- Concurrent Execution (Platform Dependent) ---");
        // Execution order is non-deterministic and depends on OS/JVM thread scheduler
        lowPriorityThread.start();
        highPriorityThread.start();

        try {
            lowPriorityThread.join();
            highPriorityThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
