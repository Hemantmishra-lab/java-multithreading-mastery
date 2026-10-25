package core.java.concurrency;

// Shared resource representing a movie theater booking system
class BookMovieSeat {
    // Total seats available
    private int totalSeats = 10;

    // Unsynchronized method demonstrating thread interference and data inconsistency
    public void bookSeat(int requestedSeats) {
        String threadName = Thread.currentThread().getName();

        System.out.println("[" + threadName + "] Checking seat availability...");

        // Simulated processing time to trigger race conditions between concurrent threads
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Critical Section: Without synchronization, multiple threads enter this check simultaneously
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

class PassengerThread extends Thread {
    private final BookMovieSeat bookingSystem;
    private final int seatsToBook;

    public PassengerThread(BookMovieSeat bookingSystem, int seatsToBook, String name) {
        super(name);
        this.bookingSystem = bookingSystem;
        this.seatsToBook = seatsToBook;
    }

    @Override
    public void run() {
        bookingSystem.bookSeat(seatsToBook);
    }
}

public class Synchronization_Problem_Demo {
    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("Multithreading Issue: Unsynchronized Seat Booking");
        System.out.println("==================================================");

        // Shared resource instance accessed by multiple threads
        BookMovieSeat sharedBookingSystem = new BookMovieSeat();

        // Two users attempting to book seats simultaneously
        // Total seats: 10 | User 1 requests: 7 | User 2 requests: 6 (Total requested: 13)
        PassengerThread user1 = new PassengerThread(sharedBookingSystem, 7, "User-Alice");
        PassengerThread user2 = new PassengerThread(sharedBookingSystem, 6, "User-Bob");

        // Concurrent execution leads to race conditions and negative seat balances
        user1.start();
        user2.start();

        try {
            user1.join();
            user2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n==================================================");
        System.out.println("Synchronization Overview & Concepts");
        System.out.println("==================================================");
        System.out.println("• Problem      : Race conditions & thread interference");
        System.out.println("• Definition   : Controls shared resource access for consistency");
        System.out.println("• Advantage    : Resolves data inconsistency");
        System.out.println("• Disadvantage : Increases waiting time & impacts performance");
        System.out.println("• Techniques   : Synchronized Method, Synchronized Block, Static Synchronization");
    }
}
