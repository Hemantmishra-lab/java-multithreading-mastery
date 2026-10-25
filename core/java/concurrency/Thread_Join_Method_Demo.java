package core.java.concurrency;

// Driver License Sequential Process Steps

class MedicalTestTask extends Thread {
    @Override
    public void run() {
        try {
            System.out.println("[Medical Test] Examination started...");
            Thread.sleep(500); // Simulate medical checkup duration
            System.out.println("[Medical Test] Completed & Cleared!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class DrivingTestTask extends Thread {
    private final Thread medicalThread;

    public DrivingTestTask(Thread medicalThread) {
        this.medicalThread = medicalThread;
    }

    @Override
    public void run() {
        try {
            // Wait for Medical Test to complete before starting Driving Test
            medicalThread.join();

            System.out.println("[Driving Test] Test drive started...");
            Thread.sleep(500); // Simulate driving test duration
            System.out.println("[Driving Test] Passed!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class ChildWaitingForMainTask implements Runnable {
    private final Thread mainThreadRef;

    public ChildWaitingForMainTask(Thread mainThreadRef) {
        this.mainThreadRef = mainThreadRef;
    }

    @Override
    public void run() {
        try {
            System.out.println("[Child Thread] Waiting for Main thread to complete initialization...");
            // Child thread waits for Main thread execution to complete
            mainThreadRef.join();
            System.out.println("[Child Thread] Main thread finished! Proceeding with post-initialization.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Thread_Join_Method_Demo {
    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("1. Overloaded join() Signature Verification");
        System.out.println("==================================================");

        Thread dummyThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        dummyThread.start();

        try {
            // Overload 1: join() - Waits indefinitely for thread death
            // Overload 2: join(long millis) - Waits at most millis
            // Overload 3: join(long millis, int nanos) - Waits at most millis + nanos
            dummyThread.join(200); // Wait maximum 200 ms
            dummyThread.join(200, 500000); // Wait maximum 200 ms + 500,000 ns
            dummyThread.join(); // Final wait until termination
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        System.out.println("\n==================================================");
        System.out.println("2. Multi-Step License Process (Thread Dependencies)");
        System.out.println("==================================================");

        MedicalTestTask medical = new MedicalTestTask();
        DrivingTestTask driving = new DrivingTestTask(medical);

        medical.start();
        driving.start();

        try {
            // Officer (Main Thread) waits for Driving Test to complete before issuing license
            driving.join();
            System.out.println("[Officer - Main Thread] Final approval granted. License Issued!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        System.out.println("\n==================================================");
        System.out.println("3. Child Thread Waiting for Main Thread");
        System.out.println("==================================================");

        // Capturing reference of current (Main) thread
        Thread mainThread = Thread.currentThread();

        Thread childThread = new Thread(new ChildWaitingForMainTask(mainThread), "Dependent-Child");
        childThread.start();

        try {
            System.out.println("[Main Thread] Performing core application setups...");
            Thread.sleep(400);
            System.out.println("[Main Thread] Setup complete. Main exiting...");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}