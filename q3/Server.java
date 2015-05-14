package q3;
import java.net.*; 
import java.io.*; 
import java.util.Random;
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
				threadPool.execute(() -> {
					System.out.println("Connection successful.");
					System.out.println("Waiting for input from client...");

					// Try with resources to automatically manage the streams' resources
					try (PrintWriter out = new PrintWriter(client.getOutputStream(), true);
						 BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));) {
						String line;

						while ((line = in.readLine()) != null) {
							System.out.printf("Server: '%s'\n", line);
							
				        	if (line.equals("Bye.")) {
				        		break;
				        	}
				        	
	        				Random rand = new Random();
	        				int randomInt = rand.nextInt(5);

	        				if (randomInt == 0) {
		        				try {
									Thread.sleep(5000);
								} catch (Exception e) {
									e.printStackTrace();
								}
	        				}
				        	
				        	out.println(line.toUpperCase());
				        }
					} catch (IOException e) {
						System.err.println("Error reading from streams.");
					} finally {
						try {
							client.close();
						} catch (Exception e) {
							System.err.println("Error closing socket.");
						}
					}
				});
        	}
		}
		catch (IOException e) { 
			System.err.println("Could not listen on port.");
			System.exit(1); 
        } 	
	} 
}
