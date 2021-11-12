/*
    Socket Programmaing with TCP

    Example: client-server app
    1)  client reads line from standard input(inFromUser stream), sends to server via 
        socket(outToServer strea)
    2)  server reads line from socket
    3)  server converts line to uppercase, sends back to client
    4)  client reads, prints modified line from socket(inFromServer stream)
 */


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {

	public static void main(String argv[]) throws Exception {
		String sentence;
		String modifiedSentence;

        /* Read from the user */
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        /*
            create clientSocket object of type Socket, connect to server
            domain name (hostname): "localhost" 
            port number: 6789

            after this is established we can now create input and out wrapper 
            ex.
            (DataOutStream)
        */
		Socket clientSocket = new Socket("localhost", 6789); 
		System.out.println("Client successfully established TCP connection.\n"
				+ "Client(local) end of the connection uses port " 
				+ clientSocket.getLocalPort() 
				+ " and server(remote) end of the connection uses port "
				+ clientSocket.getPort());

        /*
            create output stream attached to socket
            -this is to write to the server
         */
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		
        /*
            create input stream attached to socket
            -this is to read from the server
         */
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // read from the user
		sentence = inFromUser.readLine(); 
		while (sentence.toLowerCase().compareTo("exit") != 0) {

            /*
                send line to server
             */
			outToServer.writeBytes(sentence + '\n');

            /*
                read line from server
             */
			modifiedSentence = inFromServer.readLine();

			System.out.println("FROM SERVER: " + modifiedSentence);
			sentence = inFromUser.readLine();
		}

        // close socket (clean up behind yourself)
		clientSocket.close();
	}
}