import java.io.*;
import java.net.*;
 
public class Client {
 
	public static void main(String[] args) throws IOException {
 
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
 
		try {
			socket = new Socket("localhost", 5555); 
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("server not found");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("I/O exception:" + e.getMessage());
			System.exit(1);
		}
		System.out.println("connected to server");
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		System.out.println("Waiting for input ...");
		while (!(userInput = stdIn.readLine()).equals("END_SESSION")) {
			out.println(userInput);
			System.out.println("From the server: " + in.readLine());
		}
		System.out.println("The connection is disconnecting...");
 
		out.close();
		in.close();
		stdIn.close();
		socket.close();
	}
}