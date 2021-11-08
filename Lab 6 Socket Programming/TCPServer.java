import java.io.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	static String get_codebook_translation(String codebook, int fount_index){
		String message = "";
		int i;
		for(i = fount_index; codebook.charAt(i) != '\t'; i++)
		;
		i++;
		for(; codebook.charAt(i) != '\n'; i++){
			message = message + String.valueOf(codebook.charAt(i));
		}
		return message;
	}

	String[][] clean_token(String codebook, String token){
		String c_token[][];
		
	}

	static String translate_message(String client_message, String codebook){
		String client_message_token[] = client_message.split(" ");

		for(int  i = 0; i < client_message_token.length; i++){
			System.out.println(client_message_token[i]);
		}

		String message = "";
		int cb_start_match_index;
		for(int i = 0; i < client_message_token.length; i++){
			// [cleaned token str][rest of the token str][/0] = clean_token(client_message_token[i])

			cb_start_match_index = codebook.indexOf(client_message_token[i]);
			if(cb_start_match_index == -1){
				// add here

				// if rest of the token is not null
				// add rest
				// else 
				message = message + client_message_token[i];
			}else{
				// if rest of the token is not null
				// add rest
				// else 
				message = message + get_codebook_translation(codebook, cb_start_match_index);
			}
		}
		System.out.print(message); // TEst
		return message;
	}

	public static void main(String argv[]) throws Exception {
		String clientSentence;
		//String capitalizedSentence;
		String translation_sentence;
		String codebook_str = "";

		/*
			Opening codebook.txt
		 */
		File file = new File("codebook.txt");
		FileInputStream fis;
		if(file.exists()){
			fis = new FileInputStream(file);
			int r=0;  
			while((r=fis.read()) != -1){  
				codebook_str += String.valueOf((char)r);  
			} 
			codebook_str += String.valueOf((char)'\n'); 
			fis.close();
		}

		//TEST
		String s = "GM, RU COMING B4 NOON? PLZ WB ASAP. TYVM. GTG, TTYL.";
		System.out.println(translate_message(s, codebook_str));

		//System.out.println(codebook_str.toString()); //TEST

		/*
            create welcoming socket at port 6789
            - listening for incoming request, not server for talking.
            - OS binding of port: 6789
            - it can also do IP address (find an example)
            - returns a Socket Object
         */
		ServerSocket welcomeSocket = new ServerSocket(6789);
        System.out.println("Server listening on port: 6789");

		while (true) {

            /*
                wait, on welcoming socket accept() method for client contact create,
                new socket on return
             */
			Socket connectionSocket = welcomeSocket.accept();
            
            /*
                create input strea, attached to socket 
             */
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
            /*
                create output stream, attached to socket
             */
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			System.out.println("Accepted TCP connection from" 
					+ connectionSocket.getInetAddress() 
					+ ":" + connectionSocket.getPort());

			try {
				while (true) {
                    /* read in line from socket */
					clientSentence = inFromClient.readLine();

					//capitalizedSentence = clientSentence.toUpperCase() + '\n';
					translation_sentence = translate_message(clientSentence, codebook_str);

                    /* write out line to socket */
					//outToClient.writeBytes(capitalizedSentence);
					outToClient.writeBytes(translation_sentence);
				}
			} catch (Exception e) {
				// TODO: handle exception, if client closed connection, print:
				System.out.println("Client closed connection.");
			}
		}
	}


}