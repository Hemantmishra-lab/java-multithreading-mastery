package core.java.concurrency;

// Shared resource representing a movie ticket booking system
class SynchronizedBookMovieSeat {
    private int totalSeats = 10;

    // Synchronized Method: Resolves thread interference and data inconsistency.
    // When a thread enters this method, it automatically acquires the object's intrinsic lock (monitor).
    // Other threads attempting to execute any synchronized method on the same object must wait.
    public synchronized void bookSeat(int requestedSeats) {
        String threadName = Thread.currentThread().getName();

        System.out.println("[" + threadName + "] Checking availability for " + requestedSeats + " seats...");

        // Simulated processing time (lock remains held during sleep)
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Critical Section: Protected by the object lock, ensuring thread-safe state updates
        if (totalSeats >= requestedSeats) {
            System.out.println("[" + threadName + "] Success: " + requestedSeats + " seats booked!");
            totalSeats -= requestedSeats;
            System.out.println("[" + threadName + "] Remaining seats: " + totalSeats);
        } else {
            System.err.println("[" + threadName + "] Failed: Not enough seats available. Requested: "
                    + requestedSeats + ", Available: " + totalSeats);
        }
    }
}

class SeatBookingPassenger extends Thread {
    private final SynchronizedBookMovieSeat bookingSystem;
    private final int seatsToBook;

    public SeatBookingPassenger(SynchronizedBookMovieSeat bookingSystem, int seatsToBook, String name) {
        super(name);
        this.bookingSystem = bookingSystem;
        this.seatsToBook = seatsToBook;
    }

    @Override
    public void run() {
        bookingSystem.bookSeat(seatsToBook);
    }
}

public class Synchronized_Method_Demo {
    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("Multithreading Solution: Synchronized Method");
        System.out.println("==================================================");

        // Single shared resource instance containing the intrinsic lock
        SynchronizedBookMovieSeat sharedBookingSystem = new SynchronizedBookMovieSeat();

        // Total seats: 10 | User 1 requests: 7 | User 2 requests: 6
        SeatBookingPassenger user1 = new SeatBookingPassenger(sharedBookingSystem, 7, "User-Alice");
        SeatBookingPassenger user2 = new SeatBookingPassenger(sharedBookingSystem, 6, "User-Bob");

        // Concurrent start: Only one thread acquires the object lock at a time, preventing double-booking
        user1.start();
        user2.start();

        try {
            user1.join();
            user2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n==================================================");
        System.out.println("Synchronized Method Key Takeaways");
        System.out.println("==================================================");
        System.out.println("• Mechanism : Uses object-level intrinsic lock (monitor)");
        System.out.println("• Safety    : Prevents data inconsistency & race conditions");
        System.out.println("• Behavior  : Waiting threads block until active thread releases lock");
    }
}
