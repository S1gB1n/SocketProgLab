/*
	Author: Erl John Lydzustre
			
	File: TCPServer
	This File sets a server, when connected to the server it will translate the given String from 
	the matching code in the codebook and then returns it to the Client. 
 */
import java.io.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class TCPServer {

	static final int TOKEN = 0;
	static final int NON_ALPHA = 1;
	static final int KEY = 0;
	static final int VALUE = 1;

	
	static String[] clean_token(String token){
		String c_token[] = {"",""};
		String _token = "";
		String non_alpha = "";
		for(int i = 0; i < token.length(); i++){
			if(Character.isLetter(token.charAt(i)) || Character.isDigit(token.charAt(i))){
				_token += token.charAt(i);
			}else{
				non_alpha += token.charAt(i);
			}
		}
		c_token[TOKEN] = _token;
		c_token[NON_ALPHA] = non_alpha;
		return c_token; 
	}

	static String translate_message(Hashtable<String, String> hash_codebook, String client_message){
		String client_message_token[] = client_message.split(" ");
		String message = "";
		boolean start = true;

		for(int i = 0; i < client_message_token.length; i++){
			
			// [cleaned token str][rest of the token str][/0] = clean_token(client_message_token[i])
			String[] c_token = clean_token(client_message_token[i]);

			if(hash_codebook.containsKey(c_token[KEY])){
				if(start){
					start = false;
					message = hash_codebook.get(c_token[KEY]) + c_token[VALUE];
				}else{
					message = message + " " + hash_codebook.get(c_token[KEY]) + c_token[VALUE];
				}
			}else{
				if(start){
					start = false;
					message = client_message_token[i];
				}else{
					message = message + " " + client_message_token[i];
				}
			}
		}
		return message;
	}

	public static void main(String argv[]) throws Exception {
		String capitalizedSentence;
		String clientSentence;
		//String capitalizedSentence;
		String translation_sentence;

		Hashtable<String, String> hash_codebook = new Hashtable<String, String>();

		FileReader fr = new FileReader("codebook.txt");
		BufferedReader br = new BufferedReader(fr);
		String str = br.readLine();
		while(str != null){
			String key_value[] = str.split("\t");
			hash_codebook.put(key_value[KEY], key_value[VALUE]);
			str = br.readLine();
		}

		//System.out.println(hash_codebook.toString());

		// TEST
		/*
		String s1 = "GM, RU";
		String s2 = "GM, RU COMING B4 NOON? PLZ WB ASAP. TYVM. GTG, TTYL.";
		String s3 = "WTF!";
		translation_sentence = translate_message(hash_codebook, s2);
		System.out.println(translation_sentence);
		*/

		ServerSocket welcomeSocket = new ServerSocket(6789);
        System.out.println("Server listening on port: 6789");

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
            
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			System.out.println("Accepted TCP connection from" 
					+ connectionSocket.getInetAddress() 
					+ ":" + connectionSocket.getPort());

			try {
				while (true) {
					clientSentence = inFromClient.readLine();

					//capitalizedSentence = clientSentence.toUpperCase() + '\n';
					translation_sentence = translate_message(hash_codebook, clientSentence) + "\n";

                    /* write out line to socket */
					//outToClient.writeBytes(capitalizedSentence);
					outToClient.writeBytes(translation_sentence);
					translation_sentence = "";
				}
			} catch (Exception e) {
				// TODO: handle exception, if client closed connection, print:
				System.out.println("Client closed connection.");
			}
		}
	}


}
