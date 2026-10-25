package core.java.concurrency;

class CustomTask implements Runnable {
    @Override
    public void run() {
        // Thread.currentThread() gets the reference to the currently executing thread
        System.out.println("Default user thread name: " + Thread.currentThread().getName());

        // Customizing the thread name using setName()
        Thread.currentThread().setName("Worker-Task-Thread");
        System.out.println("Updated user thread name: " + Thread.currentThread().getName());
    }
}

public class Thread_Naming_And_Status_Demo {
    public static void main(String[] args) {

        // --- 1. Main Thread Naming & Modification ---
        // Accessing the main thread executing this code block
        Thread mainThread = Thread.currentThread();
        System.out.println("Default main thread name: " + mainThread.getName());

        // Changing the main thread's name
        mainThread.setName("Primary-App-Thread");
        System.out.println("Updated main thread name: " + mainThread.getName());


        // --- 2. Default Naming for User-Created Threads ---
        // Creating threads without specifying names (JVM defaults to Thread-0, Thread-1, etc.)
        Thread defaultThread1 = new Thread(new CustomTask());
        Thread defaultThread2 = new Thread(new CustomTask());


        // --- 3. Checking Thread Status with isAlive() ---
        System.out.println("\n--- Checking isAlive() Status ---");
        System.out.println("Before start - Is defaultThread1 alive? " + defaultThread1.isAlive());

        // Start thread execution
        defaultThread1.start();
        defaultThread2.start();

        System.out.println("After start - Is defaultThread1 alive? " + defaultThread1.isAlive());

        try {
            // Wait for both threads to finish execution
            defaultThread1.join();
            defaultThread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("After completion - Is defaultThread1 alive? " + defaultThread1.isAlive());
    }
}