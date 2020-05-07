import java.util.*;
import java.io.*;


//Given the output of the compiler as standard input and a file, find and output any matching patterns
public class REsearch {
    List<State> states;    //Global array to store all the states
    Deque<Integer> deque;    //Deque to facilitate checking of current and next states

    private class State {
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
    }

    public State getState(int stateNum) {
            State s = new State(0, 0, 0, 0);
            for (int i = 0; i < states.size(); i++) {
                if (states.get(i).stateNum == stateNum) {
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
            while((line = br.readLine()) != null) {
                String[] splitString = line.split("\t");     //Split the input by Tab Char to get out the state, c, n1, n2
                state = Integer.parseInt(splitString[0]);
                c = Integer.parseInt(splitString[1]);
                n1 = Integer.parseInt(splitString[2]);
                n2 = Integer.parseInt(splitString[3]);
                State s = new State(state, c, n1 , n2);
                states.add(s);      //Add the new state to the array of states
                deque.addFirst(s.stateNum);
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    public REsearch(String inputFile) {
        states = new ArrayList<State>();
        deque = new ArrayDeque<Integer>();
        int condition = 0;      //Indicates the reason the program has ended
        State scan = new State(-1,0, 0, 0);     //Scan has stateNum of -1, to indicate that it is the state
        deque.addFirst(scan.stateNum);
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

                if(currentState.c == -1) {
                    if(deque.size() > 0) {      //If you popped scan but there are still more states, put it on the botton of deque
                        State newScan = new State(-1,0, 0, 0);
                        deque.addLast(newScan.stateNum);
                    }
                    else {      //Otherwise, scan was popped because deque is empty so match has failed
                        condition = 1;
                        break;
                    }
                }
                else if(currentState.c == r) {     //If the char is a match to our text
                    if(currentState.n1 == currentState.n2) {        //If n1 is == to n2, only add one of them
                        deque.addLast(currentState.n1);
                    }
                    else {
                        deque.addLast(currentState.n1);
                        deque.addLast(currentState.n2);
                    }
                    p++;
                    while(p == r) {       //Moves the pointer along to check the next char if it is the same, and so on until whole pattern is found
                        System.out.println((char)r);
                        p++;
                        r++;
                        condition = 2;
                    }
                }
                else if(currentState.c == '\0') {      //If the char is a Branching State
                    deque.addFirst(currentState.n1);
                    deque.addFirst(currentState.n2);
                }
            }
            fis.close();
            if(condition == 0) {
                System.out.println("The End Of The File was reached, and No Matches were found");
            }
            if(condition == 1) {
                System.out.println("No Matches could be found");
            }
            if(condition == 2) {
                System.out.println("A Match has been found");
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
