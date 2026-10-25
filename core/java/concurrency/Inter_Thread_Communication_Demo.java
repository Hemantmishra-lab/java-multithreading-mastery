package core.java.concurrency;

// Shared resource calculating earnings for a movie booking application
class MovieEarningsCalculator extends Thread {
    private int totalEarnings = 0;
    private boolean isCalculated = false;

    @Override
    public void run() {
        // Inter-thread communication methods MUST be invoked inside a synchronized block/method
        // to ensure the executing thread owns the object's lock (monitor).
        synchronized (this) {
            System.out.println("[" + Thread.currentThread().getName() + "] Calculation started...");

            // Simulating seat earnings computation
            for (int i = 1; i <= 10; i++) {
                totalEarnings += 100; // Rs. 100 per ticket
            }

            System.out.println("[" + Thread.currentThread().getName() + "] Calculation complete. Total earnings: Rs. " + totalEarnings);

            isCalculated = true;

            // notify() wakes up a single thread waiting on this object's monitor
            // (notifyAll() can be used to wake up all waiting threads)
            System.out.println("[" + Thread.currentThread().getName() + "] Notifying waiting thread(s)...");
            this.notify();
        }
    }

    public int getTotalEarnings() {
        return totalEarnings;
    }

    public boolean isCalculated() {
        return isCalculated;
    }
}

public class Inter_Thread_Communication_Demo {
    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("Inter-Thread Communication (wait & notify)");
        System.out.println("==================================================");

        MovieEarningsCalculator calculatorThread = new MovieEarningsCalculator();
        calculatorThread.setName("Calculator-Worker");

        calculatorThread.start();

        // Synchronize on the target object to acquire its intrinsic lock before calling wait()
        synchronized (calculatorThread) {
            try {
                // Prevent spuriously waking up before calculation completes
                if (!calculatorThread.isCalculated()) {
                    System.out.println("[Main Thread] Waiting for " + calculatorThread.getName()
                            + " to finish calculating using wait()...");

                    // wait() releases the lock and pauses the current thread until notified
                    calculatorThread.wait();
                }

                System.out.println("[Main Thread] Resumed after notification!");
                System.out.println("[Main Thread] Final Total Earnings: Rs. " + calculatorThread.getTotalEarnings());

            } catch (InterruptedException e) {
                System.err.println("[Main Thread] Interrupted while waiting!");
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n==================================================");
        System.out.println("Key Takeaways & Method Characteristics");
        System.out.println("==================================================");
        System.out.println("• Class Location : Defined in Object class (not Thread class)");
        System.out.println("• Context Requirement : Must be called inside synchronized blocks/methods");
        System.out.println("• wait()          : Releases object lock and pauses thread execution");
        System.out.println("• notify()        : Wakes up a single waiting thread on the monitor");
        System.out.println("• notifyAll()     : Wakes up all waiting threads on the monitor");
    }
}