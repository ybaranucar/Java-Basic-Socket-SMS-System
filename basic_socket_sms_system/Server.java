import java.net.*;
import java.io.*;
import java.util.*;
 
public class Server {
	
	static ArrayList<String> sender=new ArrayList<String>();
	static ArrayList<String> sent=new ArrayList<String>();
	static ArrayList<String> message=new ArrayList<String>();
	static String active_user = "";
	
	public static void main(String[] args) throws IOException {
		
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(5555);
		} catch (IOException e) {
			System.err.println("I/O exception: " + e.getMessage());
			System.exit(1);
		}
		
		while (true) {
			System.out.println("Server started. Waiting for connection ...");
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept(); 
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
	 
			System.out.println(clientSocket.getLocalAddress() + " connected.");
	 
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
	 
			String inputLine, outputLine;
			System.out.println("Waiting for input from client ...");
			
			while ((inputLine = in.readLine()) != null) {
				System.out.println("from client :" + inputLine);
				String[] arrSplit = inputLine.split(" <");
				
		        if (arrSplit[0].equals("BEGIN_SESSION")) {
		            active_user = arrSplit[1];
		            int sayac = 0;
		            for (String i : sent) {
		            	if (i.equals(active_user)) {
		            		sayac += 1;
		            	}
		            }
		            outputLine = "<You have " + Integer.toString(sayac) + ">messages";
		            out.println(outputLine);
		        }
		            
		        else if (arrSplit[0].equals("SEND_SMS")) {
		        	sender.add(active_user);
		        	sent.add(arrSplit[1]);
		        	message.add(arrSplit[2]);	
		            outputLine = "your message has been received";
		            out.println(outputLine);
		        }
		            
		        else if (arrSplit[0].equals("POP_SMS")) {
		            int sayac = 0;
		            for (int i = 0; i < sent.size(); i++) {
		            	if (sent.get(i).equals(active_user)) {
		            		sayac += 1;
		            	}
		            	if (sayac == 1) {
		            		outputLine = "<" + sender.get(i) + "<" + message.get(i);
		            		out.println(outputLine);
		            		sender.remove(i);
		            		sent.remove(i);
		            		message.remove(i);
		            		break;
		            	}
		            }
		            if (sayac == 0) {
		            	outputLine = "You do not have an SMS message";
		            	out.println(outputLine);
		            }
		        }
		            
		        else if (arrSplit[0].equals("END_SESSION")) {
		            break;
		        }
		            
		        else {
		            outputLine = "message could not be understood";
		            out.println(outputLine);
		        }
			}
			
			System.out.println(clientSocket.getLocalSocketAddress()
					+ " disconnected.");
			
			if (message.size() == 100000) { 
				break;
			}

		}
		serverSocket.close();
	}
	
	
}