import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.Stack;

class Decoder{
    Node[] nodes;
    int count;
    File file;
    private class Node {
        Stack<Byte> phrase;
        public Node(int pointer) {
            phrase = new Stack<Byte>();
            if (count < 256) { phrase.push((byte) (count + Byte.MIN_VALUE)); }
            else {
                phrase.addAll(nodes[pointer].phrase);
                if (count > 256) { nodes[count-1].phrase.add(0,phrase.peek()); }
            }
        }
    }

    public Decoder(File file){
        this.file = file;
        nodes = new Node[(int) file.length()/Integer.BYTES+256];
        for (count = 0; count < 256; count++) { nodes[count] = new Node(count); }
    }
    
    public void decode() throws IOException{
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        File output = new File(file.getPath().substring(0,file.getPath().indexOf('.'))+"_.txt");
        DataOutputStream out = new DataOutputStream(new FileOutputStream(output));
        output.createNewFile();      
        while (in.available() > 0){ 
            nodes[count] = new Node(in.readInt());
            count++;
        }
        for (int i = 256; i < nodes.length; i++) { while (!nodes[i].phrase.empty()) { out.write(nodes[i].phrase.pop()); } }
        in.close();
        out.close();
    }

    public static void main(String[] args) throws IOException{
        if (args.length == 0){ 
            System.out.println("Usage: java Decoder <filepath>");
            args = new String[]{"tests/test1_lzw.txt"};
        }
        File file = new File(args[0]);
        if (!file.exists() || file.length() == 0) { System.out.println("File is empty or does not exist"); }
        else {
            Decoder decoder = new Decoder(file);
            decoder.decode();
        }
    } 
}
