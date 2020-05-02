import java.util.*;
import java.io.*;


//Given the output of the compiler, and a file, find and output any matching patterns
public class REsearch {
    List<State> states = new ArrayList<State>();     //Global array to store all the states

    class State {
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

    public void getStandardInput() {
        int state, c, n1, n2;
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            line = br.readLine();
            //System.out.println(line);
            String[] splitString = line.split(" ");     //Split the input by SOMETHING to get out the state, c, n1, n2
            state = Integer.parseInt(splitString[0]);
            c = Integer.parseInt(splitString[1]);
            n1 = Integer.parseInt(splitString[2]);
            n2 = Integer.parseInt(splitString[3]);
            State s = new State(state, c, n1 , n2);
            states.add(s);      //Add the new state to the array of states
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    public REsearch(String inputFile) {
        //Deque has addFirst(e), removeFirst(), and addLast(e) eg. "deque.addLast(e)"
        getStandardInput();
        Deque<State> deque = new LinkedList<State>();
        //Scan state has -2 as both next states so you know that this is the scan
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
