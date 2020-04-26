import java.util.*;
import java.io.*;


//Given the output of the compiler, and a file, find and output any matching patterns
public class REsearch {
    static class State {
        int stateNum;
        int c;
        int n1;
        int n2;

        public State(int stateNum, int ch, int n1, int n2) {
            this.stateNum = stateNum;
            this.c = c;     //If char is -1, then that means it is a branching state
            this.n1 = n1;
            this.n2 = n2;
        }

        public int getStateNum() {
            return stateNum;
        }

        public int getC() {
            return c;
        }

        public int getN1() {
            return n1;
        }

        public int getN2() {
            return n2;
        }
    }

    public REsearch(String inputFile) {
        //Deque has addFirst(e), removeFirst(), and addLast(e) eg. "deque.addLast(e)"
        Deque<State> deque = new LinkedList<State>();
        //Scan state has -2 as both next states so you know that this is the scan
        State[] states;
        State scan = new State(0,0, -2, -2);
        deque.addFirst(scan);

        try {
            File file = new File(inputFile);
            FileInputStream fis = new FileInputStream(file);
            int r = 0;
            while((r = fis.read())!=-1)
            {
                System.out.print((char)r);      //r is each char read
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args) {
        try {
            if (args.length == 0) {
                System.out.println("Usage: java REsearch <file>");
            }
            REsearch Rs = new REsearch(args[0]);
        } catch (Error e) {
            System.err.println(e);
        }
    }
}
