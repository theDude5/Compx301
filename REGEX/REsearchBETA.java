import java.util.*;
import java.io.*;


//Given the output of the compiler as standard input and a file, find and output any matching patterns
public class REsearch {
    List<State> states = new ArrayList<State>();     //Global array to store all the states
    //Deque has addFirst(e), removeFirst(), and addLast(e) eg. "deque.addLast(e)"
    Deque<Integer> deque = new LinkedList<Integer>();    //Deque to facilitate checking of current and next states

    class State {
        int stateNum;
        int c;
        int n1;
        int n2;

        public State(int _stateNum, int _c, int _n1, int _n2) {
            this.stateNum = _stateNum;
            this.c = _c;     //If char is \0, then that means it is a branching state
            this.n1 = _n1;
            this.n2 = _n2;
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
        int condition = 0;
        //Scan has stateNum of -1, to indicate that it is the state
        State scan = new State(-1,0, 0, 0);
        deque.addFirst(scan.getStateNum());
        getStandardInput();

        try {
            File file = new File(inputFile);
            FileInputStream fis = new FileInputStream(file);
            int r;          //r is the char being read from the file
            int p = 0;      //p is the pointer
            int stateNum = 0;
            while((r = fis.read()) != -1)
            {
                p = r;
                Integer currentStateNum = deque.getFirst();
                deque.removeFirst(); //Pops the state at the top of the deque off
                State currentState = getState(currentStateNum);

                if(currentState.getC() == -1) {
                    if(deque.size() > 0) {      //If you popped scan but there are still more states, put it on the botton of deque
                        State newScan = new State(-1,0, 0, 0);
                        deque.addLast(newScan.getStateNum());
                    }
                    else {      //Otherwise, scan was popped because deque is empty so match has failed
                        condition = 1;
                        break;
                    }
                }
                else if(currentState.getC() == r) {     //If the char is a match to our text
                    if(currentState.getN1() == currentState.getN2()) {        //If n1 is == to n2, only add one of them
                        deque.addLast(currentState.getN1());
                    }
                    else {
                        deque.addLast(currentState.getN1());
                        deque.addLast(currentState.getN2());
                    }
                    p++;
                    while(p == r) {       //Moves the pointer along to check the next char if it is the same, and so on until whole pattern is found
                        System.out.println((char)r);
                        p++;
                        r++;
                        condition = 2;
                    }
                }
                else if(currentState.getC() == '\0') {      //If the char is a Branching State
                    deque.addFirst(currentState.getN1());
                    deque.addFirst(currentState.getN2());
                }
                //System.out.print((char)r);
            }
            if(condition == 0) {
                System.out.println("The End Of The File was reached, and No Matches were found");
            }
            if(condition == 1) {
                System.out.println("No Matches could be found");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args) {
        try {
            if (args.length == 0) {
                System.out.println("Usage: java REsearch <standardInput> <file>");
            }
            REsearch Rs = new REsearch(args[0]);
        } catch (Error e) {
            System.err.println(e);
        }
    }
}
