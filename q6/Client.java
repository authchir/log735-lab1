package q6;

import java.io.*;
import java.net.*;

public class Client {
	
	// Get the socket for a given host
	private static Socket getSocket(String host) throws UnknownHostException, IOException {
		String hostname = host.split(":")[0];
		int port = Integer.parseInt(host.split(":")[1]);

        System.out.println ("Trying to reach " + hostname + " on port " + port);
        
		Socket socket = new Socket(hostname, port); // Connect to server
		socket.setSoTimeout(1000); // Wait 10 seconds for accept or answer

		return socket;
	}

	public static void main(String[] args) {
		int failover = 0;
		boolean isFinished = false;

		String userInput = "";
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		
		// Iterate every fail over servers if no answer received within 10 seconds
		while (failover < args.length && !isFinished) {
			try (Socket socket = getSocket(args[failover]);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {

				if (userInput.isEmpty()) {
					System.out.print("Input: ");
				}
				
				// Keep reading if not finished.
				// Will read a new input from client if previous command was successful.
				while (!isFinished && (!userInput.isEmpty() || (userInput = stdIn.readLine()) != null)) {
					String echo;
					out.println(userInput);
					
					if (userInput.equals("Bye.")) {
						isFinished = true;
					} else {
						echo = in.readLine();
						userInput = ""; // Reset user input since command was successful.

						if (echo == null) {
							break;
						}

						System.out.println("Echo: " + echo);
						System.out.print("Input: ");	
					}
				}
			} catch (UnknownHostException e) {
				System.err.println("Unknown host.");
			} catch (SocketTimeoutException timeoutException) {
				// Server failed to answer.
				System.err.println("Timed out.");
			} catch (IOException e) {
				System.err.println("Could not connect.");
			} finally {
				failover = (failover + 1) % args.length; // Use next available server in loopy loop.
			}
		}
		
		if (!isFinished) {
			System.err.println("Ran out of servers. :(");
		}
	}
}
