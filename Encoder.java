/**
 * Implements the LZW data compression algorithm
 * @author : Stuart Ussher (1060184)
 */
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
 
class Encoder{
    private final int DOMAIN = (int) Math.pow(2, 8); // character space
    private final Node[] ROOT; // trie ROOT
    private Node pointer;
    private int count; // phrase counter

    private class Node {
        int rank; // phrase number
        byte key; // Symbol
        ArrayList<Node> search_space = new ArrayList<Node>(); // child nodes
        ArrayList<Byte> index = new ArrayList<Byte>(); // index of node indexes
        
        private Node(int rank, byte key) {
            this.rank = rank;
            this.key = key;
        }

        public int query(byte value){
            int temp = index.indexOf(value);
            if (temp != -1) { // continue search
                pointer = search_space.get(temp);
                return -1;
            }
            else { // add new node, index node and go back to ROOT
                search_space.add(new Node(count++ ,value));
                index.add(value);
                pointer = ROOT[Math.abs(value)]; // start new phrase beginning with unmatched value
                return rank;
            }
        }
    }

    public Encoder() {
        ROOT = new Node[DOMAIN];
        for (count = 0; count < ROOT.length; count++) { ROOT[count] = new Node(count, (byte) count); }
    }
    
    /**
     * Implements LZW algorithm
     * @param file File to encode
     * @throws IOException
     */
    public void encode(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        if (in.available() > 0) {
            File output = new File(file.getPath().substring(0,file.getPath().indexOf('.'))+".csv");
            output.createNewFile();
            FileWriter out = new FileWriter(output);
            byte value;
            int result;
            pointer = ROOT[(byte) in.read()];
            while (in.available() > 0) {
                value = (byte) in.read();
                result = pointer.query(value);
                if (result != -1){
                    System.out.println("Symbol : " + value + "\tOutput : " + result);
                    out.write(result+",");
                }
            }
            System.out.println("Symbol : " + pointer.key + "\tOutput : " + pointer.rank);
            out.write(pointer.rank+"");
            out.close();
        }
        in.close();
    }

    /**
     * Test Implementation
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException { 
        if (args.length == 0){ 
            System.out.println("Usage: java <filepath>");
            args = new String[]{"tests/MobyDick.txt"};
        }
        File file = new File(args[0]);
        if (!file.exists() || file.length() == 0) { System.out.println("File is empty or does not exist"); }
        else {
            Encoder encoder = new Encoder();
            encoder.encode(file);
        }
    }
}
