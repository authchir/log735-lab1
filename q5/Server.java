package q5;
import java.net.*; 
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server { 
	public static void main(String[] args) throws IOException {
		try (ServerSocket server = new ServerSocket(10118)) {
			AtomicInteger counter = new AtomicInteger(0); 					// Query counter
			ExecutorService threadPool = Executors.newCachedThreadPool(); 	// Creating a thread pool for the clients
			
			System.out.printf("Server running on port %d. Waiting for clients...\n", server.getLocalPort());

			// Accepting new clients in main blocking loop
			for (;;) {
				Socket client = server.accept();
					
				// Managing the new client socket in a thread from the thread pool
				threadPool.execute(new EchoTask(client, counter));
        	}
		}
		catch (IOException e) { 
			System.err.println("Could not listen on port.");
			System.exit(1); 
        } 	
	} 
}
