package core.java.concurrency;

// Media player task simulating continuous background playback
class AudioPlayerTask implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("[Audio Thread - " + Thread.currentThread().getName()
                    + "] Playing track segment " + i);
            try {
                // Yield CPU time to demonstrate context switching during sleep
                Thread.sleep(400);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

class VideoPlayerTask implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("[Video Thread - " + Thread.currentThread().getName()
                    + "] Rendering frame segment " + i);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

public class Thread_Start_Vs_Run_Demo {
    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("1. INCORRECT: Calling run() directly (Sequential Execution)");
        System.out.println("==================================================");

        Thread audioThreadDirect = new Thread(new AudioPlayerTask(), "Direct-Audio");
        Thread videoThreadDirect = new Thread(new VideoPlayerTask(), "Direct-Video");

        // Invoking run() directly executes code synchronously on the MAIN thread.
        // No new call stack is created, and tasks execute sequentially.
        audioThreadDirect.run();
        videoThreadDirect.run();


        System.out.println("\n==================================================");
        System.out.println("2. CORRECT: Calling start() (Concurrent Multithreading)");
        System.out.println("==================================================");

        Thread audioThreadConcurrent = new Thread(new AudioPlayerTask(), "Concurrent-Audio");
        Thread videoThreadConcurrent = new Thread(new VideoPlayerTask(), "Concurrent-Video");

        // Invoking start() registers threads with the JVM scheduler and creates separate call stacks.
        // This allows CPU time-slicing and interleaving during sleep() calls.
        audioThreadConcurrent.start();
        videoThreadConcurrent.start();

        try {
            // Wait for both concurrent threads to complete before exiting main
            audioThreadConcurrent.join();
            videoThreadConcurrent.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n[Main Thread] Playback demonstration complete.");
    }
}