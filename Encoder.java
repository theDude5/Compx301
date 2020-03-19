import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.IOException;

public class Encoder{
    private final int char_space = (int) Math.pow(2, 8);
    private final Node[] root;
    private Node pointer;
    private int count;

    private class Node {
        int rank; // phrase number
        byte val; // character value
        ArrayList<Node> next = new ArrayList<Node>(); //child nodes
        ArrayList<Byte> next_Bytes = new ArrayList<Byte>(); //record of child node values 
        
        private Node(int rank, byte val) {
            this.rank = rank;
            this. val = val;
        }
        
        public int query(byte value){
            int temp = next_Bytes.indexOf(value);
            if (temp >=0) { pointer = next.get(temp); }
            else {
                next.add(new Node(count++ ,value));
                next_Bytes.add(value);
                pointer = root[value];
            }
            return temp >=0 ? pointer.rank : rank ;
        }    
    }
    
    public Encoder () {
        root = new Node[char_space];
        pointer = null;
        for (count = 0; count < root.length; count++) { root[count] = new Node(count, (byte) count); }
    }

    public int query(byte value){
        if (pointer == null) { 
            pointer = root[value];
            return pointer.rank;
        }
        return pointer.query(value);
    }

    // Unit Tests
    public static void main(String[] args){
        if (args.length == 0){ 
            System.out.println("Usage: java <filepath>");
            return;
        }
        try {
            FileInputStream file = new FileInputStream(args[0]);
            if (file.available() == 0) {
                System.out.println("File is empty");
                file.close();
                return;
            }
            Encoder encoder = new Encoder();
            System.out.println("Reading "+file.available()+" characters");
            byte value;
            int old_count = encoder.count, temp;
            while (file.available() > 0) {
                value = (byte) file.read();
                temp = encoder.query(value);
                if (old_count != encoder.count || file.available() == 0) { 
                    System.out.println("Symbol :"+value+"\t Phrase Number : "+temp);
                    old_count++;
                } 
            }
            file.close();
        }
        catch (IOException e) { System.err.println(e); }
    }
}