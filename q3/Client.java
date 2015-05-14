package q3;

import java.io.*;
import java.net.*;

public class Client {
	private static Socket getDatSocket(String host)
			throws UnknownHostException, IOException {
		String hostname = host.split(":")[0];
		int port = Integer.parseInt(host.split(":")[1]);

		System.out.printf("Trying to reach %s on port %d.\n", hostname, port);
		System.out.flush();

		Socket socket = new Socket(hostname, port);
		socket.setSoTimeout(1000);

		return socket;
	}

	public static void main(String[] args) throws IOException {
		int failover = 0;
		boolean isFinished = false;

		String userInput = "";
		
		while (failover < args.length && !isFinished) {
			try (Socket socket = getDatSocket(args[failover]);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
				
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

				if (userInput.isEmpty()) {
					System.out.print("Input: ");
				}
				
				while (!isFinished && !userInput.isEmpty() || (userInput = stdIn.readLine()) != null) {
					String echo;
					out.println(userInput);
					
					if (userInput.equals("Bye.")) {
						isFinished = true;
					} else {
						echo = in.readLine();
						userInput = "";

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
				//System.err.println("Timed out.");
			} catch (IOException e) {
				System.err.println("Could not connect.");
				e.printStackTrace();
			} finally {
				++failover;
			}
		}
	}
}
