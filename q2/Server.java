package q2;
import java.net.*; 
import java.io.*; 
import java.util.concurrent.*;

public class Server { 
	public static void main(String[] args) throws IOException {
		// Creating a thread pool for the clients
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		try (ServerSocket server = new ServerSocket(10118)) { 
			System.out.println ("Le serveur est en marche. Attente de la connexion...");

			// Accepting new clients in main blocking loop
			for (;;) {
				Socket client = server.accept();
					
				// Managing the new client socket in a thread from the thread pool
				threadPool.execute(() -> {
					System.out.println("Connexion réussie");
					System.out.println("Attente de l'entrée...");

					// Try with resources to automatically manage the streams' resources
					try (PrintWriter out = new PrintWriter(client.getOutputStream(), true);
						 BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));) {
						String line;

						while ((line = in.readLine()) != null) {
							System.out.printf("Serveur: '%s'\n", line);
							
				        	if (line.equals("Bye.")) {
				        		break;
				        	}
				        	
				        	out.println(line.toUpperCase());
				        }
					} catch (IOException e) {
						System.err.println("Error reading from streams.");
						System.exit(2);
					} finally {
						try {
							client.close();
						} catch (Exception e) {
							System.err.println("Error closing socket.");
							System.exit(3);
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
