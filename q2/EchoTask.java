package q2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoTask implements Runnable {
	private final Socket _client;
	
	public EchoTask(Socket client) {
		this._client = client;
	}
	
	@Override
	public void run() {
		System.out.println("Connection successful.");
		System.out.println("Waiting for input from client...");

		// Try with resources to automatically manage the streams' resources
		try (PrintWriter out = new PrintWriter(this._client.getOutputStream(), true);
			 BufferedReader in = new BufferedReader(new InputStreamReader(this._client.getInputStream()));) {
			String line;

			while ((line = in.readLine()) != null) {
				System.out.printf("Server: '%s'\n", line);
				
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
				this._client.close();
			} catch (Exception e) {
				System.err.println("Error closing socket.");
				System.exit(3);
			}
		}
	}

}
