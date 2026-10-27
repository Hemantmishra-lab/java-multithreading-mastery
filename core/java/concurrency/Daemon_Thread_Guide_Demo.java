// package core.java.concurrency;

// // Background task simulating a service thread (e.g., Garbage Collector, Autosave)
// class BackgroundService implements Runnable {
//     @Override
//     public void run() {
//         while (true) {
//             System.out.println("[Daemon Service] Running in background on: "
//                     + Thread.currentThread().getName());
//             try {
//                 Thread.sleep(300);
//             } catch (InterruptedException e) {
//                 System.out.println("[Daemon Service] Interrupted.");
//                 break;
//             }
//         }
//     }
// }

// // Parent thread used to demonstrate daemon status inheritance
// class ParentUserTask implements Runnable {
//     @Override
//     public void run() {
//         System.out.println("\n--- Daemon Inheritance Demonstration ---");
//         System.out.println("Parent thread is daemon? " + Thread.currentThread().isDaemon());

//         // Child thread created by a daemon thread automatically inherits daemon status
//         Thread childThread = new Thread(() -> {
//             System.out.println("Child thread spawned by daemon is daemon? "
//                     + Thread.currentThread().isDaemon());
//         }, "Child-Service-Thread");

//         childThread.start();
//         try {
//             childThread.join();
//         } catch (InterruptedException e) {
//             Thread.currentThread().interrupt();
//         }
//     }
// }

// public class Daemon_Thread_Guide_Demo {
//     public static void main(String[] args) {

//         // --- 1. Basic Daemon Setup & Properties ---
//         Thread daemonWorker = new Thread(new BackgroundService(), "Background-Service-Thread");

//         // Mark thread as daemon before starting it
//         daemonWorker.setDaemon(true);

//         // Verify daemon status using isDaemon()
//         System.out.println("Is daemonWorker configured as daemon? " + daemonWorker.isDaemon());

//         daemonWorker.start();


//         // --- 2. Illegal State Check (Setting daemon after start()) ---
//         Thread invalidDaemonSetup = new Thread(new BackgroundService(), "Invalid-Setup-Thread");
//         invalidDaemonSetup.start();

//         try {
//             // Attempting setDaemon() on an already started thread throws IllegalThreadStateException
//             invalidDaemonSetup.setDaemon(true);
//         } catch (IllegalThreadStateException e) {
//             System.err.println("Error: Cannot set daemon status after thread has been started.");
//         }


//         // --- 3. Main Thread Rule ---
//         try {
//             // Main thread is already started by the JVM; setting it as daemon fails
//             Thread.currentThread().setDaemon(true);
//         } catch (IllegalThreadStateException e) {
//             System.err.println("Error: Cannot modify daemon status of the main thread.");
//         }


//         // --- 4. Daemon Inheritance Demonstration ---
//         Thread parentDaemon = new Thread(new ParentUserTask(), "Parent-Daemon-Thread");
//         parentDaemon.setDaemon(true);
//         parentDaemon.start();

//         try {
//             parentDaemon.join();

//             // Allow background daemon to run briefly
//             Thread.sleep(700);
//         } catch (InterruptedException e) {
//             Thread.currentThread().interrupt();
//         }

//         // --- 5. JVM Lifecycle Behavior ---
//         // Once the main thread ends, the JVM shuts down without waiting for daemonWorker to finish.
//         System.out.println("\n[Main Thread] Exiting... JVM terminates regardless of active daemon threads.");
//     }
// }
