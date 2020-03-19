import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Encoder{
    private final int char_space = (int) Math.pow(2, 8); // character space
    private final Node[] root; // trie root
    private Node pointer;
    private int count; // phrase count

    private class Node {
        int rank; // phrase number
        byte key; // character value
        ArrayList<Node> search_space = new ArrayList<Node>(); // child nodes
        ArrayList<Byte> index = new ArrayList<Byte>(); // indexed of nodes 
        
        private Node(int rank, byte key) {
            this.rank = rank;
            this.key = key;
        }
        public int query(byte value, boolean eof){
            int temp = index.indexOf(value);
            if (temp >=0 && !eof) { // continue search
                pointer = search_space.get(temp);
                return -1;
            }
            else { // add new node, index node and go back to root
                search_space.add(new Node(count++ ,value));
                index.add(value);
                pointer = root[value];
                return rank; // return phrase rank
            }
        }
    }

    private Encoder () {
        root = new Node[char_space];
        pointer = null;
        for (count = 0; count < root.length; count++) { 
            root[count] = new Node(count, (byte) count);
        }
    }

    private int query(byte value, boolean eof){
        if (pointer == null) { pointer = root[value]; }
        return pointer.query(value, eof);
    }

    private void encode(File file){
        try {
            FileInputStream in = new FileInputStream(file);
            if (in.available() == 0) {
                System.out.println("File is empty");
                in.close();
                return;
            }
            System.out.println("Reading "+in.available()+" characters");
            byte value;
            int temp = 0;
            while (in.available() > 0) {
                value = (byte) in.read();
                temp = query(value, in.available() == 0);
                if (temp >= 0) {
                    System.out.println("Symbol : " + value + "\tOutput : " + temp);
                }
            }
            in.close();
        }
        catch (IOException e) { System.err.println(e); }
    }

    public static void main(String[] args) throws FileNotFoundException { 
        File file;
        if (args.length == 0){ 
            System.out.println("Usage: java <filepath>");
            file = new File("tests/test1.txt");
        }
        else{ file = new File(args[0]); }
        if (!file.exists()) { throw new FileNotFoundException(); }
        Encoder encoder = new Encoder();
        encoder.encode(file);
    }
}