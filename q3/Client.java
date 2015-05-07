package q3;
import java.io.*;
import java.net.*;

public class Client {
	public static void main(String[] args) throws IOException {

		String serverHostname = new String ("127.0.0.1");

        if (args.length > 0)
        	serverHostname = args[0];
        System.out.println ("Trying to reach " + serverHostname + " at port 10118.");

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(serverHostname, 10118);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Could not connect to: " + serverHostname);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        
        System.out.print ("Input: ");
        
        while ((userInput = stdIn.readLine()) != null) {
        	out.println(userInput);
        	
        	String echo = in.readLine();
        	
        	if (echo == null) {
        		break;
        	}
        	
        	System.out.println("Echo: " + echo);
            System.out.print ("Input: ");
        }

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
}

