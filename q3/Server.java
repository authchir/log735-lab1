package q3;
import java.net.*; 
import java.io.*;
import java.util.concurrent.*;

public class Server { 
	public static void main(String[] args) throws IOException {
		// Creating a thread pool for the clients
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		try (ServerSocket server = new ServerSocket(10119)) {
			System.out.printf("Server running on port %d. Waiting for clients...\n", server.getLocalPort());

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
	} 
}
