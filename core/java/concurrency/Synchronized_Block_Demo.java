package core.java.concurrency;

// Shared resource representing a theater ticket booking system using Synchronized Blocks
class SynchronizedBlockBookSeat {
    private int totalSeats = 10;

    public void bookSeat(int requestedSeats) {
        String threadName = Thread.currentThread().getName();

        // 1. NON-SYNCHRONIZED SECTION (Runs concurrently across all threads)
        // High-performance operations like UI updates, logging, or pre-validations
        System.out.println("[" + threadName + "] Log: User initiated booking request for " + requestedSeats + " seats.");
        System.out.println("[" + threadName + "] Log: Validating user credentials and session token...");

        // 2. SYNCHRONIZED BLOCK (Targeted Critical Section)
        // Only locks the specific lines accessing the shared resource ('this' refers to current object lock)
        // Minimizes thread waiting time and improves overall application performance
        synchronized (this) {
            System.out.println("\n[" + threadName + "] Lock Acquired -> Checking seat availability...");

            // Simulated critical processing delay
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (totalSeats >= requestedSeats) {
                System.out.println("[" + threadName + "] Success: " + requestedSeats + " seats booked!");
                totalSeats -= requestedSeats;
                System.out.println("[" + threadName + "] Remaining seats: " + totalSeats);
            } else {
                System.err.println("[" + threadName + "] Failed: Not enough seats available. Requested: "
                        + requestedSeats + ", Available: " + totalSeats);
            }
            System.out.println("[" + threadName + "] Lock Released.\n");
        }

        // 3. NON-SYNCHRONIZED SECTION (Runs concurrently after releasing lock)
        System.out.println("[" + threadName + "] Log: Sending booking receipt notification via email.");
    }
}

class BlockPassengerThread extends Thread {
    private final SynchronizedBlockBookSeat bookingSystem;
    private final int seatsToBook;

    public BlockPassengerThread(SynchronizedBlockBookSeat bookingSystem, int seatsToBook, String name) {
        super(name);
        this.bookingSystem = bookingSystem;
        this.seatsToBook = seatsToBook;
    }

    @Override
    public void run() {
        bookingSystem.bookSeat(seatsToBook);
    }
}

public class Synchronized_Block_Demo {
    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("Multithreading Solution: Synchronized Block");
        System.out.println("==================================================");

        // Single shared resource instance containing the intrinsic object lock
        SynchronizedBlockBookSeat sharedBookingSystem = new SynchronizedBlockBookSeat();

        // Total seats: 10 | User 1 requests: 7 | User 2 requests: 6
        BlockPassengerThread user1 = new BlockPassengerThread(sharedBookingSystem, 7, "User-Alice");
        BlockPassengerThread user2 = new BlockPassengerThread(sharedBookingSystem, 6, "User-Bob");

        // Concurrent start: Non-critical parts run in parallel, while critical section is thread-safe
        user1.start();
        user2.start();

        try {
            user1.join();
            user2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("==================================================");
        System.out.println("Synchronized Block Key Advantages");
        System.out.println("==================================================");
        System.out.println("• Granular Scope : Synchronizes only critical sections rather than whole methods");
        System.out.println("• High Efficiency: Allows non-critical code to execute concurrently across threads");
        System.out.println("• Lock Flexibility: Supports locking on 'this' or any custom monitor object");
    }
}