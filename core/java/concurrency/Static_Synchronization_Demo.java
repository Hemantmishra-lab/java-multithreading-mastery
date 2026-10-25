package core.java.concurrency;

// Shared resource representing a theater ticket booking system using Static Synchronization
class StaticBookMovieSeat {
    // Shared static resource across all instances of the class
    private static int totalSeats = 20;

    // Static Synchronized Method: Implements Class-Level Locking (StaticBookMovieSeat.class lock)
    // Regardless of which object instance calls this method, all threads acquire the single class-level lock.
    // This prevents data inconsistency when multiple threads interact with multiple objects.
    public static synchronized void bookSeat(int requestedSeats) {
        String threadName = Thread.currentThread().getName();

        System.out.println("[" + threadName + "] Checking class-level seat availability for "
                + requestedSeats + " seats...");

        // Simulated processing time (class lock remains held during sleep)
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Critical Section: Protected by the class-level lock across all instances
        if (totalSeats >= requestedSeats) {
            System.out.println("[" + threadName + "] Success: " + requestedSeats + " seats booked!");
            totalSeats -= requestedSeats;
            System.out.println("[" + threadName + "] Remaining total seats: " + totalSeats);
        } else {
            System.err.println("[" + threadName + "] Failed: Not enough seats available. Requested: "
                    + requestedSeats + ", Available: " + totalSeats);
        }
    }
}

class MultiObjectPassengerThread extends Thread {
    private final StaticBookMovieSeat bookingInstance;
    private final int seatsToBook;

    public MultiObjectPassengerThread(StaticBookMovieSeat bookingInstance, int seatsToBook, String name) {
        super(name);
        this.bookingInstance = bookingInstance;
        this.seatsToBook = seatsToBook;
    }

    @Override
    public void run() {
        // Calling static synchronized method via instance reference
        bookingInstance.bookSeat(seatsToBook);
    }
}

public class Static_Synchronization_Demo {
    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("Multithreading Solution: Static Synchronization");
        System.out.println("==================================================");

        // Multiple distinct object instances of the resource class
        StaticBookMovieSeat instance1 = new StaticBookMovieSeat();
        StaticBookMovieSeat instance2 = new StaticBookMovieSeat();

        // Total seats: 20 across the class
        // Instance 1 Threads
        MultiObjectPassengerThread user1 = new MultiObjectPassengerThread(instance1, 7, "App-User-Alice");
        MultiObjectPassengerThread user2 = new MultiObjectPassengerThread(instance1, 6, "App-User-Bob");

        // Instance 2 Threads
        MultiObjectPassengerThread user3 = new MultiObjectPassengerThread(instance2, 5, "Counter-User-Charlie");
        MultiObjectPassengerThread user4 = new MultiObjectPassengerThread(instance2, 4, "Counter-User-David");

        // Concurrent execution across multiple objects:
        // Class-level locking ensures only ONE thread executes bookSeat() at a time across ALL objects.
        user1.start();
        user2.start();
        user3.start();
        user4.start();

        try {
            user1.join();
            user2.join();
            user3.join();
            user4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n==================================================");
        System.out.println("Static Synchronization Key Principles");
        System.out.println("==================================================");
        System.out.println("• Lock Type     : Class-Level Lock (Class.class)");
        System.out.println("• Problem Solved: Prevents race conditions when multiple objects access shared static data");
        System.out.println("• Scope         : Blocks all other static synchronized methods on the class across all instances");
    }
}