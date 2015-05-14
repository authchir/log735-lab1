package q6;
import java.net.*; 
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server { 
	private static AtomicInteger counter = new AtomicInteger(0);					// Query counter
	private static ExecutorService threadPool = Executors.newCachedThreadPool(); 	// Creating a thread pool for the clients
	
	private static DatagramSocket syncSocket = null;								// UDP Socket for time synchronization
	
	public static void main(String[] args) throws IOException {
		try (ServerSocket server = new ServerSocket(10119)) {
			System.out.printf("Server running on port %d. Waiting for clients...\n", server.getLocalPort());

			syncSocket = new DatagramSocket(4000);									// Opening sync socket
			threadPool.execute(new CounterSynchronizationReceiveTask(syncSocket));	// Starting synchronization task
			
			// Accepting new clients in main blocking loop
			for (;;) {
				Socket client = server.accept();
					
				// Managing the new client socket in a thread from the thread pool
				threadPool.execute(new EchoTask(client));
        	}
		}
		catch (IOException e) { 
			System.err.println("Could not listen on port.");
			System.exit(1); 
        }
		finally{
			syncSocket.close();
		}
	} 
	
	public static int IncrementQueryCounter() {
		int newQueryCount = Server.counter.incrementAndGet();
		
		threadPool.execute(new CounterSynchronizationSendTask(syncSocket, newQueryCount));
		
		return newQueryCount;
	}
	
	public static void SetQueryCounter(int value) {
		Server.counter.set(value);
	}
	
	public static int GetCurrentQueryCount() {
		return Server.counter.get();
	}
}
