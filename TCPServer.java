import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	static final int TOKEN = 0;
	static final int NON_ALPHA = 1;

	static String get_codebook_translation(String codebook, int fount_index){
		String message = "";
		int i;
		for(i = fount_index; codebook.charAt(i) != '\t'; i++) //{System.out.print(" index:" + codebook.charAt(i));}
		;
		i++;
		for(; codebook.charAt(i) != '\n'; i++){
			message = message + String.valueOf(codebook.charAt(i));
		}
		//System.out.println("\n message:" + message);
		return message;
	}

	static String[] clean_token(String token){
		String c_token[] = {"",""};
		String _token = "";
		String non_alpha = "";
		for(int i = 0; i < token.length(); i++){
			if(Character.isLetter(token.charAt(i))){
				_token += token.charAt(i);
			}else{
				non_alpha += token.charAt(i);
			}
		}
		c_token[TOKEN] = _token;
		c_token[NON_ALPHA] = non_alpha;
		return c_token;
	}

	static String translate_message(String client_message, String codebook){
		String client_message_token[] = client_message.split(" ");
		String message = "";
		int cb_start_match_index;
		for(int i = 0; i < client_message_token.length; i++){
			
			// [cleaned token str][rest of the token str][/0] = clean_token(client_message_token[i])
			String[] c_token = clean_token(client_message_token[i]);

			cb_start_match_index = codebook.indexOf(c_token[TOKEN]);
			
			if(cb_start_match_index == -1){
				message = message + " " + client_message_token[i];
			}else{
				message = message + " " + get_codebook_translation(codebook, cb_start_match_index) + c_token[NON_ALPHA];
			}
		}
		System.out.print(message); // TEst <-------------------------------------------------------------
		return message;
	}

	static String open_file(String file_name) throws IOException{
		String codebook_str = "";
		File file = new File(file_name);
		FileInputStream fis;
		if(file.exists()){
			fis = new FileInputStream(file);
			int r=0;  
			while((r=fis.read()) != -1){  
				codebook_str += String.valueOf((char)r);  
			} 
			codebook_str += String.valueOf((char)'\n'); 
			fis.close();
		}else{
			System.out.println("File: " + file_name + " doest not exist.");
		}
		return codebook_str;
	}

	public static void main(String argv[]) throws Exception {
		String clientSentence;
		//String capitalizedSentence;
		String translation_sentence;
		String codebook_str = "";

		System.out.println(""); // Test <-----------------------------------

		codebook_str = open_file("codebook.txt");

		// TEST
		String s1 = "GM, RU";
		String s2 = "GM, RU COMING B4 NOON? PLZ WB ASAP. TYVM. GTG, TTYL.";
		translation_sentence = translate_message(s1, codebook_str);
		//System.out.println(translation_sentence);


	}


}