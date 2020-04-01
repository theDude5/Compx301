/**
 * Implements the LZW data compression algorithm
 * @author : Stuart Ussher (1060184)
 */
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Encoder class
 * the root node is an array of 256 nodes
 * indexes 0 and 255 represent byte values -128 and 127 respectively
 */
class Encoder{
    private final Node[] root; // trie root
    private Node pointer;
    private int count; // phrase counter
    
    private class Node {
        int rank; // phrase number
        // byte key; // symbol value
        ArrayList<Node> search_space = new ArrayList<Node>(); // child nodes
        ArrayList<Byte> index = new ArrayList<Byte>(); // indexed of nodes 
        
        private Node(int rank, byte key) {
            this.rank = rank;
            // this.key = key; // used for testing
        }
        
        public int query(byte key){
            int temp = index.indexOf(key);
            if (temp != -1) { // continue search
                pointer = search_space.get(temp);
                return -1;
            }
            else { // add new node, index node and go back to root
                search_space.add(new Node(count++ ,key));
                index.add(key);
                pointer = root[key - Byte.MIN_VALUE]; // start new phrase beginning with unmatched value
                return rank;
            }
        }
    }

    public Encoder() {
        root = new Node[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
        for (count = 0; count < root.length; count++) { root[count] = new Node(count, (byte) (count + Byte.MIN_VALUE)); }
    }
    
    /**
     * Implements LZW algorithm
     * @param file File to encode
     * @throws IOException
     */
    public void encode(File file) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        if (in.available() > 0) {
            File output = new File(file.getPath().substring(0,file.getPath().indexOf('.'))+"_lzw.txt");
            output.createNewFile();
            FileWriter out = new FileWriter(output);
            byte key;
            int result;
            pointer = root[in.readByte() - Byte.MIN_VALUE];
            while (in.available() > 0) {;
                key = in.readByte();
                result = pointer.query(key);
                if (result != -1){
                    // System.out.println("Input : " + value + "\tOutput : " + result);
                    out.write(result);
                }
            }
            // System.out.println("EOF Input : " + pointer.key + "\tEOF Output : " + pointer.rank);
            out.write(pointer.rank);
            out.close();
        }
        in.close();
    }

    /**
     * LZW Encoder Implementation
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException { 
        if (args.length == 0){ 
            System.out.println("Usage: java <filepath>");
            return;
            // args = new String[]{"tests/MobyDick.txt"};
        }
        File file = new File(args[0]);
        if (!file.exists() || file.length() == 0) { System.out.println("File is empty or does not exist"); }
        else {
            Encoder encoder = new Encoder();
            encoder.encode(file);
        }
    }
}
