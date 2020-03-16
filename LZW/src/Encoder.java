import java.util.ArrayList;

public class Encoder{
    private final int char_space = (int) Math.pow(2, 8);
    private final Node[] root;
    private int rank;
    private byte buffer;
    private int depth;
    
    private class Node {
        int rank; // phrase number
        byte val; // character
        ArrayList<Node> next = new ArrayList<Node>();
        private Node(int rank, byte val) {
            this.rank = rank;
            this. val = val;
        }
    }

    public Encoder () {
        root = new Node[char_space];
        for (rank = 0; rank < root.length; rank++) {
            root[rank] = new Node(rank, (byte) rank);
        }
        depth = 1;
    }

    // public int query(byte val){
    //     Node curr = root[val];
    // }

    // Unit Tests
    public static void main(String[] args){System.out.println("Hello World");}
}