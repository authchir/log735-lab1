package q3;
import java.io.*;
import java.net.*;

public class Client {
	public static void main(String[] args) throws IOException {
		int failover = 0;
		String hostname = args[failover].split(":")[0];
		int port = Integer.parseInt(args[failover].split(":")[1]);
        
        System.out.println ("Trying to reach " + hostname + " at port 10118.");

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(hostname, port);
            echoSocket.setSoTimeout(10000);
            
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + hostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Could not connect to: " + hostname);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        
        System.out.print ("Input: ");
        
        while ((userInput = stdIn.readLine()) != null) {
        	String echo;
        	out.println(userInput);
        	
        	try {
        		echo = in.readLine();
            	
            	if (echo == null) {
            		break;
            	}
            	
            	System.out.println("Echo: " + echo);
                System.out.print ("Input: ");
        	} catch (SocketTimeoutException timeoutException) {
        		++failover;
        	}
        }

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
}

