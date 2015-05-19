package q6;
import java.net.*; 
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server { 
	private static AtomicInteger counter = new AtomicInteger(0);					// Query counter
	private static ExecutorService threadPool = Executors.newCachedThreadPool(); 	// Creating a thread pool for the clients
	
	private static final int serverId = 1;
	private static final int serverPort = 10119;
	
	private static MulticastSocket syncSocket = null;								// UDP Socket for time synchronization
	
	private static final String multicastAddress = "239.0.0.1";
	private static final int multicastPort = 4446;
	private static InetAddress multicastSyncGroup;
	
	public static void main(String[] args) throws IOException {
		try (ServerSocket server = new ServerSocket(serverPort)) {
			System.out.printf("Server running on port %d. Waiting for clients...\n", server.getLocalPort());

			multicastSyncGroup = InetAddress.getByName(multicastAddress);
			syncSocket = new MulticastSocket(multicastPort);						// Opening sync socket
			syncSocket.joinGroup(multicastSyncGroup);								// Join multicast group
			
			threadPool.execute(new CounterSynchronizationReceiveTask(syncSocket, serverId));	// Starting synchronization task
			
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

		threadPool.execute(new CounterSynchronizationSendTask(syncSocket, newQueryCount, serverId, multicastSyncGroup, multicastPort));
		
		return newQueryCount;
	}
	
	public static void SetQueryCounter(int value) {
		Server.counter.set(value);
	}
	
	public static int GetCurrentQueryCount() {
		return Server.counter.get();
	}
}
