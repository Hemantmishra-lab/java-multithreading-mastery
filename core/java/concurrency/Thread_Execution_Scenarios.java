package core.java.concurrency;

// Task A: Video Playback
class VideoTask extends Thread {
    @Override
    public void run() {
        System.out.println("[Video Thread] Playing video stream on: " + Thread.currentThread().getName());
    }
}

// Task B: Audio Playback
class MusicTask extends Thread {
    @Override
    public void run() {
        System.out.println("[Music Thread] Playing audio track on: " + Thread.currentThread().getName());
    }
}

// Task C: Progress Bar Loader
class ProgressTask extends Thread {
    @Override
    public void run() {
        System.out.println("[Progress Thread] Updating buffer progress on: " + Thread.currentThread().getName());
    }
}

public class Thread_Execution_Scenarios {
    public static void main(String[] args) {

        System.out.println("--- Case 1: Single Task from Single Thread ---");
        VideoTask singleWorker = new VideoTask();
        singleWorker.setName("Single-Worker");
        singleWorker.start();


        System.out.println("\n--- Case 2: Single Task from Multiple Threads ---");
        // Same task logic executed by multiple independent threads
        VideoTask worker1 = new VideoTask();
        VideoTask worker2 = new VideoTask();
        worker1.setName("Video-Worker-1");
        worker2.setName("Video-Worker-2");
        worker1.start();
        worker2.start();


        System.out.println("\n--- Case 3: Multiple Tasks from Single Thread (Sequential Limit) ---");
        // Demonstrating that a single thread processes steps sequentially, not concurrently
        System.out.println("[Main Thread] Executing Task A sequentially...");
        System.out.println("[Main Thread] Executing Task B sequentially...");


        System.out.println("\n--- Case 4: Multiple Tasks from Multiple Threads (True Multitasking) ---");
        // Independent classes assigned to dedicated threads for concurrent execution
        VideoTask videoThread = new VideoTask();
        MusicTask musicThread = new MusicTask();
        ProgressTask progressThread = new ProgressTask();

        videoThread.setName("Multitask-Video");
        musicThread.setName("Multitask-Music");
        progressThread.setName("Multitask-Progress");

        // Execution order is non-deterministic (managed by JVM thread scheduler)
        videoThread.start();
        musicThread.start();
        progressThread.start();


        // --- Demonstration: Lifecycle Limitation (Dead Thread Restart Failure) ---
        try {
            videoThread.join(); // Wait for videoThread to complete execution

            System.out.println("\nAttempting to restart a finished thread...");
            videoThread.start(); // Throws IllegalThreadStateException

        } catch (IllegalThreadStateException e) {
            System.err.println("Error: Cannot restart a dead thread (" + e.getMessage() + ").");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}