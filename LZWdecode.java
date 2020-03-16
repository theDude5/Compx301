import java.util.ArrayList;

public class LZWdecode {

	public static void main(String[] args) {
		byte data = 0; 		//The incoming compressed byte data to be decoded
		int initialDicLength = 256;		//The initial size of the dictionary
		int phraseNum = 0;		//The Phrase number
		char c;		//The decoded char to be outputed
		char nextChar;		//Char to hold the unknown value of the next char
		

		c = (char)data;

		ArrayList<String> dictionary = new ArrayList<String>();
		dictionary.add("a");
		dictionary.add("b");
		dictionary.add("c");
		for(int i = 0; i < dictionary.size(); i++) {
			System.out.println(dictionary.get(i));
		}
		System.out.println("End of Code");
	}
}
