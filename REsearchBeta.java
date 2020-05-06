import java.util.*;
import java.io.*;


//Given the output of the compiler, and a file, find and output any matching patterns
public class REsearch {
    List<State> states = new ArrayList<State>();     //Global array to store all the states
    //Deque has addFirst(e), removeFirst(), and addLast(e) eg. "deque.addLast(e)"
    Deque<int> deque = new LinkedList<int>();    //Deque to facilitate checking of current and next states

    class State {
        int stateNum;
        int c;
        int n1;
        int n2;

        public State(int stateNum, int ch, int n1, int n2) {
            this.stateNum = stateNum;
            this.c = ch;     //If char is \0, then that means it is a branching state
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

    public State getState(int stateNum) {
            State s = new State(0, 0, 0, 0);
            for (int i = 0; i < states.size(); i++) {
                if (states.get(i).getStateNum() == stateNum) {
                    s = states.get(i);
                }
            }
        return s;
    }

    public void getStandardInput() {
        int state, c, n1, n2;
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            line = br.readLine();
            String[] splitString = line.split("\t");     //Split the input by Tab Char to get out the state, c, n1, n2
            state = Integer.parseInt(splitString[0]);
            c = Integer.parseInt(splitString[1]);
            n1 = Integer.parseInt(splitString[2]);
            n2 = Integer.parseInt(splitString[3]);
            State s = new State(state, c, n1 , n2);
            states.add(s);      //Add the new state to the array of states
            deque.addFirst(s.getStateNum());
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    public REsearch(String inputFile) {
        //Scan has stateNum of -1, to indicate that it is the state
        State scan = new State(-1,0, 0, 0);
        deque.addFirst(scan.getStateNum());

        getStandardInput();

        try {
            File file = new File(inputFile);
            FileInputStream fis = new FileInputStream(file);
            int r = 0;
            int stateNum = 0;
            while((r = fis.read()) != -1)
            {
                int currentStateNum = deque.getFirst();
                deque.removeFirst(); //Pops the state at the top of the deque off
                State currentState = getState(currentStateNum);

                if(currentState.getC() == '\0') {      //If the char is a Branching State
                    deque.addFirst(currentState.getN1());
                    deque.addFirst(currentState.getN2());
                }
                else if(currentState.getC() == r) {     //If the char is a match to our text
                    if(currentState.getN1() != 300000) {        //If n1 does not go anywhere, don't add it to the deque
                        deque.addLast(currentState.getN1());
                    }
                    else if (currentState.getN2() != 300000) {  //If n2 does not go anywhere, don't add it to the deque
                        deque.addLast(currentState.getN2());
                    }

                }
                else if(currentState.getC() == -1 && deque.size() != 1) {     //If the char is -1, then this is the scan, if deque size is 1, there are no states left except for the scan
                    State newScan = new State(-1,0, 0, 0);
                    deque.addLast(newScan.getStateNum());
                }
                //System.out.print((char)r);
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
