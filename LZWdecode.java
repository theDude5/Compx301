import java.io.*; 
import java.util.*; 

//Author: Ryan Good ID:1353453

class LZWdecode {
    phrase[] dictionary;

    private class phrase {
        int phraseNumber;
        String symbol;

        private void addItem(int _phraseNumber, String _symbol) {
            phraseNumber = _phraseNumber;
            symbol = _symbol;
        }

        private void setVars(int phrase, String symb) {
            phraseNumber = phrase;
            symbol = symb;
        }

        private void output() {
            System.out.print("Phrase Number: "  + phraseNumber + ", " + "Symbol: " + symbol);
        }
    }

	public LZWdecode() {
		dictionary  = new phrase[256];
        phrase c = new phrase();
        c.setVars(1, "A");
        c.output();
	}

	public void decode() {
        System.out.println("Decode bytes and output to file");
    }

    public static void main(String[] args) {
        LZWdecode decoder = new LZWdecode();
        decoder.decode();
    }
}
