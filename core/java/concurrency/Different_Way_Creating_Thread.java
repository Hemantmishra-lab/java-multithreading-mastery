package core.java.concurrency;

// Method 1: Extending the Thread class
class ExtendedThread extends Thread {
    @Override
    public void run() {
        System.out.println("[Thread Class] Running on: " + Thread.currentThread().getName());
    }
}

// Method 2: Implementing the Runnable interface (Best Practice)
class RunnableTask implements Runnable {
    @Override
    public void run() {
        System.out.println("[Runnable] Running on: " + Thread.currentThread().getName());
    }
}

public class Different_Way_Creating_Thread {
    public static void main(String[] args) {

        // 1. Extending Thread class: Direct instantiation and start
        ExtendedThread thread1 = new ExtendedThread();
        thread1.setName("Thread-Extends");
        thread1.start(); // Creates call stack and registers with thread scheduler

        // 2. Implementing Runnable: Pass task object to Thread constructor
        RunnableTask task = new RunnableTask();
        Thread thread2 = new Thread(task, "Thread-Runnable");
        thread2.start();

        // 3. Thread Lifecycle Demonstration (Dead thread cannot be restarted)
        try {
            thread1.join(); // Wait for thread1 to complete its task

            System.out.println("\nAttempting to restart a finished thread...");
            thread1.start(); // Throws IllegalThreadStateException

        } catch (IllegalThreadStateException e) {
            System.err.println("Error caught: Cannot restart a dead thread.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}