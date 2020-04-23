import java.util.Deque;
import java.util.LinkedList;

//Given the output of the compiler, and a file, find and output any matching patterns
public class REsearch {
    class State {
        char c;
        int n1;
        int n2;

        public State(char ch, int n1, int n2) {
            this.c = c;
            this.n1 = n1;
            this.n2 = n2;
        }
    }
    //Deque has addFirst(e), removeFirst(), and addLast(e) eg. "deque.addLast(e)"
    //Scan state has -2 as both next states so you know that this is the scan
    State scan = new State('s', -2, -2);
    Deque<State> deque = new LinkedList<State>();

    public static void main(String [] args) {
        System.out.println("Hello World");
    }
}
