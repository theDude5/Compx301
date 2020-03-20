import java.io.*; 
import java.util.*; 

public class LZWdecode {

	public static void main(String[] args) {
		byte data = 0; 		//The incoming compressed byte data to be decoded
		int dictLength = 256;		//The initial size of the dictionary
		int phraseNum = 0;		//The Phrase number
		char c;		//The decoded char to be outputed
		char nextChar;		//Char to hold the unknown value of the next char
		
		c = (char)data;

		Stack<Integer> stack = new Stack<Integer>(); 
	
		ArrayList<Integer> phraseNumber = new ArrayList<Integer>();
		ArrayList<String> character = new ArrayList<String>();
	}

	public void decode(byte data) {
		
	}

	static void stack_push(Stack<Integer> stack) 
    { 
        for(int i = 0; i < 5; i++) 
        { 
            stack.push(i); 
        } 
    } 
      
    // Popping element from the top of the stack 
    static void stack_pop(Stack<Integer> stack) 
    { 
        System.out.println("Pop :"); 
  
        for(int i = 0; i < 5; i++) 
        { 
            Integer y = (Integer)stack.pop(); 
            System.out.println(y); 
        } 
    } 
}
